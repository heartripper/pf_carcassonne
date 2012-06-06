package it.polimi.dei.provafinale.carcassonne.controller.client;

import it.polimi.dei.provafinale.carcassonne.PlayerColor;
import it.polimi.dei.provafinale.carcassonne.controller.ClientInterface;
import it.polimi.dei.provafinale.carcassonne.controller.ConnectionLostException;
import it.polimi.dei.provafinale.carcassonne.controller.Message;
import it.polimi.dei.provafinale.carcassonne.controller.MessageBuffer;
import it.polimi.dei.provafinale.carcassonne.controller.MessageType;

/**
 * Class ClientControllerImpl implements the client controller.
 * 
 */
public class ClientControllerImpl implements Runnable {

	private final int maxReconnectionAttempts = 10;
	private final int reconnectionInterval = 30 * 1000;

	private MessageBuffer messageBuffer;
	private ClientInterface clientInterface;
	private ViewInterface viewInterface;
	private String matchName;

	private boolean endGame = false;
	private boolean endTurn = false;
	private PlayerColor clientPlayerColor;

	/**
	 * MatchControllerImpl constructor. Creates a new instance of class
	 * MatchControllerImpl.
	 * 
	 * @param ci
	 *            a ClientInterface to communicate with the controller.
	 * @param vi
	 *            a ViewInterface to communicate with the graphic interface.
	 */
	public ClientControllerImpl(ClientInterface ci, ViewInterface vi) {
		this.clientInterface = ci;
		this.viewInterface = vi;
		this.messageBuffer = new MessageBuffer();
	}

	/**
	 * Sends a message to client controller.
	 * 
	 * @param msg
	 *            a message to be sent.
	 */
	public void sendMessage(Message msg) {
		messageBuffer.write(msg);
	}

	@Override
	public void run() {
		Message startMsg = readFromServer();
		/* Case start message. */
		if (startMsg.type != MessageType.START) {
			protocolOrderError("start", startMsg.type);
		}
		initializeMatch(startMsg.payload);
		/* Other types of messages. */
		while (!endGame) {

			Message turnMsg = safeRead();
			switch (turnMsg.type) {
			/* Start a turn message. */
			case TURN:
				PlayerColor color = PlayerColor.valueOf(turnMsg.payload);
				viewInterface.setCurrentPlayer(color);
				if (clientPlayerColor == null
						|| color.equals(clientPlayerColor)) {
					manageClientTurn();
				} else {
					viewInterface.updateCurrentTile(null);
					handleGlobalUpdates();
				}
				break;
			/* End game message. */
			case END:
				handleGameEnd(turnMsg.payload);
				break;
			/* Wrong message order. */
			default:
				protocolOrderError("turn' or 'end", turnMsg.type);
				break;
			}
		}
	}

	/**
	 * Manages the match initialization.
	 * 
	 * @param payload
	 *            a String that contains information about match initialization
	 *            (first tile...).
	 */
	private void initializeMatch(String payload) {
		/* Parse received start command. */
		String[] split = payload.split(",");
		matchName = split[1].trim();
		String color = split[2].trim();

		/* The controller manages the messages (only updates) of all players. */
		if (color.equals("null")) {
			clientPlayerColor = null;
		}
		/* The controller manages the message of the associated player. */
		else {
			clientPlayerColor = PlayerColor.valueOf(color);
		}
		viewInterface.initialize(payload);
	}

	/**
	 * Manages the turn.
	 */
	private void manageClientTurn() {
		/* Receives the current tile from server. */
		Message nextMessage = readFromServer();
		viewInterface.updateCurrentTile(nextMessage.payload.trim());
		/* Turn execution. */
		endTurn = false;
		while (!endTurn && !endGame) {
			Message viewMsg = readFromGUI();
			switch (viewMsg.type) {
			case ROTATE:
				handleRotation(viewMsg);
				break;
			case PLACE:
				handleTilePositioning(viewMsg);
				break;
			case FOLLOWER:
			case PASS:
				sendToServer(viewMsg);
				handleGlobalUpdates();
			default:
				break;
			}
		}
	}

	/**
	 * Reads message from the gui.
	 * 
	 * @return the read Message.
	 */
	private Message readFromGUI() {
		viewInterface.setUIActive(true);

		Message msg = messageBuffer.read();

		viewInterface.setUIActive(false);
		return msg;
	}

	/**
	 * Manages the tile rotation.
	 * 
	 * @param msg
	 *            a message of rotation.
	 */
	private void handleRotation(Message msg) {
		sendToServer(msg);
		Message response = readFromServer();
		switch (response.type) {
		case ROTATED:
			viewInterface.updateCurrentTile(response.payload.trim());
			break;
		case INVALID_MOVE:
			viewInterface.showNotify("Tile can't be rotated");
			break;
		default:
			protocolOrderError("rotated", response.type);
		}
	}

