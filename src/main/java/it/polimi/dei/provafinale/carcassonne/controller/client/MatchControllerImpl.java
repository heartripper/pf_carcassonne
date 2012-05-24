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
import it.polimi.dei.provafinale.carcassonne.view.game.PlayerPanel;

class MatchControllerImpl implements Runnable {

	private final int MAX_RECONNECTION_ATTEMPTS = 10;
	private final int RECONNECTION_INTERVAL = 30 * 1000;

	private Message bufferedMessage = null;
	private ClientInterface clientInterface;
	private GamePanel gamePanel;
	private String matchName;

	private TileGrid grid;
	private boolean endGame = false;
	private boolean endTurn = false;
	private PlayerColor clientPlayerColor;

	public MatchControllerImpl(ClientInterface clientInterface) {
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
		if (startMsg.type != MessageType.START) {
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
				if (color == clientPlayerColor) {
					manageClientTurn();
				} else {
					manageOtherPlayerTurn();
				}
				break;
			case END:
				updateScore(turnMsg.payload);
				gamePanel.setMessageText("Game end.");
				endGame = true;
				break;
			default:
				protocolOrderError("turn' or 'end", turnMsg.type);
				break;
			}
		}
	}

	private void initializeMatch(String payload) {
		gamePanel = new GamePanel();
		// Parse command
		String[] split = payload.split(",");
		handleTileUpdate(split[0] + ", 0, 0");
		matchName = split[1].trim();
		clientPlayerColor = PlayerColor.valueOf(split[2].trim());
		int playerNumber = Integer.parseInt(split[3].trim());

		// Set game panel
		gamePanel.setTilesPanelGrid(grid);
		gamePanel.createPlayerPanels(playerNumber);
		int index = PlayerColor.indexOf(clientPlayerColor);
		gamePanel.getPlayerPanels()[index].setClientPlayer();
		gamePanel.setUIActive(false);

		// Append game panel to frame
		CarcassonneFrame frame = ViewManager.getInstance().getFrame();
		frame.setGamePanel(gamePanel);
		frame.changeMainPanel(CarcassonneFrame.GAMEPANEL);
		gamePanel.updateTileGridPanel();
	}

	private void setCurrentPlayer(PlayerColor color) {
		String msg = String.format("It's player %s turn.", color.getFullName());
		gamePanel.setMessageText(msg);
	}

	// Turn management
	private void manageClientTurn() {
		System.out.println("Manage client turn.");
		Message nextMessage = readFromServer();
		gamePanel.setCurrentTile(nextMessage.payload.trim());

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
			ViewManager.getInstance().updateView();
			// Consume buffered message;
			bufferedMessage = null;
		}
		// Handle tile and score updates
		handleUpdates();
	}

	private synchronized void readFromGUI() {
		gamePanel.setUIActive(true);
		while (bufferedMessage == null) {
			try {
				wait();
			} catch (InterruptedException ie) {}
		}
		gamePanel.setUIActive(false);
	}

	private void handleRotation() {
		sendToServer(bufferedMessage);
		Message response = readFromServer();
		if (response.type == MessageType.ROTATED) {
			gamePanel.setCurrentTile(response.payload.trim());
		} else {
			protocolOrderError("rotated", response.type);
		}
	}

	private void handleTilePositioning() {
		sendToServer(bufferedMessage);
		Message response = readFromServer();
		switch (response.type) {
		case UPDATE:
			handleTileUpdate(response.payload);
			gamePanel.updateTileGridPanel();
			return;
		case INVALID_MOVE:
			gamePanel.setMessageText("Card position not valid.");
			return;
		default:
			protocolOrderError("update' or 'invalid move", response.type);
			return;
		}
	}

	private void handleFollowerPositioning() {
		sendToServer(bufferedMessage);
		Message response = readFromServer();
		switch (response.type) {
		case UPDATE:
			handleTileUpdate(response.payload);
			gamePanel.setMessageText("Follower put on given side.");
			endTurn = true;
			return;
		case INVALID_MOVE:
			gamePanel.setMessageText("You can't add a follower there.");
			return;
		default:
			protocolOrderError("update' or 'invalid move", response.type);
			return;
		}
	}

	private void manageOtherPlayerTurn() {
		gamePanel.setUIActive(false);
		handleUpdates();
	}
	
	// Helpers
	private void handleUpdates() {
		Message msg = null;
		boolean updatesEnded = false;

		while (!updatesEnded) {
			msg = readFromServer();
			switch (msg.type) {
			case UPDATE:
				handleTileUpdate(msg.payload);
				break;
			case SCORE:
				updatesEnded = true;
				break;
			default:
				protocolOrderError("update' or 'score", msg.type);
			}
		}
		//Update game panel
		gamePanel.updateTileGridPanel();
		//Update score
		updateScore(msg.payload);
	}

	private void handleTileUpdate(String command) {
		String[] split = command.split(",");
		Card newTile = new Card(split[0].trim());
		int x = Integer.parseInt(split[1].trim());
		int y = Integer.parseInt(split[2].trim());
		Coord c = new Coord(x, y);
		Card oldTile = grid.getTile(c);
		if (oldTile == null) {
			grid.putTile(newTile, c);
		} else {
			// Update followers
			for (SidePosition pos : SidePosition.values()) {
				PlayerColor newFollower = newTile.getSide(pos).getFollower();
				oldTile.getSide(pos).setFollower(newFollower);
			}
		}
	}

	private void updateScore(String msg){
		String[] scores = msg.split(",");
		PlayerPanel[] panels = gamePanel.getPlayerPanels();
		for(String s : scores){
			String[] split = s.split("=");
			PlayerColor color = PlayerColor.valueOf(split[0].trim());
			int colorIndex = PlayerColor.indexOf(color);
			int score = Integer.parseInt(split[1].trim());
			panels[colorIndex].setScore(score);
		}
	}
	
	private void protocolOrderError(String expected, MessageType received) {
		System.out.printf(
				"Protocol order error. Expecting '%s' but received '%s'.\n",
				expected, received);
		System.exit(1);
	}

	// Handlers to send and receive messages
	private void sendToServer(Message msg) {
		while (true) {
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
				if (msg.type == MessageType.LOCK) {
					return handleLock(); // Handle lock and returns first
											// correct message.
				} else {
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
		for (int i = 0; i < MAX_RECONNECTION_ATTEMPTS; i++) {
			try {
				clientInterface.reconnect(matchName, clientPlayerColor);
				return;
			} catch (ConnectionLostException cle) {
				// Still can't connect; go on.
				try {
					Thread.sleep(RECONNECTION_INTERVAL);
				} catch (InterruptedException e) {
					// Nothing to do.
				}
			}
		}
		// Notify the user that connection with server was lost.
		Thread.currentThread().interrupt();
	}
}