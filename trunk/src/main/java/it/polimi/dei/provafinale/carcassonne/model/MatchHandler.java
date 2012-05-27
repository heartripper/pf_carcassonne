package it.polimi.dei.provafinale.carcassonne.model;

import java.util.List;

import it.polimi.dei.provafinale.carcassonne.controller.server.PlayersDisconnectedException;
import it.polimi.dei.provafinale.carcassonne.model.gameinterface.GameInterface;
import it.polimi.dei.provafinale.carcassonne.model.gameinterface.Message;
import it.polimi.dei.provafinale.carcassonne.model.gameinterface.MessageType;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.Coord;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.Match;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.player.PlayerColor;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.tile.SidePosition;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.tile.Tile;

public class MatchHandler implements Runnable {

	private Match match;
	private PlayerColor currentPlayer;
	private Tile currentTile;
	private boolean currentPlayerDisconnected;
	private boolean currentTileAdded;
	private boolean currentTurnEnd;

	private GameInterface gameInterface;

	/**
	 * MatchHandler constructor. Creates a new instance of class MatchHandler.
	 * 
	 * @param gameInterface
	 *            an instance of class GameInterface
	 */
	public MatchHandler(GameInterface gameInterface) {
		this.gameInterface = gameInterface;
	}

	/**
	 * Initializes and manages the match execution.
	 */
	public void startGame() {
		/* Getting players number from interface and initializing match. */
		int playerNumber = gameInterface.askPlayerNumber();
		match = new Match(playerNumber);

		/* Initialize players */
		String firstTileRep = match.getFirstTile().toString();
		sendMessage(new Message(MessageType.START, firstTileRep));

		while (match.hasMoreCards()) {
			/* Draw the current tile. */
			currentTile = match.drawTile();
			/* Get the player that have to play in the current turn. */
			currentPlayer = match.getNextPlayer();
			/* Send turn information. */
			sendMessage(new Message(MessageType.TURN, currentPlayer.toString()));
			/* Send card information to currentPlayer. */
			String currentTileRep = currentTile.toString();
			sendMessage(new Message(MessageType.NEXT, currentTileRep));
			/* Turn execution. */
			currentTurnEnd = false;
			currentTileAdded = false;
			while (!currentTurnEnd && !currentPlayerDisconnected) {
				/* Waiting for messages from current player. */
				Message req = readFromCurrentPlayer();
				/* Player disconnected. */
				if (currentPlayerDisconnected) {
					break;
				}
				Message resp;
				/* Managing rotation. */
				if (req.type == MessageType.ROTATE && !currentTileAdded) {
					resp = handleTileRotation();
				}
				/* Managing tile placement on grid. */
				else if (req.type == MessageType.PLACE && !currentTileAdded) {
					resp = handleTilePlacing(req.payload);
				}
				/* Managing follower addition on tile. */
				else if (req.type == MessageType.FOLLOWER && currentTileAdded) {
					resp = handleFollowerPlacing(req.payload);
					currentTurnEnd = (resp.type == MessageType.UPDATE);
				}
				/* Managing end turn request. */
				else if (req.type == MessageType.PASS && currentTileAdded) {
					resp = handlePass();
					currentTurnEnd = true;
				}
				/* Managing invalid requests. */
				else {
					resp = new Message(MessageType.INVALID_MOVE, null);
				}

				sendMessage(resp);
			}

			handleTurnEnd();
		}
		/* Match end. */
		handleMatchEnd();
	}

	/* Helper methods to manage turn. */

	/* Manages tile rotation. */
	private Message handleTileRotation() {
		currentTile.rotate();
		String payload = currentTile.toString();
		return new Message(MessageType.ROTATED, payload);
	}

	/* Manages tile placement. */
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

	/* Manages follower placement. */
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

	/* Manages end turn request. */
	private Message handlePass() {
		String update = getUpdateTileMsg(currentTile);
		return new Message(MessageType.UPDATE, update);
	}

	/* Manages end turn. */
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

	/* Manages end match. */
	private void handleMatchEnd() {
		match.finalizeMatch();
		Message msg = new Message(MessageType.END, getScoreMsg());
		sendMessage(msg);
	}

	/* Helpers to send messages. */

	/* Update tile message. */
	private String getUpdateTileMsg(Tile tile) {
		String rep = tile.toString();
		Coord c = tile.getCoordinates();
		return String.format("%s, %s, %s", rep, c.getX(), c.getY());
	}

	/* Score message. */
	private String getScoreMsg() {
		int[] scores = match.getScores();
		StringBuilder payload = new StringBuilder();
		for (int i = 0; i < scores.length; i++) {
			PlayerColor color = PlayerColor.valueOf(i);
			payload.append(String.format("%s=%s, ", color, scores[i]));
		}
		return payload.toString().trim();
	}

	private void sendMessage(Message msg) {
		try {
			switch (msg.type) {
			case START:
			case TURN:
			case UPDATE:
			case SCORE:
			case END:
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

	/* Reads a message from current player. */
	private Message readFromCurrentPlayer() {
		try {
			return gameInterface.readFromPlayer(currentPlayer);
		} catch (PlayersDisconnectedException pde) {
			handleDisconnection(pde);
			return null;
		}
	}

	private void handleDisconnection(PlayersDisconnectedException pde) {
		if (pde.getDisconnectedPlayers().contains(currentPlayer)) {
			currentPlayerDisconnected = true;
		}
		try {
			for (PlayerColor c : pde.getDisconnectedPlayers()) {
				match.removePlayer(c);
			}
		} catch (NotEnoughPlayersException nep) {
			// TODO: what to do when there are not enough player left
			System.out.println("There are not enough players left.");
			// TODO: End match
			Thread.currentThread().interrupt();
		}
	}

	/* Runnable methods */

	@Override
	public void run() {
		this.startGame();
	}

}
