package it.polimi.dei.provafinale.carcassonne.controller.client;

import it.polimi.dei.provafinale.carcassonne.model.gameinterface.Message;
import it.polimi.dei.provafinale.carcassonne.model.gameinterface.MessageType;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.Coord;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.card.Card;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.card.SidePosition;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.card.TileGrid;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.player.PlayerColor;
import it.polimi.dei.provafinale.carcassonne.view.CarcassonneFrame;
import it.polimi.dei.provafinale.carcassonne.view.ViewManager;
import it.polimi.dei.provafinale.carcassonne.view.game.GamePanel;
import it.polimi.dei.provafinale.carcassonne.view.game.TilesPanel;

public class ClientMatchController implements Runnable {

	private final int maxReconnectionAttempts = 10;
	private final int reconnectionInterval = 30 * 1000;
	
	private Message bufferedMessage = null;
	private ClientInterface clientInterface;
	private String matchName;

	private TileGrid grid;
	private boolean endGame = false;
	private boolean endTurn = false;
	private PlayerColor clientPlayerColor;

	public ClientMatchController(ClientInterface clientInterface) {
		this.clientInterface = clientInterface;
		this.grid = new TileGrid();
	}
	
	public synchronized void sendMessage(Message msg) {
		this.bufferedMessage = msg;
		notify();
	}

	@Override
	public void run() {
		Message startMsg = readFromServer();
		if (startMsg.type != MessageType.START){
			protocolOrderError("start", startMsg.type);
		}
		initializeMatch(startMsg.payload);

		while (!endGame) {
			// Read turn information from server
			Message turnMsg = readFromServer();
			switch (turnMsg.type) {
			case TURN:
				PlayerColor color = PlayerColor.valueOf(turnMsg.payload);
				setCurrentPlayer(color);
				if (color == clientPlayerColor){
					manageClientTurn();
				}else{
					handleUpdates();
				}
				break;
			case END:
				//TODO handle game end;
				endGame = true;
				break;
			default:
				protocolOrderError("turn' or 'end", turnMsg.type);
				break;
			}
		}
	}

	private void initializeMatch(String payload) {
		String[] split = payload.split(",");
		handleTileUpdate(split[0] + ", 0, 0");
		matchName = split[1].trim();
		clientPlayerColor = PlayerColor.valueOf(split[2].trim());
		int playerNumber = Integer.parseInt(split[3].trim());
		
		CarcassonneFrame frame  = ViewManager.getInstance().getFrame();
		GamePanel game = new GamePanel();
		
		game.createPlayerPanels(playerNumber);
		int index = PlayerColor.indexOf(clientPlayerColor);
		game.getPlayerPanels()[index].setClientPlayer();
		game.setTilesPanelGrid(grid);
		
		frame.setGamePanel(game);
		frame.changeMainPanel(CarcassonneFrame.GAMEPANEL);
	}

	private void setCurrentPlayer(PlayerColor color) {
		// TODO: Set the current player in GUI
	}

	// Turn management
	private void manageClientTurn() {
		Message nextMessage = readFromServer();
		Card nextTile = new Card(nextMessage.payload.trim());
		// TODO: Update GUI with next tile.

		endTurn = false;
		while (!endTurn) {
			readFromGUI();
			switch (bufferedMessage.type) {
			case ROTATE:
				handleRotation();
				break;
			case PLACE:
				handleTilePositioning();
				break;
			case FOLLOWER:
				handleFollowerPositioning();
				break;
			case PASS:
				sendMessage(bufferedMessage);
				endTurn = true;
				break;
			}
		}
		// Handle tile and score updates
		handleUpdates();
		bufferedMessage = null;
	}

	private synchronized void readFromGUI() {
		// Set interface active
		while (bufferedMessage == null) {
			try {
				wait();
			} catch (InterruptedException ie) {
				// TODO
			}
		}
		// Set interface inactive
	}

