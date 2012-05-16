package it.polimi.dei.provafinale.carcassonne.model.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import it.polimi.dei.provafinale.carcassonne.model.gameinterface.GameInterface;
import it.polimi.dei.provafinale.carcassonne.model.gameinterface.Message;
import it.polimi.dei.provafinale.carcassonne.model.gameinterface.MessageType;
import it.polimi.dei.provafinale.carcassonne.model.player.PlayerColor;

public class ServerGameInterface implements GameInterface {

	private final int RECONNECTION_TIMEOUT = 20 * 1000;

	private Vector<PlayerConnection> connections;
	private String matchName = Integer.toHexString(hashCode());

	public ServerGameInterface(Vector<PlayerConnection> connections) {
		this.connections = connections;
	}

	@Override
	public int askPlayerNumber() {
		return connections.size();
	}

	@Override
	public Message readFromPlayer(PlayerColor player)
			throws PlayersDisconnectedException {
		int index = PlayerColor.indexOf(player);
		PlayerConnection pc = connections.get(index);
		MessageType type = null;
		String payload = null;

		String resp = readPlayerResponse(pc);

		String[] split = resp.split(":");
		if (split[0].equals("ruota")) {
			type = MessageType.ROTATION;
		} else if (split[0].equals("posiziona")) {
			type = MessageType.PLACE;
			payload = split[1];
		} else if (split[0].equals("pedina")) {
			type = MessageType.FOLLOWER;
			payload = split[1];
		} else if (split[0].equals("passo")) {
			type = MessageType.PASS;
		} else {
			type = MessageType.INVALID_MOVE;
		}

		return new Message(type, payload);
	}

	@Override
	public void sendPlayer(PlayerColor color, Message msg)
			throws PlayersDisconnectedException {
		String protocolMessage;

		switch (msg.type) {
		case ROTATION:
			protocolMessage = "ruotata: " + msg.payload;
			break;
		case NEXT:
			protocolMessage = "prox: " + msg.payload;
			break;
		case INVALID_MOVE:
			protocolMessage = "mossa non valida";
			break;
		default:
			System.out.println("SERVER INTERFACE > Nothing to send "
					+ "for message type " + msg.type + "");
			return;
		}

		int index = PlayerColor.indexOf(color);
		PlayerConnection pc = connections.get(index);
		sendMsgToPlayer(protocolMessage, pc);
	}

	@Override
	public void sendAllPlayer(Message msg) throws PlayersDisconnectedException {
		String protocolMessage;

		switch (msg.type) {
		case START:
			initPlayers(msg.payload);
			return;
		case TURN:
			protocolMessage = "turno: " + msg.payload;
			break;
		case UPDATE:
			protocolMessage = "aggiorna: " + msg.payload;
			break;
		default:
			System.out.println("SERVER INTERFACE > Nothing to send "
					+ "for message type " + msg.type + "");
			return;
		}
		sendMsgToAll(protocolMessage);
	}

	private void initPlayers(String firstTile)
			throws PlayersDisconnectedException {
		PlayersDisconnectedException pde = null;
		for (PlayerConnection pc : connections) {
			if (!pc.isActive())
				continue;

			PlayerColor color = PlayerColor.valueOf(connections.indexOf(pc));
			String protocolMessage = String.format("inizio: %s, %s, %s",
					firstTile, matchName, color);
			try {
				sendMsgToPlayer(protocolMessage, pc);
			} catch (PlayersDisconnectedException e) {
				ArrayList<PlayerColor> disconnected = e
						.getDisconnectedPlayers();
				if (pde == null)
					pde = new PlayersDisconnectedException(disconnected);
				else
					pde.add(disconnected);
			}

			if (pde != null)
				throw pde;
		}
	}

	// Helpers to send protocol messages to clients and read from them
	private void sendMsgToAll(String msg) throws PlayersDisconnectedException {
		PlayersDisconnectedException pde = null;
		for (PlayerConnection pc : connections) {
			if (!pc.isActive())
				continue;
			try {
				sendMsgToPlayer(msg, pc);
			} catch (PlayersDisconnectedException e) {
				ArrayList<PlayerColor> disconnected = e
						.getDisconnectedPlayers();
				if (pde == null)
					pde = new PlayersDisconnectedException(disconnected);
				else
					pde.add(disconnected);
			}
		}

		if (pde != null)
			throw pde;
	}

	private void sendMsgToPlayer(String msg, PlayerConnection pc)
			throws PlayersDisconnectedException {
		int index = connections.indexOf(pc);
		while (true) {
			try {
				pc.out.writeObject(msg);
				pc.out.flush();
				System.out.printf("SERVER>PLAYER%s: %s\n", index, msg);
				return;
			} catch (IOException e) {
				handleDisconnection(pc);
			}
		}
	}

	private String readPlayerResponse(PlayerConnection pc)
			throws PlayersDisconnectedException {
		int connectionIndex = connections.indexOf(pc);
		while (true) {
			try {
				String req = (String) pc.in.readObject();
				System.out
						.printf("PLAYER%s>SERVER: %s\n", connectionIndex, req);
				return req;
			} catch (ClassNotFoundException e) {
				System.out
						.println("FATAL ERROR: Error in request from client.");
				System.exit(1); // This error should't occur so just crash.
				return null;
			} catch (IOException e) {
				handleDisconnection(pc);
			}
		}
	}

	private synchronized void handleDisconnection(PlayerConnection connection)
			throws PlayersDisconnectedException {
		int index = connections.indexOf(connection);
		System.out.println("Player " + index
				+ "disconnected. Waiting for reconnection...");
		connection.setActive(false);
		try {
			wait(RECONNECTION_TIMEOUT);
		} catch (InterruptedException ie) {

		}

		connection = connections.get(index);

		if (!connection.isActive()) {
			PlayerColor color = PlayerColor.valueOf(index);
			System.out.printf("Player %s did not reconnect.\n", color);
			throw new PlayersDisconnectedException(color);
		}
	}

	/**
	 * Reconnects player identified by given color to the match, then notifies
	 * the thread that the player reconnected.
	 * 
	 * @param color
	 *            - The color of the player who's reconnecting
	 * @param pc
	 *            - The new player connection
	 */
	public synchronized void reconnectPlayer(PlayerColor color,
			PlayerConnection pc) {
		int index = PlayerColor.indexOf(color);
		connections.set(index, pc);
		notify();
	}

	/**
	 * Gives this match name.
	 * 
	 * @return - this match name
	 */
	public String getName() {
		return matchName;
	}
}