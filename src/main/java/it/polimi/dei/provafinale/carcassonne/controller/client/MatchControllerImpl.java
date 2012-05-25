package it.polimi.dei.provafinale.carcassonne.controller.client;

import it.polimi.dei.provafinale.carcassonne.model.gameinterface.Message;
import it.polimi.dei.provafinale.carcassonne.model.gameinterface.MessageType;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.Coord;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.card.Card;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.card.SidePosition;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.card.TileGrid;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.player.PlayerColor;
import it.polimi.dei.provafinale.carcassonne.view.viewInterface.ViewInterface;

public class MatchControllerImpl implements Runnable {

	private final int MAX_RECONNECTION_ATTEMPTS = 10;
	private final int RECONNECTION_INTERVAL = 30 * 1000;

	private Message bufferedMessage = null;
	private ClientInterface clientInterface;
	private ViewInterface viewInterface;
	private String matchName;

	private TileGrid grid;
	private boolean endGame = false;
	private boolean endTurn = false;
	private PlayerColor clientPlayerColor;

	public MatchControllerImpl(ClientInterface ci, ViewInterface vi) {
		this.clientInterface = ci;
		this.viewInterface = vi;
		this.grid = new TileGrid();
	}

	public synchronized void sendMessage(Message msg) {
		this.bufferedMessage = msg;
		notifyAll();
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
				if (clientPlayerColor == null || color.equals(clientPlayerColor)) {
					manageClientTurn();
				} else {
					manageOtherPlayerTurn();
				}
				break;
			case END:
				viewInterface.showNotify("Game end.");
				viewInterface.updateScore(turnMsg.payload);
				endGame = true;
				break;
			default:
				protocolOrderError("turn' or 'end", turnMsg.type);
				break;
			}
		}
	}

	private void initializeMatch(String payload) {
		// Parse command
		String[] split = payload.split(",");
		handleTileUpdate(split[0] + ", 0, 0");
		matchName = split[1].trim();
		String color = split[2].trim();
		if(color.equals("null")){
			clientPlayerColor = null;
		} else {
			clientPlayerColor = PlayerColor.valueOf(color);
		}
		int playerNumber = Integer.parseInt(split[3].trim());

		viewInterface.initialize(grid, playerNumber, clientPlayerColor);
		viewInterface.updateGridRepresentation();
	}

	private void setCurrentPlayer(PlayerColor color) {
		String msg = String.format("It's player %s turn.", color.getFullName());
		viewInterface.showNotify(msg);
	}

	// Turn management
	private void manageClientTurn() {
		Message nextMessage = readFromServer();
		viewInterface.updateCurrentTile(nextMessage.payload.trim());

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
				handlePass();
				break;
			}
			// Consume buffered message;
			bufferedMessage = null;
		}
		// Handle tile and score updates
		handleUpdates();
	}

	private synchronized void readFromGUI() {
		viewInterface.setUIActive(true);
		while (bufferedMessage == null) {
			try {
				wait();
			} catch (InterruptedException ie) {
			}
		}
		viewInterface.setUIActive(false);
	}

	private void handleRotation() {
		sendToServer(bufferedMessage);
		Message response = readFromServer();
		if (response.type == MessageType.ROTATED) {
			viewInterface.updateCurrentTile(response.payload.trim());
		} else {
			protocolOrderError("rotated", response.type);
		}
	}

	private void handleTilePositioning() {
		String coord = bufferedMessage.payload;
		if (!coord.matches("[-]??[0-9]+,[-]??[0-9]+")) {
			viewInterface.showNotify("Position not valid.");
		}

		sendToServer(bufferedMessage);
		Message response = readFromServer();
		switch (response.type) {
		case UPDATE:
			handleTileUpdate(response.payload);
			viewInterface.updateGridRepresentation();
			viewInterface.showNotify("Tile placed.");
			return;
		case INVALID_MOVE:
			viewInterface.showNotify("Card position not valid.");
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
			viewInterface.showNotify("Follower put on given side.");
			endTurn = true;
			return;
		case INVALID_MOVE:
			viewInterface.showNotify("You can't add a follower there.");
			return;
		default:
			protocolOrderError("update' or 'invalid move", response.type);
			return;
		}
	}

	private void handlePass() {
		sendToServer(bufferedMessage);
		Message response = readFromServer();
		switch (response.type) {
		case UPDATE:
			endTurn = true;
			break;
		case INVALID_MOVE:
			viewInterface.showNotify("You must add your card before passing.");
			break;
		default:
			protocolOrderError("update' or 'invalid move", response.type);
		}
	}

	private void manageOtherPlayerTurn() {
		viewInterface.setUIActive(false);
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
		// Update game panel
		viewInterface.updateGridRepresentation();
		// Update score
		viewInterface.updateScore(msg.payload);
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

	private void protocolOrderError(String expected, MessageType received) {
		String msg = String.format(
				"Protocol order error. Expecting '%s' but received '%s'.\n",
				expected, received);
		viewInterface.showNotify(msg);
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
		viewInterface.showNotify("A player is not responding.");
		Message msg = readFromServer();
		switch (msg.type) {
		case LEAVE:
			String format = "Player %s left the game.\n";
			viewInterface.showNotify(String.format(format, msg.payload));
			Message update = readFromServer();
			while (update.type == MessageType.UPDATE) {
				handleTileUpdate(update.payload);
				update = readFromServer();
			}
			return update;
		case UNLOCK:
			viewInterface.showNotify("Player reconnected.");
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