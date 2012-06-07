package it.polimi.dei.provafinale.carcassonne.controller.server;

import java.util.List;

import it.polimi.dei.provafinale.carcassonne.PlayerColor;
import it.polimi.dei.provafinale.carcassonne.controller.ConnectionLostException;
import it.polimi.dei.provafinale.carcassonne.controller.GameInterface;
import it.polimi.dei.provafinale.carcassonne.controller.Message;
import it.polimi.dei.provafinale.carcassonne.controller.MessageType;

/**
 * Class ServerGameInterface implements GameInterface in order to allow Match
 * Controller to talk to RemotePlayer over the internet.
 * 
 */
public class ServerGameInterface implements GameInterface {

	private static final int RECONNECTION_TIMEOUT = 10 * 1000;
	private List<RemotePlayer> remotePlayers;
	private String name;
	private int numPlayers;

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
	}

	/* Gets the number of players. */
	@Override
	public int getPlayerNumber() {
		return numPlayers;
	}

	/* Reads a message from a player. */
	@Override
	public Message readFromPlayer(PlayerColor color)
			throws PlayersDisconnectedException {

		RemotePlayer remotePlayer = getRemotePlayerByColor(color);
		while (true) {
			try {
				Message msg = remotePlayer.readMessage();

				int index = remotePlayers.indexOf(remotePlayer);
				String protocolMsg = msg.toProtocolMessage();

				System.out.printf("P%s>S: \"%s\"\n", index, protocolMsg);
				return msg;
			} catch (ConnectionLostException cle) {
				handleDisconnection(remotePlayer);
			}
		}
	}

	/* Sends a message to all the players. */
	@Override
	public void sendAllPlayer(Message message)
			throws PlayersDisconnectedException {
		PlayersDisconnectedException pde = new PlayersDisconnectedException();

		for (RemotePlayer player : remotePlayers) {
			if (!player.isActive()) {
				continue;
			}
			
			int index = remotePlayers.indexOf(player);
			PlayerColor color = PlayerColor.valueOf(index);
			Message toSend;
			if (message.type == MessageType.START) {
				toSend = getStartMessage(message.payload, color);
			} else {
				toSend = message;
			}
			
			System.out.printf("S>P%s: %s\n", index, toSend);
			
			try {
				sendPlayer(player, toSend);
			} catch (PlayersDisconnectedException e) {
				pde.add(e.getDisconnectedPlayers());
			}
		}

		/* Unified exception of all the disconnected players. */
		if (pde.getDisconnectedPlayers().size() != 0) {
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
	
	/* Helper methods. */

	/**
	 * Retrieves a remote player by his color.
	 * 
	 * @param color
	 *            the color of the RemotePlayer we want to retrieve.
	 * @return the RemotePlayer corresponding to the given color.
	 */
	private RemotePlayer getRemotePlayerByColor(PlayerColor color) {
		int colorIndex = PlayerColor.indexOf(color);
		return remotePlayers.get(colorIndex);
	}

	/**
	 * Manages the disconnection of a RemotePlayer.
	 * 
	 * @param player
	 *            the RemotePlayer we want to disconnect.
	 * @throws PlayersDisconnectedException
	 */
	private synchronized void handleDisconnection(RemotePlayer player)
			throws PlayersDisconnectedException {
		PlayersDisconnectedException pde = new PlayersDisconnectedException();

		int playerIndex = remotePlayers.indexOf(player);
		player.setInactive();
		/*
		 * Sends to the active players the lock message because a player is
		 * inactive and they have to wait for his possible reconnection.
		 */
		try {
			sendAllPlayer(new Message(MessageType.LOCK, null));
		} catch (PlayersDisconnectedException e) {
			pde.add(e.getDisconnectedPlayers());
		}
		/* Let's wait for player to reconnect. */
		try {
			wait(RECONNECTION_TIMEOUT);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		/*
		 * If the player is still inactive notify other players that the player
		 * has been disconnected.
		 */
		RemotePlayer newPlayer = remotePlayers.get(playerIndex);
		PlayerColor color = PlayerColor.valueOf(playerIndex);
		if (!newPlayer.isActive()) {
			System.out.printf("Player %s has disconnected.\n", color);
			pde.add(color);
			throw pde;
		} else {
			/*
			 * If the player is now active notify other players that he has
			 * reconnected.
			 */
			Message msg = new Message(MessageType.UNLOCK, null);
			System.out.printf("Player %s has reconnected.\n", color);
			try {
				sendAllPlayer(msg);
			} catch (PlayersDisconnectedException e) {
				pde.add(e.getDisconnectedPlayers());
			}

			/* Unified exception of all the disconnected players. */
			if (pde.getDisconnectedPlayers().size() != 0) {
				throw pde;
			}
		}
	}

	/**
	 * Computes start message.
	 * 
	 * @param tile
	 *            - the String representation of the first tile.
	 * @param color
	 *            - a PlayerColor
	 * @return the start message
	 * */
	private Message getStartMessage(String tile, PlayerColor color) {
		String payload = String.format("%s, %s, %s, %s", tile, name, color,
				numPlayers);
		return new Message(MessageType.START, payload);
	}

	/**
	 * Sends a message to a player.
	 * 
	 * @param player
	 *            - the RemotePlayer to whom to send the message.
	 * @param message
	 *            - the message to send
	 * */
	private void sendPlayer(RemotePlayer player, Message message)
			throws PlayersDisconnectedException {
		try {
			player.sendMessage(message);
		} catch (ConnectionLostException cle) {
			handleDisconnection(player);
		}
	}
}