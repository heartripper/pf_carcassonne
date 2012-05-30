package it.polimi.dei.provafinale.carcassonne.controller.server;

import java.util.List;

import it.polimi.dei.provafinale.carcassonne.PlayerColor;
import it.polimi.dei.provafinale.carcassonne.controller.Message;
import it.polimi.dei.provafinale.carcassonne.controller.MessageType;
import it.polimi.dei.provafinale.carcassonne.controller.client.ConnectionLostException;
import it.polimi.dei.provafinale.carcassonne.logger.Logger;
import it.polimi.dei.provafinale.carcassonne.logger.LoggerService;

/**
 * Class ServerGameInterface implements GameInterface in order to allow Match
 * Controller to talk to RemotePlayer over the internet.
 * 
 */
public class ServerGameInterface implements GameInterface {

	private static final int RECONNECTION_TIMEOUT = 15 * 1000;
	private List<RemotePlayer> remotePlayers;
	private String name;
	private int numPlayers;
	private Logger logger;

	/**
	 * ServerGameInterface constructor. Creates a new instance of class
	 * ServerGameInterface.
	 * 
	 * @param remotePlayers
	 *            a list of RemotePlayer.
	 */
	public ServerGameInterface(List<RemotePlayer> remotePlayers) {
		this.remotePlayers = remotePlayers;
		this.name = Integer.toHexString(hashCode());
		this.numPlayers = remotePlayers.size();
		this.logger = LoggerService.getService()
				.register("ServerGameInterface");
	}

	/* Gets the number of players. */
	@Override
	public int askPlayerNumber() {
		return numPlayers;
	}

	/* Reads a message from a player. */
	@Override
	public Message readFromPlayer(PlayerColor color)
			throws PlayersDisconnectedException {
		/* Retrieving the remote player associated to the given color. */
		RemotePlayer remotePlayer = getRemotePlayerByColor(color);
		while (true) {
			try {
				/* Reading the message. */
				Message msg = remotePlayer.readMessage();
				/* Log preliminaries. */
				int index = remotePlayers.indexOf(remotePlayer);
				String protocolMsg = msg.toProtocolMessage();
				/* Printing the log. */
				logger.log(String.format("P%s>S: \"%s\"\n", index, protocolMsg));
				return msg;
			} catch (ConnectionLostException cle) {
				handleDisconnection(remotePlayer);
			}
		}
	}

	/* Sends a message to a player. */
	@Override
	public void sendPlayer(PlayerColor color, Message msg)
			throws PlayersDisconnectedException {
		/* Retrieving the remote player. */
		RemotePlayer remotePlayer = getRemotePlayerByColor(color);
		while (true) {
			try {
				Message toSend;
				/* Message type: update to a single player. */
				if (msg.type == MessageType.UPDATE_SINGLE) {
					toSend = new Message(MessageType.UPDATE, msg.payload);
				}
				/* Any other message types. */
				else {
					toSend = msg;
				}
				/* Log preliminaries. */
				int index = remotePlayers.indexOf(remotePlayer);
				String protocolMsg = msg.toProtocolMessage();
				/* Printing the log. */
				logger.log(String.format("S>P%s: \"%s\"\n", index, protocolMsg));
				/* Sending message. */
				remotePlayer.sendMessage(toSend);
				return;
			} catch (ConnectionLostException cle) {
				handleDisconnection(remotePlayer);
			}
		}
	}

	/* Sends a message to all the players. */
	@Override
	public void sendAllPlayer(Message msg) throws PlayersDisconnectedException {
		PlayersDisconnectedException pde = null;
		/* Players scanning. */
		for (RemotePlayer player : remotePlayers) {
			/* Doing nothing if the player is inactive. */
			if (!player.isActive()) {
				continue;
			}
			/* Taking the index of the player. */
			int index = remotePlayers.indexOf(player);
			/* Retrieving the correspondent color. */
			PlayerColor color = PlayerColor.valueOf(index);
			try {
				/* Start message. */
				if (msg.type == MessageType.START) {
					String payload = String.format("%s, %s, %s, %s",
							msg.payload, name, color, numPlayers);
					Message newMsg = new Message(MessageType.START, payload);
					sendPlayer(color, newMsg);
				}
				/* Other messages. */
				else {
					sendPlayer(color, msg);
				}
			} catch (PlayersDisconnectedException e) {
				if (pde == null) {
					pde = e;
				} else {
					pde.add(e.getDisconnectedPlayers());
				}
			}
		}
		/* Unified exception of all the disconnected players. */
		if (pde != null) {
			throw pde;
		}
	}

	/* Helper methods. */

	/* Retrieves a remote player by his color. */
	private RemotePlayer getRemotePlayerByColor(PlayerColor color) {
		int colorIndex = PlayerColor.indexOf(color);
		return remotePlayers.get(colorIndex);
	}

	/* Disconnections / Reconnections handlers methods. */

	/* Manages the disconnection of a RemotePlayer. */
	private synchronized void handleDisconnection(RemotePlayer player)
			throws PlayersDisconnectedException {
		PlayersDisconnectedException pde = null;
		/* Retrieving the index of the player we want to disconnect. */
		int playerIndex = remotePlayers.indexOf(player);
		/* Setting the player inactive. */
		player.setInactive();
		/*
		 * Sends to the active players the lock message because a player is
		 * inactive and they have to wait for his possible reconnection.
		 */
		try {
			sendAllPlayer(new Message(MessageType.LOCK, null));
		} catch (PlayersDisconnectedException e) {
			pde = e;
		}
		/* Let's wait for player to reconnect. */
		try {
			wait(RECONNECTION_TIMEOUT);
		} catch (InterruptedException e) {
			// TODO
		}
		/* Takes the new information about the player that has been inactive. */
		RemotePlayer newPlayer = remotePlayers.get(playerIndex);
		PlayerColor color = PlayerColor.valueOf(playerIndex);
		Message msg;
		/*
		 * If the player is still inactive notify other players that the player
		 * has been disconnected.
		 */
		if (!newPlayer.isActive()) {
			msg = new Message(MessageType.LEAVE, color.toString());
			logger.log(String.format("Player %s has disconnected.\n", color));
			if (pde == null) {
				pde = new PlayersDisconnectedException(color);
			} else {
				pde.add(color);
			}
		}
		/*
		 * If the player is now active notify other players that he has
		 * reconnected.
		 */
		else {
			msg = new Message(MessageType.UNLOCK, null);
			logger.log(String.format("Player %s has reconnected.\n", color));
		}
		/* Trying to send notifies. */
		try {
			sendAllPlayer(msg);
		} catch (PlayersDisconnectedException e) {
			if (pde == null) {
				pde = e;
			} else {
				pde.add(e.getDisconnectedPlayers());
			}
		}
		/* Unified exception of all the disconnected players. */
		if (pde != null) {
			throw pde;
		}
	}

	/**
	 * Manages the reconnection of a player.
	 * 
	 * @param color
	 *            the PlayerColor of the player to reconnect.
	 * @param player
	 *            the associated RemotePlayer.
	 */
	public synchronized void reconnectPlayer(PlayerColor color,
			RemotePlayer player) {
		int connectionIndex = PlayerColor.indexOf(color);
		remotePlayers.set(connectionIndex, player);
		notifyAll();
	}

	/**
	 * 
	 * @return this match name.
	 */
	public String getName() {
		return name;
	}

}