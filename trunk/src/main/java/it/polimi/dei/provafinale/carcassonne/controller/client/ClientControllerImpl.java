package it.polimi.dei.provafinale.carcassonne.controller.client;

import it.polimi.dei.provafinale.carcassonne.model.gameinterface.Message;
import it.polimi.dei.provafinale.carcassonne.model.gameinterface.MessageBuffer;
import it.polimi.dei.provafinale.carcassonne.model.gameinterface.MessageType;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.Coord;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.card.Tile;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.card.SidePosition;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.card.TileGrid;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.player.PlayerColor;
import it.polimi.dei.provafinale.carcassonne.view.viewInterface.ViewInterface;

public class ClientControllerImpl implements Runnable {

	private final int maxReconnectionAttempts = 10;
	private final int reconnectionInterval = 30 * 1000;

	private MessageBuffer messageBuffer;
	private ClientInterface clientInterface;
	private ViewInterface viewInterface;
	private String matchName;

	private TileGrid grid;
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
		this.grid = new TileGrid();
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
			/* Read turn information from server. */
			Message turnMsg = readFromServer();
			switch (turnMsg.type) {
			/* Start a turn message. */
			case TURN:
				PlayerColor color = PlayerColor.valueOf(turnMsg.payload);
				setCurrentPlayer(color);
				if (clientPlayerColor == null
						|| color.equals(clientPlayerColor)) {
					manageClientTurn();
				} else {
					manageOtherPlayerTurn();
				}
				break;
			/* End game message. */
			case END:
				viewInterface.showNotify("Game end.");
				viewInterface.updateScore(turnMsg.payload);
				endGame = true;
				break;
			/* Wrong message order. */
			default:
				protocolOrderError("turn' or 'end", turnMsg.type);
				break;
			}
		}
	}

	private void initializeMatch(String payload) {
		/* Parse received start command. */
		String[] split = payload.split(",");
		/* Manages the first tile. */
		handleTileUpdate(split[0] + ", 0, 0");
		/* Manages the identificator of the match. */
		matchName = split[1].trim();
		/* Manages the color value of the associated player. */
		String color = split[2].trim();
		/* The controller manages the messages (only updates) of all players. */
		if (color.equals("null")) {
			clientPlayerColor = null;
		}
		/* The controller manages the message of the associated player. */
		else {
			clientPlayerColor = PlayerColor.valueOf(color);
		}
		/* Manages the number of players. */
		int playerNumber = Integer.parseInt(split[3].trim());
		/* View initialization. */
		viewInterface.initialize(grid, playerNumber, clientPlayerColor);
		/* Updates the grid that now contains the first tile. */
		viewInterface.updateGridRepresentation();
	}

	private void setCurrentPlayer(PlayerColor color) {
		/* Communicate the player that has to play the turn. */
		String msg = String.format("It's player %s turn.", color.getFullName());
		viewInterface.showNotify(msg);
	}

	/* Turn management. */
	private void manageClientTurn() {
		/* Receives the current tile from server. */
		Message nextMessage = readFromServer();
		viewInterface.updateCurrentTile(nextMessage.payload.trim());
		/* Turn execution. */
		endTurn = false;
		while (!endTurn) {
			Message viewMsg = readFromGUI();
			switch (viewMsg.type) {
			case ROTATE:
				handleRotation(viewMsg);
				break;
			case PLACE:
				handleTilePositioning(viewMsg);
				break;
			case FOLLOWER:
				handleFollowerPositioning(viewMsg);
				break;
			case PASS:
				handlePass(viewMsg);
				break;
			default:
				break;
			}
		}
		/* Handle tile and score updates */
		handleUpdates();
	}

	private Message readFromGUI() {
		/* Activates the interface. */
		viewInterface.setUIActive(true);
		/* Reads a message from buffer. */
		Message msg = messageBuffer.read();
		/* Deactivates the interface. */
		viewInterface.setUIActive(false);
		return msg;
	}

	/* Manages the tile rotation. */
	private void handleRotation(Message msg) {
		/* Sends the rotation message to the server. */
		sendToServer(msg);
		/* Reads the answer from the server. */
		Message response = readFromServer();
		/* Rotation successful. */
		if (response.type == MessageType.ROTATED) {
			viewInterface.updateCurrentTile(response.payload.trim());
		}
		/* Rotation unsuccessful. */
		else {
			protocolOrderError("rotated", response.type);
		}
	}

	/* Manages the tile positioning. */
	private void handleTilePositioning(Message msg) {
		/* Puts the payload of bufferedMessage in coord. */
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
		/* Reads the server answer. */
		Message response = readFromServer();
		switch (response.type) {
		/* Tile is correctly placed. */
		case UPDATE:
			handleTileUpdate(response.payload);
			viewInterface.updateGridRepresentation();
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

	/* Manages the follower positioning. */
	private void handleFollowerPositioning(Message msg) {
		/* Sends to the server the message contained in bufferedMessage. */
		sendToServer(msg);
		/* Reads the server response and analyzes it. */
		Message response = readFromServer();
		switch (response.type) {
		/* The follower is correctly put on tile. */
		case UPDATE:
			handleTileUpdate(response.payload);
			viewInterface.showNotify("Follower put on given side.");
			endTurn = true;
			return;
			/* The selected position for the follower in invalid. */
		case INVALID_MOVE:
			viewInterface.showNotify("You can't add a follower there.");
			return;
			/* Error in the order of messages. */
		default:
			protocolOrderError("update' or 'invalid move", response.type);
			return;
		}
	}

	/* Manages the change of player. */
	private void handlePass(Message msg) {
		/* Sends to the server the message contained in bufferedMessage. */
		sendToServer(msg);
		/* Reads the server response and analyzes it. */
		Message response = readFromServer();
		switch (response.type) {
		/* Correct change of player. */
		case UPDATE:
			endTurn = true;
			break;
		/* Case the current player hasn't positioned the tile. */
		case INVALID_MOVE:
			viewInterface.showNotify("You must add your card before passing.");
			break;
		/* Error in the order of messages. */
		default:
			protocolOrderError("update' or 'invalid move", response.type);
		}
	}

	/* Updates the view of its player with notifications from the other players. */
	private void manageOtherPlayerTurn() {
		viewInterface.setUIActive(false);
		handleUpdates();
	}

	/* Helper methods. */

	/* Manages the updates of the match. */
	private void handleUpdates() {
		Message msg = null;
		boolean updatesEnded = false;
		/* Analyzes all the updates. */
		while (!updatesEnded) {
			/* Possible updates. */
			msg = readFromServer();
			switch (msg.type) {
			/* Updates the grid with the given tile. */
			case UPDATE:
				handleTileUpdate(msg.payload);
				break;
			/*
			 * It's the last update we can receive, containing the current
			 * score.
			 */
			case SCORE:
				updatesEnded = true;
				break;
			/* Error in the order of messages. */
			default:
				protocolOrderError("update' or 'score", msg.type);
			}
		}
		/* Update game panel. */
		viewInterface.updateGridRepresentation();
		/* Update score. */
		viewInterface.updateScore(msg.payload);
	}

	/* Manages the updates on the tile. */
	private void handleTileUpdate(String command) {
		String[] split = command.split(",");
		/* Creates a new card based on the arrived message. */
		Tile newTile = new Tile(split[0].trim());
		/* Finds out the coordinates. */
		int x = Integer.parseInt(split[1].trim());
		int y = Integer.parseInt(split[2].trim());
		/* Sets the tile coordinates. */
		Coord c = new Coord(x, y);
		Tile oldTile = grid.getTile(c);
		/* Case the player hasn't put the tile in the specified coordinates yet. */
		if (oldTile == null) {
			grid.putTile(newTile, c);
		}
		/*
		 * Case the player has already put the tile in the specified
		 * coordinates.
		 */
		else {
			/* Update followers. */
			for (SidePosition pos : SidePosition.values()) {
				PlayerColor newFollower = newTile.getSide(pos).getFollower();
				oldTile.getSide(pos).setFollower(newFollower);
			}
		}
	}

	/*
	 * Points out that an error in the order of the received message has been
	 * occourred.
	 */
	private void protocolOrderError(String expected, MessageType received) {
		String msg = String.format(
				"Protocol order error. Expecting '%s' but received '%s'.\n",
				expected, received);
		throw new RuntimeException(msg);
	}

	/* Helpers to send and receive messages. */

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
		Message msg;
		while (true) {
			try {
				msg = clientInterface.readMessage();
				/*
				 * Handles lock and returns first correct message in case
				 * another player doesn't answer.
				 */
				if (msg.type == MessageType.LOCK) {
					return handleLock();
				}
				/* Returns the received message. */
				else {
					return msg;
				}
			} catch (ConnectionLostException cle) {
				handleReconnection();
			}
		}
	}

	/* Manages the lock message. */
	private Message handleLock() {
		viewInterface.showNotify("A player is not responding.");
		Message msg = readFromServer();
		switch (msg.type) {
		/* The player left the game. */
		case LEAVE:
			/* Points out that a player has left the game. */
			String format = "Player %s left the game.\n";
			viewInterface.showNotify(String.format(format, msg.payload));
			/* Reads the updates messages from server. */
			Message update = readFromServer();
			while (update.type == MessageType.UPDATE) {
				handleTileUpdate(update.payload);
				update = readFromServer();
			}
			return update;
			/* The player has come back to the game. */
		case UNLOCK:
			viewInterface.showNotify("Player reconnected.");
			return readFromServer();
			/* Error in the order of received messages. */
		default:
			protocolOrderError("leave' or 'unlock", msg.type);
			return null;
		}
	}

	/* Manages the client reconnection. */
	private void handleReconnection() {
		/* Notify GUI we lost connection with server. */
		for (int i = 0; i < maxReconnectionAttempts; i++) {
			try {
				clientInterface.reconnect(matchName, clientPlayerColor);
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
		/* Notify the user that connection with server was lost. */
		Thread.currentThread().interrupt();
	}

}