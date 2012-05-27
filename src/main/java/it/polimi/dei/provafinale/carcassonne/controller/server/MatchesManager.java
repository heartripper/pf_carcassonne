package it.polimi.dei.provafinale.carcassonne.controller.server;

import it.polimi.dei.provafinale.carcassonne.Constants;
import it.polimi.dei.provafinale.carcassonne.model.MatchHandler;
import it.polimi.dei.provafinale.carcassonne.model.gameinterface.Message;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.player.PlayerColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatchesManager implements Runnable {

	private List<RemotePlayer> pendingPlayers;
	private Map<String, ServerGameInterface> matches;

	public MatchesManager() {
		this.pendingPlayers = new ArrayList<RemotePlayer>();
		this.matches = new HashMap<String, ServerGameInterface>();
	}

	@Override
	public void run() {
		checkPlayersPeriodically();
	}

	public synchronized void enqueuePlayer(RemotePlayer player, Message request) {
		switch (request.type) {
		case CONNECT:
			pendingPlayers.add(player);
			int size = pendingPlayers.size();
			System.out.printf("Player added (%s/%s)\n", size,
					Constants.MAX_PLAYER_NUMBER);
			if (size == Constants.MAX_PLAYER_NUMBER) {
				startMatch(pendingPlayers);
				pendingPlayers = new ArrayList<RemotePlayer>();
				System.out.println("Match started.");
			}
			break;

		case RECONNECT:
			String[] split = request.payload.split(",");
			PlayerColor color = PlayerColor.valueOf(split[0].trim());
			String matchName = split[1].trim();
			matches.get(matchName).reconnectPlayer(color, player);
			break;

		default:
			System.out.println("Received wrong request type: " + request);
		}
	}

	// Helpers
	private void startMatch(List<RemotePlayer> players) {
		ServerGameInterface sgi = new ServerGameInterface(players);
		MatchHandler mh = new MatchHandler(sgi);
		Thread th = new Thread(mh);
		th.start();
		matches.put(sgi.getName(), sgi);
	}

	private synchronized void checkPlayersPeriodically() {
		while (true) {
			int size = pendingPlayers.size();
			try {
				wait(Constants.PLAYER_LIST_CHECK_TIME);
				if (size >= Constants.MIN_PLAYER_NUMBER) {
					startMatch(pendingPlayers);
					pendingPlayers = new ArrayList<RemotePlayer>();
					System.out
							.printf("Server started with %s players.\n", size);
				}
			} catch (InterruptedException ie) {
				throw new RuntimeException(ie);
			}
		}
	}
}