	/**
	 * Manages the tile positioning.
	 * 
	 * @param msg
	 *            a message which contains the coordinate.
	 */
	private void handleTilePositioning(Message msg) {
		String coord = msg.payload;
		/*
		 * If the two parts of coord are not equal to an expression composed by
		 * a "-" or not followed by one or more numbers the position is not
		 * valid.
		 */
		if (!coord.matches("[-]??[0-9]+,[-]??[0-9]+")) {
			viewInterface.showNotify("Position not valid.");
			return;
		}
		/* If the inserted coord is correct, it is sent to the server. */
		sendToServer(msg);
		Message response = readFromServer();
		switch (response.type) {
		/* Tile is correctly placed. */
		case UPDATE:
			handleTileUpdate(response.payload);
			viewInterface.showNotify("Tile placed.");
			return;
			/* Coord not valid. */
		case INVALID_MOVE:
			viewInterface.showNotify("Tile position not valid.");
			return;
			/* Error in the order of messages. */
		default:
			protocolOrderError("update' or 'invalid move", response.type);
			return;
		}
	}

	/* Helper methods. */
	/**
	 * Manages the safe read of a message.
	 * 
	 * @return the read message.
	 */
	private Message safeRead() {
		Message msg = readFromServer();
		while (true) {
			switch (msg.type) {
			case UPDATE:
				handleTileUpdate(msg.payload);
				endTurn = true;
				break;
			case LOCK:
				viewInterface.showNotify("A Player is not responding");
				break;
			case UNLOCK:
				viewInterface.showNotify("Player reconnected");
				break;
			case LEAVE:
				String notify = "Player %s left";
				viewInterface.showNotify(String.format(notify, msg.payload));
				break;
			default:
				return msg;
			}
		}
	}

	/**
	 * Manages the updates of the match.
	 */
	private void handleGlobalUpdates() {
		while (true) {
			Message msg = readFromServer();
			switch (msg.type) {
			case UPDATE:
				handleTileUpdate(msg.payload);
				endTurn = true;
				break;
			case LOCK:
				viewInterface.showNotify("A Player is not responding");
				break;
			case UNLOCK:
				viewInterface.showNotify("Player reconnected");
				break;
			case LEAVE:
				String notify = "Player %s left";
				viewInterface.showNotify(String.format(notify, msg.payload));
				break;
			case SCORE:
				viewInterface.updateScore(msg.payload);
				endTurn = true;
				return;
			case INVALID_MOVE:
				viewInterface.showNotify("Move not valid");
				return;
			case END:
				handleGameEnd(msg.payload);
				return;
			default:
				protocolOrderError("an update", msg.type);
			}
		}
	}

	/**
	 * Manages the tile updating.
	 * 
	 * @param command
	 *            a String representing the message sent from server.
	 */
	private void handleTileUpdate(String command) {
		viewInterface.updateGridRepresentation(command);
	}

	/**
	 * Manages the end of the game.
	 * 
	 * @param msg
	 *            a String representing the message sent from server.
	 */
	private void handleGameEnd(String msg) {
		viewInterface.showNotify("Game end.");
		viewInterface.updateScore(msg);
		endGame = true;
	}

	/*
	 * Points out that an error in the order of the received message has been
	 * occurred.
	 */
	private void protocolOrderError(String expected, MessageType received) {
		String msg = String.format(
				"Protocol order error. Expecting '%s' but received '%s'.\n",
				expected, received);
		throw new RuntimeException(msg);
	}

	/* Sends a message to the server. */
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

	/* Reads a message to the server. */
	private Message readFromServer() {
		while (true) {
			try {
				return clientInterface.readMessage();
			} catch (ConnectionLostException cle) {
				handleReconnection();
			}
		}
	}

	/* Manages the client reconnection. */
	private void handleReconnection() {
		/* Notify GUI we lost connection with server. */
		for (int i = 0; i < maxReconnectionAttempts; i++) {
			try {
				clientInterface.reconnect(matchName, clientPlayerColor);
				Message msg = readFromServer();
				if (!(msg.type == MessageType.UNLOCK)) {
					protocolOrderError("UNLOCK", msg.type);
				}
				return;
			} catch (ConnectionLostException cle) {
				/* Still can't connect; go on. */
				try {
					Thread.sleep(reconnectionInterval);
				} catch (InterruptedException e) {
					/* Nothing to do. */
				}
			}
		}
		viewInterface.showNotify("Connection with server lost");
		endGame = true;
	}
}