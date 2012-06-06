package it.polimi.dei.provafinale.carcassonne.controller.server;

import java.util.List;

import it.polimi.dei.provafinale.carcassonne.Coord;
import it.polimi.dei.provafinale.carcassonne.PlayerColor;
import it.polimi.dei.provafinale.carcassonne.controller.GameInterface;
import it.polimi.dei.provafinale.carcassonne.controller.Message;
import it.polimi.dei.provafinale.carcassonne.controller.MessageType;
import it.polimi.dei.provafinale.carcassonne.model.Match;
import it.polimi.dei.provafinale.carcassonne.model.NotEnoughPlayersException;
import it.polimi.dei.provafinale.carcassonne.model.SidePosition;
import it.polimi.dei.provafinale.carcassonne.model.Tile;

/**
 * Class MatchHandler implements Runnable in order to manage the match server
 * side.
 * 
 */
public class MatchHandler implements Runnable {

	private Match match;
	private PlayerColor currentPlayer;
	private Tile currentTile;
	private boolean currentTileAdded;
	private boolean endCurrentTurn;

	private boolean endGame = false;

	private GameInterface gameInterface;

	/**
	 * MatchHandler constructor. Creates a new instance of class MatchHandler.
	 * 
	 * @param gameInterface
	 *            an instance of class GameInterface.
	 */
	public MatchHandler(GameInterface gameInterface) {
		this.gameInterface = gameInterface;
	}

	/**
	 * Initializes and manages the match execution.
	 */
	@Override
	public void run() {

		int playerNumber = gameInterface.getPlayerNumber();
		match = new Match(playerNumber);

		String firstTileRep = match.getFirstTile().toString();
		sendMessage(new Message(MessageType.START, firstTileRep));

		while (match.hasMoreCards() && !endGame) {

			currentTile = match.drawTile();

			currentPlayer = match.getNextPlayer();
			sendMessage(new Message(MessageType.TURN, currentPlayer.toString()));
			sendMessage(new Message(MessageType.NEXT, currentTile.toString()));

			endCurrentTurn = false;
			currentTileAdded = false;
			while (!endCurrentTurn && !endGame) {

				Message req = readFromCurrentPlayer();
				/* Player disconnected. */
				if (endCurrentTurn || endGame) {
					break;
				}

				Message resp;
				
				if (req.type == MessageType.ROTATE && !currentTileAdded) {
					resp = handleTileRotation();
				}
				
				else if (req.type == MessageType.PLACE && !currentTileAdded) {
					resp = handleTilePlacing(req.payload);
				}

				else if (req.type == MessageType.FOLLOWER && currentTileAdded) {
					resp = handleFollowerPlacing(req.payload);
					endCurrentTurn = (resp.type == MessageType.UPDATE);
				}

				else if (req.type == MessageType.PASS && currentTileAdded) {
					resp = handlePass();
					endCurrentTurn = true;
				}
				/* Managing invalid requests. */
				else {
					resp = new Message(MessageType.INVALID_MOVE, null);
				}

				sendMessage(resp);
			}

			if (endGame) {
				break;
			} else {
				handleTurnEnd();
			}
		}

		handleMatchEnd();
	}

	/* Helper methods to manage turn. */

	/**
	 * Manages tile rotation.
	 * 
	 * @return a message containing the tile rotation response.
	 */
	private Message handleTileRotation() {
		currentTile.rotate();
		String payload = currentTile.toString();
		return new Message(MessageType.ROTATED, payload);
	}

	/**
	 * Manages the tile placement.
	 * 
	 * @param payload
	 *            a String containing the coordinate where to put the tile on.
	 * @return a message containing the tile placement response.
	 */
	private Message handleTilePlacing(String payload) {
		String[] split = payload.split(",");
		int x = Integer.parseInt(split[0].trim());
		int y = Integer.parseInt(split[1].trim());

		Message resp;
		/* Allowed option. */
		if (match.putTile(currentTile, new Coord(x, y))) {
			String update = getUpdateTileMsg(currentTile);
			resp = new Message(MessageType.UPDATE_SINGLE, update);
			currentTileAdded = true;
		}
		/* Not allowed option. */
		else {
			resp = new Message(MessageType.INVALID_MOVE, null);
		}

		return resp;
	}

