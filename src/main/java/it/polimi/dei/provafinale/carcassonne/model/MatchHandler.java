package it.polimi.dei.provafinale.carcassonne.model;

import it.polimi.dei.provafinale.carcassonne.model.card.Card;
import it.polimi.dei.provafinale.carcassonne.model.card.SidePosition;
import it.polimi.dei.provafinale.carcassonne.model.gameinterface.GameInterface;
import it.polimi.dei.provafinale.carcassonne.model.gameinterface.Message;
import it.polimi.dei.provafinale.carcassonne.model.gameinterface.MessageType;
import it.polimi.dei.provafinale.carcassonne.model.player.PlayerColor;

public class MatchHandler implements Runnable {

	private GameInterface gameInterface;
	private Match match;

	// Placed here to be shared between startGame and handler methods.
	private PlayerColor currentPlayer;
	private Card currentTile;

	/**
	 * Initializes the match handler with a game interface. The game interface
	 * allows the game handler to communicate with who handles the communication
	 * with the user in a standard way. Can be run in a separate Thread (runs
	 * automatically) or in the main thread (you have to call <b>startGame</b> method
	 * after initialization to start the game).
	 * 
	 * @param gameInterface
	 *            - a game interface.
	 * */
	public MatchHandler(GameInterface gameInterface) {
		this.gameInterface = gameInterface;
	}

	/**
	 * Handles the match flow. Initializes the game, manages each turn and then
	 * finalizes the match.
	 */
	public void startGame() {
		int playersNumber = gameInterface.askPlayerNumber();
		match = new Match(playersNumber);

		// Initialize match with first card:
		String payload = match.getFirstCard().getRepresentation();
		sendAllPlayer(MessageType.INIT, payload);

		while (match.hasMoreCards()) {
			// Initialize turn
			currentPlayer = match.getNextPlayer().getColor();
			currentTile = match.drawCard();

			// Send all player who will be the next playing.
			sendAllPlayer(MessageType.TURN, currentPlayer.toString());

			// Send current player his card.
			sendCurrentPlayer(MessageType.PROX, currentTile.getRepresentation());

			boolean endTurn = false, cardAdded = false;
			while (!endTurn) {
				Message cmd = gameInterface.readFromPlayer(currentPlayer);
				if (cmd.type == MessageType.ROTATION && !cardAdded) {
					manageTileRotation();
				} else if (cmd.type == MessageType.PASS && cardAdded) {
					endTurn = true;
				} else if (cmd.type == MessageType.PUT_TILE && !cardAdded) {
					cardAdded = manageTilePositioning(cmd.payload);
				} else if (cmd.type == MessageType.PUT_FOLLOWER && cardAdded) {
					endTurn = manageFollowerPositioning(cmd.payload);
				} else {
					sendCurrentPlayer(MessageType.INVALID_MOVE, null);
				}
			}

			// Once the turn has ended check for completed entities.
			match.checkForCompletedEntities(currentTile);
		}

		// When the match is finished...
		match.finalizeMatch();

		// TODO Send result to players (how is this handled in protocol??)
	}

	// Helpers to manage turn operations
	private void manageTileRotation() {
		currentTile.rotate();
		String payload = currentTile.getRepresentation();
		sendCurrentPlayer(MessageType.ROTATION, payload);
	}

	private boolean manageTilePositioning(String payload) {
		// Retrieves coordinate from payload.
		// We are sure payload in the x,y form.
		String[] split = payload.split(",");
		int x = Integer.parseInt(split[0]);
		int y = Integer.parseInt(split[1]);

		// Tries to put the tile into the game.
		if (match.putTile(currentTile, new Coord(x, y))) {
			// If success, send update message.
			String update = String.format("%s, %s, %s",
					currentTile.getRepresentation(), x, y);
			sendAllPlayer(MessageType.UPDATE, update);
			return true;
		} else {
			// If fail, send invalid move message.
			sendCurrentPlayer(MessageType.INVALID_MOVE, null);
			return false;
		}

	}

	private boolean manageFollowerPositioning(String payload) {
//		try {
			SidePosition pos = SidePosition.valueOf(payload);
			// Tries to add a follower for the current player on given side.
			boolean added = match.addFollower(currentTile, pos, currentPlayer);
			if (added) {
				// If success remove follower and send update message.
				String update = currentTile.getRepresentation();
				sendAllPlayer(MessageType.UPDATE, update);
				return true;
			}else{
				sendCurrentPlayer(MessageType.INVALID_MOVE, null);	
				return false;
			}
//		} catch (Exception e) {
//			sendCurrentPlayer(MessageType.INVALID_MOVE, null);
//		}

		// If the follower was not added sends invalid move message.

//		return false;
	}

	// Helpers to send messages.
	private void sendCurrentPlayer(MessageType type, String payload) {
		Message msg = new Message(type, payload);
		gameInterface.sendPlayer(currentPlayer, msg);
	}

	private void sendAllPlayer(MessageType type, String payload) {
		Message msg = new Message(type, payload);
		gameInterface.sendAllPlayer(msg);
	}

	/**
	 * Wrapper to automatically run startGame method when run in a thread.
	 * */
	@Override
	public void run() {
		this.startGame();
	}
}
