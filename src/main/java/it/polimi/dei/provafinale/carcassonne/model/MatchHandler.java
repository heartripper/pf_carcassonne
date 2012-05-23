package it.polimi.dei.provafinale.carcassonne.model;

import java.util.ArrayList;
import java.util.List;

import it.polimi.dei.provafinale.carcassonne.model.gameinterface.GameInterface;
import it.polimi.dei.provafinale.carcassonne.model.gameinterface.Message;
import it.polimi.dei.provafinale.carcassonne.model.gameinterface.MessageType;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.Coord;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.Match;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.card.Card;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.card.SidePosition;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.player.PlayerColor;
import it.polimi.dei.provafinale.carcassonne.model.server.PlayersDisconnectedException;

public class MatchHandler implements Runnable {

	private Match match;
	private PlayerColor currentPlayer;
	private Card currentTile;
	private boolean currentPlayerDisconnected;
	private boolean currentTileAdded;
	private boolean currentTurnEnd;

	private GameInterface gameInterface;

	public MatchHandler(GameInterface gameInterface) {
		this.gameInterface = gameInterface;
	}

	public void startGame() {
		int playerNumber = gameInterface.askPlayerNumber();
		match = new Match(playerNumber);

		// Initialize players
		String firstTileRep = match.getFirstTile().toString();
		sendMessage(new Message(MessageType.START, firstTileRep));

		while (match.hasMoreCards()) {
			currentTile = match.drawCard();
			currentPlayer = match.getNextPlayer();

			// Send turn information
			sendMessage(new Message(MessageType.TURN, currentPlayer.toString()));

			// Send card information to currentPlayer
			String currentTileRep = currentTile.toString();
			sendMessage(new Message(MessageType.NEXT, currentTileRep));

			currentTurnEnd = false;
			currentTileAdded = false;
			while (!currentTurnEnd && !currentPlayerDisconnected) {
				Message req = readFromCurrentPlayer();
				if (currentPlayerDisconnected){
					break;
				}
				Message resp;
				if (req.type == MessageType.ROTATE && !currentTileAdded) {
					resp = handleTileRotation();
				} else if (req.type == MessageType.PLACE && !currentTileAdded) {
					resp = handleTilePlacing(req.payload);
				} else if (req.type == MessageType.FOLLOWER && currentTileAdded) {
					resp = handleFollowerPlacing(req.payload);
					currentTurnEnd = (resp.type == MessageType.UPDATE);
				} else if (req.type == MessageType.PASS && currentTileAdded) {
					resp = handlePass();
					currentTurnEnd = true;
				} else {
					resp = new Message(MessageType.INVALID_MOVE, null);
				}

				sendMessage(resp);
			}

			// Turn end
			handleTurnEnd();
		}
		// Match end
		handleMatchEnd();
	}

	// Helpers to manage turn
	private Message handleTileRotation() {
		currentTile.rotate();
		String payload = currentTile.toString();
		return new Message(MessageType.ROTATED, payload);
	}

	private Message handleTilePlacing(String payload) {
		String[] split = payload.split(",");
		int x = Integer.parseInt(split[0].trim());
		int y = Integer.parseInt(split[1].trim());

		Message resp;
		if (match.putTile(currentTile, new Coord(x, y))) {
			String update = getUpdateTileMsg(currentTile);
			resp = new Message(MessageType.PLACE, update);
			currentTileAdded = true;
		} else {
			resp = new Message(MessageType.INVALID_MOVE, null);
		}

		return resp;
	}

	private Message handleFollowerPlacing(String payload) {
		SidePosition position = SidePosition.valueOf(payload.trim());
		Message response;
		if (match.putFollower(currentTile, position, currentPlayer)) {
			String update = getUpdateTileMsg(currentTile);
			response = new Message(MessageType.UPDATE, update);
		} else {
			response = new Message(MessageType.INVALID_MOVE, null);
		}

		return response;
	}

	private Message handlePass(){
		String update = getUpdateTileMsg(currentTile);
		return new Message(MessageType.UPDATE, update);
	}
	
	private void handleTurnEnd() {
		// Send tiles updates.
		List<Card> updatedTile = match
				.checkForCompletedEntities(currentTile);
		for (Card c : updatedTile) {
			Message msg = new Message(MessageType.UPDATE, getUpdateTileMsg(c));
			sendMessage(msg);
		}

		// Send scores update.
		Message msg = new Message(MessageType.SCORE, getScoreMsg());
		sendMessage(msg);
	}

	private void handleMatchEnd() {
		match.finalizeMatch();
		Message msg = new Message(MessageType.END, getScoreMsg());
		sendMessage(msg);
	}

	private String getUpdateTileMsg(Card tile) {
		String rep = tile.toString();
		Coord c = tile.getCoordinates();
		String update = String.format("%s, %s, %s", rep, c.getX(), c.getY());
		return update;
	}

	private String getScoreMsg() {
		int[] scores = match.getScores();
		StringBuilder payload = new StringBuilder();
		for (int i = 0; i < scores.length; i++) {
			PlayerColor color = PlayerColor.valueOf(i);
			payload.append(String.format("%s=%s, ", color, scores[i]));
		}
		return payload.toString().trim();
	}

	// helpers to send messages.
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

	//Reads a message from current player.
	private Message readFromCurrentPlayer() {
		try {
			return gameInterface.readFromPlayer(currentPlayer);
		} catch (PlayersDisconnectedException pde) {
			handleDisconnection(pde);
			return null;
		}
	}

	private void handleDisconnection(PlayersDisconnectedException pde) {
		if (pde.getDisconnectedPlayers().contains(currentPlayer)){
			currentPlayerDisconnected = true;
		}
		try {
			for (PlayerColor c : pde.getDisconnectedPlayers())
				match.removePlayer(c);
		} catch (NotEnoughPlayersException nep) {
			// TODO: what to do when there are not enough player left
			System.out.println("There are not enough players left.");
			System.exit(1);
		}
	}

	// Runnable methods
	@Override
	public void run() {
		this.startGame();
	}

}