	/**
	 * Manages follower placement.
	 * 
	 * @param payload
	 *            a String containing the position to put the follower on.
	 * @return a message containing the follower placement response.
	 */
	private Message handleFollowerPlacing(String payload) {
		SidePosition position = SidePosition.valueOf(payload.trim());
		Message response;
		/* Allowed option. */
		if (match.putFollower(currentTile, position, currentPlayer)) {
			String update = getUpdateTileMsg(currentTile);
			response = new Message(MessageType.UPDATE, update);
		}
		/* Not allowed option. */
		else {
			response = new Message(MessageType.INVALID_MOVE, null);
		}

		return response;
	}

	/**
	 * Manages the end of turn request.
	 * 
	 * @return a message of update.
	 */
	private Message handlePass() {
		String update = getUpdateTileMsg(currentTile);
		return new Message(MessageType.UPDATE, update);
	}

	/**
	 * Manages the end of a turn.
	 */
	private void handleTurnEnd() {
		/* Send tiles updates. */
		List<Tile> updatedTile = match.checkForCompletedEntities(currentTile);
		for (Tile c : updatedTile) {
			Message msg = new Message(MessageType.UPDATE, getUpdateTileMsg(c));
			sendMessage(msg);
		}
		/* Send scores update. */
		Message msg = new Message(MessageType.SCORE, getScoreMsg());
		sendMessage(msg);
	}

	/**
	 * Manages the end of the match.
	 */
	private void handleMatchEnd() {
		match.finalizeMatch();
		Message msg = new Message(MessageType.END, getScoreMsg());
		sendMessage(msg);
	}

	/* Helpers to send messages. */

	/**
	 * 
	 * @param tile
	 *            a Tile.
	 * @return a String representing a tile.
	 */
	private String getUpdateTileMsg(Tile tile) {
		String rep = tile.toString();
		Coord c = tile.getCoords();
		return String.format("%s, %s, %s", rep, c.getX(), c.getY());
	}

	/**
	 * 
	 * @return a String containing the scores.
	 */
	private String getScoreMsg() {
		int[] scores = match.getScores();
		StringBuilder payload = new StringBuilder();
		for (int i = 0; i < scores.length; i++) {
			PlayerColor color = PlayerColor.valueOf(i);
			payload.append(String.format("%s=%s, ", color, scores[i]));
		}
		return payload.toString().trim();
	}

	/**
	 * 
	 * @param msg
	 *            the message to be sent.
	 */
	private void sendMessage(Message msg) {
		try {
			switch (msg.type) {
			case START:
			case TURN:
			case UPDATE:
			case SCORE:
			case END:
			case LEAVE:
				gameInterface.sendAllPlayer(msg);
				return;
			default:
				gameInterface.sendPlayer(currentPlayer, msg);
				return;
			}
		} catch (PlayersDisconnectedException e) {
			handleDisconnection(e);
		}
	}

	/**
	 * 
	 * @return the message read from the current player.
	 */
	private Message readFromCurrentPlayer() {
		try {
			return gameInterface.readFromPlayer(currentPlayer);
		} catch (PlayersDisconnectedException pde) {
			handleDisconnection(pde);
			return null;
		}
	}

	/**
	 * Manages players disconnection.
	 * 
	 * @param pde
	 *            a PlayersDisconnectedException.
	 */
	private void handleDisconnection(PlayersDisconnectedException pde) {
		if (pde.getDisconnectedPlayers().contains(currentPlayer)) {
			endCurrentTurn = true;
		}
		try {
			for (PlayerColor color : pde.getDisconnectedPlayers()) {
				List<Tile> updates = match.removePlayer(color);
				for (Tile tile : updates) {
					Message updateMsg = new Message(MessageType.UPDATE,
							getUpdateTileMsg(tile));
					sendMessage(updateMsg);
				}
				Message leaveMsg = new Message(MessageType.LEAVE,
						color.toString());
				sendMessage(leaveMsg);
			}
		} catch (NotEnoughPlayersException nep) {
			System.out.println("There are not enough players left.");
			endGame = true;
		}
	}
}