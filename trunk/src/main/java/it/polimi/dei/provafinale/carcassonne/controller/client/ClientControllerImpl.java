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

	private static final int maxReconnectionAttempts = 10;
	private static final int reconnectionInterval = 30 * 1000;

	private MessageBuffer messageBuffer;
	private final ClientInterface clientInterface;
	private final ViewInterface viewInterface;
	private String matchName;
	private Thread responseMonitor;
	
	private boolean endGame = false;
	private PlayerColor clientColor;

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

		responseMonitor = new Thread(new ServerResponseMonitor());
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
		responseMonitor.start();
		while (!endGame) {
			Message msg = messageBuffer.read();
			viewInterface.setUIActive(false);
			sendToServer(msg);
		}
	}

	/**
	 * Sends a message to the server.
	 * 
	 * @param msg
	 *            the message to be sent.
	 */
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

	/**
	 * Reads a message to the server.
	 * 
	 * @return the message from server.
	 */
	private Message readFromServer() {
		while (true) {
			try {
				return clientInterface.readMessage();
			} catch (ConnectionLostException cle) {
				handleReconnection();
			}
		}
	}

	/**
	 * Manages the client reconnection.
	 */
	private void handleReconnection() {
		/* Notify GUI we lost connection with server. */
		for (int i = 0; i < maxReconnectionAttempts; i++) {
			try {
				clientInterface.reconnect(matchName, clientColor);
				Message msg = readFromServer();
				if (!(msg.type == MessageType.UNLOCK)) {
					throw new RuntimeException(
							"Unlock expected after disconnection.");
				}
				return;
			} catch (ConnectionLostException cle) {
				/* Still can't connect: go on. */
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

	private void setClientColor(PlayerColor color) {
		clientColor = color;
	}

	private void setGameEnd() {
		endGame = true;
	}

	/**
	 * ServerResponseMonitor class monitors the response received from the
	 * server.
	 * */
	private class ServerResponseMonitor implements Runnable {

		private boolean currentTileAdded = false;
		private boolean handlingLock = false;

		private PlayerColor clientColor;
		private PlayerColor currentColor;

		@Override
		public void run() {
			while (true) {
				Message resp = readFromServer();
				switch (resp.type) {
				case START:
					initializeMatch(resp.payload);
					break;
				case TURN:
					PlayerColor color = PlayerColor.valueOf(resp.payload);
					currentColor = color;
					viewInterface.setCurrentPlayer(color);
					break;
				case UPDATE:
					viewInterface.updateGridRepresentation(resp.payload);
					if (!currentTileAdded && !handlingLock) {
						currentTileAdded = true;
						updateViewIfNecessary();
					}
					break;
				case NEXT:
					viewInterface.updateCurrentTile(resp.payload);
					updateViewIfNecessary();
					currentTileAdded = false;
					break;
				case ROTATED:
					viewInterface.updateCurrentTile(resp.payload);
					updateViewIfNecessary();
					break;
				case INVALID_MOVE:
					viewInterface.showNotify("Invalid move");
					updateViewIfNecessary();
					break;
				case SCORE:
					viewInterface.updateScore(resp.payload);
					break;
				case LOCK:
					handlingLock = true;
					viewInterface.showNotify("A player is not responding.");
					break;
				case UNLOCK:
					handlingLock = false;
					viewInterface.showNotify("Player reconnected.");
					break;
				case LEAVE:
					handlingLock = false;
					String msg = String.format("Player %s left.", resp.payload);
					viewInterface.showNotify(msg);
					break;
				case END:
					viewInterface.showNotify("Game ended.");
					viewInterface.updateScore(resp.payload);
					setGameEnd();
					return; /* Exit from thread */
				default:
					throw new RuntimeException("Unknown update type: "
							+ resp.type);
				}

				System.out.println("Response monitor handled: " + resp);
			}
		}

		/**
		 * Initializes client controller with informations received from the
		 * server.
		 * */
		private void initializeMatch(String payload) {
			String[] split = payload.split(",");
			matchName = split[1].trim();
			String color = split[2].trim();

			if (color.equals("null")) {
				clientColor = null;
			} else {
				clientColor = PlayerColor.valueOf(color);
				setClientColor(clientColor);
			}
			viewInterface.initialize(payload);
		}

		/**
		 * Activates the user interface if necessary.
		 * */
		private void updateViewIfNecessary() {
			if (clientColor == null || clientColor == currentColor) {
				viewInterface.setUIActive(true);
			}
		}
	}
}