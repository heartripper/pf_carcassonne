package it.polimi.dei.provafinale.carcassonne.model;

import java.util.ArrayList;

import it.polimi.dei.provafinale.carcassonne.model.card.Card;
import it.polimi.dei.provafinale.carcassonne.model.card.SidePosition;
import it.polimi.dei.provafinale.carcassonne.model.gameinterface.GameInterface;
import it.polimi.dei.provafinale.carcassonne.model.gameinterface.Message;
import it.polimi.dei.provafinale.carcassonne.model.gameinterface.MessageType;
import it.polimi.dei.provafinale.carcassonne.model.player.PlayerColor;
import it.polimi.dei.provafinale.carcassonne.model.server.PlayersDisconnectedException;

public class MatchHandler implements Runnable {

	private GameInterface gameInterface;
	private Match match;

	// Placed here to be shared between startGame and handler methods.
	private PlayerColor currentPlayerColor;
	private Card currentTile;

	/**
	 * Initializes the match handler with a game interface. The game interface
	 * allows the game handler to communicate with who handles the communication
	 * with the user in a standard way. Can be run in a separate Thread (runs
	 * automatically) or in the main thread (you have to call <b>startGame</b>
	 * method after initialization to start the game).
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
		int numPlayers = gameInterface.askPlayerNumber();
		match = new Match(numPlayers);

		// Initialize match with first card:
		String payload = match.getFirstTile().getRepresentation();
		sendMessage(new Message(MessageType.INIT, payload));

		while (match.hasMoreCards()) {
			// Initialize turn
			currentPlayerColor = match.getNextPlayer().getColor();
			currentTile = match.drawCard();

			// Send all player who will be the next playing.
			sendMessage(new Message(MessageType.TURN,
					currentPlayerColor.toString()));

			// Send current player his card.
			sendMessage(new Message(MessageType.PROX,
					currentTile.getRepresentation()));

			boolean cardAdded = false, endTurn = false;
			while (!endTurn) {
				Message cmd;
				try {
					cmd = gameInterface.readFromPlayer(currentPlayerColor);
				} catch (PlayersDisconnectedException pde) {
					handleDisconnection(pde);
					break;
				}

				if (cmd == null)
					continue;

				Message response;
				if (cmd.type == MessageType.ROTATION && !cardAdded) {
					response = manageTileRotation();
				} else if (cmd.type == MessageType.PASS && cardAdded) {
					break;
				} else if (cmd.type == MessageType.PUT_TILE && !cardAdded) {
					response = manageTilePositioning(cmd.payload);
					if (response.type == MessageType.UPDATE)
						cardAdded = true;
				} else if (cmd.type == MessageType.PUT_FOLLOWER && cardAdded) {
					response = manageFollowerPositioning(cmd.payload);
					if (response.type == MessageType.UPDATE)
						endTurn = true;
				} else {
					response = new Message(MessageType.INVALID_MOVE, null);
				}

				endTurn = sendMessage(response);
			}

			// Once the turn has ended check for completed entities.
			match.checkForCompletedEntities(currentTile);
		}

		// When the match is finished...
		match.finalizeMatch();

		// TODO Send result to players (how is this handled in protocol??)
	}

	// Helpers to manage turn operations
	private Message manageTileRotation() {
		currentTile.rotate();
		String payload = currentTile.getRepresentation();
		return new Message(MessageType.ROTATION, payload);
	}

	private Message manageTilePositioning(String payload) {
		// Retrieves coordinate from payload.
		// We are sure payload in the x,y form.
		String[] split = payload.split(",");
		int x = Integer.parseInt(split[0].trim());
		int y = Integer.parseInt(split[1].trim());

		// Tries to put the tile into the game.
		if (match.putTile(currentTile, new Coord(x, y))) {
			// If success, send update message.
			String update = String.format("%s, %s, %s",
					currentTile.getRepresentation(), x, y);
			return new Message(MessageType.UPDATE, update);
		} else {
			// If fail, send invalid move message.
			return new Message(MessageType.INVALID_MOVE, null);
		}
	}

	private Message manageFollowerPositioning(String payload) {
		SidePosition pos = SidePosition.valueOf(payload.trim());
		// Tries to add a follower for the current player on given side.
		boolean added = match.putFollower(currentTile, pos, currentPlayerColor);
		if (added) {
			// If success remove follower and send update message.
			String update = currentTile.getRepresentation();
			return new Message(MessageType.UPDATE, update);
		} else {
			return new Message(MessageType.INVALID_MOVE, null);
		}
	}

	// Helpers to send and read messages.
	private boolean sendMessage(Message msg) {
		try {
			switch (msg.type) {
			case INIT:
			case TURN:
			case UPDATE:
				gameInterface.sendAllPlayer(msg);
				break;
			default:
				gameInterface.sendPlayer(currentPlayerColor, msg);
				break;
			}
			return true;
		} catch (PlayersDisconnectedException pde) {
			handleDisconnection(pde);
			// If current player is among those who disconnected return false so
			// the caller can go on to next player.
			return pde.getDisconnectedPlayers().contains(currentPlayerColor);
		}
	}
	
	private void handleDisconnection(PlayersDisconnectedException pde) {
		ArrayList<PlayerColor> discPlayerColors = pde.getDisconnectedPlayers();
		try {
			for (PlayerColor color : discPlayerColors)
				match.removePlayer(color);
		} catch (NotEnoughPlayersException nep) {
			// TODO: what happens when there is just one player?
			System.out.println("There aren't enough players to play.");
			System.exit(1); // Just crash for now;
		}
	}

	/**
	 * Wrapper to automatically run startGame method when run in a thread.
	 * */
	@Override
	public void run() {
		this.startGame();
	}
}