	private void handleRotation() {
		sendToServer(bufferedMessage);
		Message response = readFromServer();
		if (response.type == MessageType.ROTATED) {
			Card rotatedTile = new Card(response.payload.trim());
			// TODO: Show rotated tile in GUI
		}else{
			protocolOrderError("rotated", response.type);
		}
	}

	private void handleTilePositioning() {
		sendToServer(bufferedMessage);
		Message response = readFromServer();
		switch (response.type) {
		case PLACE:
			handleTileUpdate(response.payload);
			return;
		case INVALID_MOVE:
			// Notify the user of the invalid move
			return;
		default:
			protocolOrderError("place' or 'invalid move", response.type);
			return;
		}
	}

	private void handleFollowerPositioning() {
		sendToServer(bufferedMessage);
		Message response = readFromServer();
		switch (response.type) {
		case UPDATE:
			handleTileUpdate(response.payload);
			// Put follower on current tile and update GUI. the turn ends.
			return;
		case INVALID_MOVE:
			// Notify the user of the invalid move
			return;
		default:
			// Protocol order error.
			return;
		}
	}

	// Helpers
	private void handleUpdates() {
		Message msg;
		boolean updatesEnded = false;

		while (!updatesEnded) {
			msg = readFromServer();
			switch (msg.type) {
			case UPDATE:
				handleTileUpdate(msg.payload);
				break;
			case SCORE:
				updatesEnded = true;
			default:
				protocolOrderError("update' or 'score", msg.type);
			}

			// Updates score in UI
		}

	}

	private void handleTileUpdate(String command) {
		String[] split = command.split(",");
		Card newTile = new Card(split[0].trim());
		int x = Integer.parseInt(split[1].trim());
		int y = Integer.parseInt(split[2].trim());
		Coord c = new Coord(x, y);
		Card oldTile = grid.getTile(c);
		if (oldTile == null){
			grid.putTile(newTile, c);
		}
		else {
			// Update followers
			for (SidePosition pos : SidePosition.values()) {
				PlayerColor newFollower = newTile.getSide(pos).getFollower();
				oldTile.getSide(pos).setFollower(newFollower);
			}
		}
		// TODO: update UI
	}

	private void protocolOrderError(String expected, MessageType received) {
		System.out.printf(
				"Protocol order error. Expecting '%s' but received '%s'.\n",
				expected, received);
		System.exit(1);
	}

	//Handlers to send and receive messages
	private void sendToServer(Message msg) {
		while(true){
			try {
				clientInterface.sendMessage(msg);
				return;
			} catch (ConnectionLostException e) {
				handleReconnection();
			}
		}
	}

	private Message readFromServer() {
		Message msg;
		while (true) {
			try {
				msg = clientInterface.readMessage();
				if (msg.type == MessageType.LOCK){
					return handleLock(); // Handle lock and returns first
											// correct message.
				}else{
					return msg;
				}
			} catch (ConnectionLostException cle) {
				handleReconnection();
			}
		}
	}

	private Message handleLock() {
		Message msg = readFromServer();
		switch (msg.type) {
		case LEAVE:
			// TODO: Notify a player has left.
			Message update = readFromServer();
			while (update.type == MessageType.UPDATE) {
				handleTileUpdate(update.payload);
				update = readFromServer();
			}
			return update;

		case UNLOCK:
			return readFromServer();

		default:
			protocolOrderError("leave' or 'unlock", msg.type);
			return null;
		}
	}

	private void handleReconnection() {
		// Notify GUI we lost connection with server
		for(int i = 0; i< maxReconnectionAttempts; i++){
			try{
				clientInterface.reconnect(matchName, clientPlayerColor);
				return;
			}catch(ConnectionLostException cle){
				//Still can't connect; go on.
					try {
						Thread.sleep(reconnectionInterval);
					} catch (InterruptedException e) {
						// Nothing to do.
					}
			}
		}
		//Notify the user that connection with server was lost.
		Thread.currentThread().interrupt();
	}
}