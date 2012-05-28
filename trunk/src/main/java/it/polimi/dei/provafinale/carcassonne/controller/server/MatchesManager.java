package it.polimi.dei.provafinale.carcassonne.controller.server;

import it.polimi.dei.provafinale.carcassonne.Constants;
import it.polimi.dei.provafinale.carcassonne.Message;
import it.polimi.dei.provafinale.carcassonne.PlayerColor;
import it.polimi.dei.provafinale.carcassonne.model.MatchHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class to manage players and matches. It handles remote players queue,
 * automatically starting a new match when:
 * <ul>
 * <li>There are 5 players waiting to play;</li>
 * <li>After a timeout there are at least 2 player waiting to play.</li>
 * </ul>
 * */
public class MatchesManager implements Runnable {

	private List<RemotePlayer> pendingPlayers;
	private Map<String, ServerGameInterface> matches;

	/**
	 * Constructs a new Matches Manager.
	 * */
	public MatchesManager() {
		this.pendingPlayers = new ArrayList<RemotePlayer>();
		this.matches = new HashMap<String, ServerGameInterface>();
	}

	@Override
	public void run() {
		checkPlayersPeriodically();
	}

	/**
	 * Handles player's request to play. If player's request is CONNECT, manager
	 * puts it into pending players queue. Then if the size of the queue is 5,
	 * it starts a new match for those players and resets the queue. On the
	 * other and, if player's request is RECONNECT, manager tries to reconnect
	 * the player to the right match.
	 * 
	 * @param player
	 *            - the player who asked to play.
	 * @param request
	 *            - player's request
	 * */
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
	/**
	 * Starts a new match.
	 * 
	 * @param players
	 *            - the list of the players who will take part in the starting
	 *            match.
	 * */
	private void startMatch(List<RemotePlayer> players) {
		ServerGameInterface sgi = new ServerGameInterface(players);
		MatchHandler mh = new MatchHandler(sgi);
		Thread th = new Thread(mh);
		th.start();
		matches.put(sgi.getName(), sgi);
	}

	/**
	 * Periodically checks if players queue size is at least 2. If so, it starts
	 * a new match for those players.
	 * */
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
