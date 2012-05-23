package it.polimi.dei.provafinale.carcassonne.model.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import it.polimi.dei.provafinale.carcassonne.model.gameinterface.GameInterface;
import it.polimi.dei.provafinale.carcassonne.model.gameinterface.Message;
import it.polimi.dei.provafinale.carcassonne.model.gameinterface.MessageType;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.player.PlayerColor;

public class ServerGameInterface implements GameInterface {

	private static final int RECONNECTION_TIMEOUT = 15 * 1000;
	private Vector<PlayerConnection> playerConnections;
	private String name;

	public ServerGameInterface(Vector<PlayerConnection> playerConnections) {
		this.playerConnections = playerConnections;
		this.name = Integer.toHexString(hashCode());
	}

	@Override
	public int askPlayerNumber() {
		return playerConnections.size();
	}

	@Override
	public Message readFromPlayer(PlayerColor player)
			throws PlayersDisconnectedException {
		PlayerConnection pc = getConnectionByColor(player);
		String protocolMessage = readStringFromPlayer(pc);
		String[] split = protocolMessage.split(":");
		Message request;
		if (split[0].equals("rotate")) {
			request = new Message(MessageType.ROTATE, null);
		} else if (split[0].equals("place")) {
			request = new Message(MessageType.PLACE, split[1]);
		} else if (split[0].equals("pass")) {
			request = new Message(MessageType.PASS, null);
		} else if (split[0].equals("tile")) {
			request = new Message(MessageType.FOLLOWER, split[1]);
		} else {
			request = new Message(MessageType.INVALID_MOVE, null);
		}

		return request;
	}

	@Override
	public void sendPlayer(PlayerColor color, Message msg)
			throws PlayersDisconnectedException {
		PlayerConnection pc = getConnectionByColor(color);
		String response = msg.toProtocolMessage();		
		sendStringToPlayer(response, pc);
	}

	@Override
	public void sendAllPlayer(Message msg) throws PlayersDisconnectedException {
		if(msg.type == MessageType.START){
			initPlayers(msg.payload);
			return;
		}else{
			String response = msg.toProtocolMessage();
			sendStringToAllPlayer(response);
		}
	}

	// Helper methods
	private void initPlayers(String firstTile)
			throws PlayersDisconnectedException {
		PlayersDisconnectedException pde = null;
		for (PlayerConnection pc : playerConnections) {
			if (!pc.isActive()){
				continue;
			}
			int connectionIndex = playerConnections.indexOf(pc);
			PlayerColor color = PlayerColor.valueOf(connectionIndex);
			String protocolMessage = String.format("start: %s, %s, %s",
					firstTile, name, color);
			try {
				sendStringToPlayer(protocolMessage, pc);
			} catch (PlayersDisconnectedException e) {
				ArrayList<PlayerColor> disc = e.getDisconnectedPlayers();
				if (pde == null){
					pde = new PlayersDisconnectedException(disc);
				}
				else{
					pde.add(disc);
				}
			}

			if (pde != null){
				throw pde;
			}
		}
	}
	
	//Message send helper
	private void sendStringToPlayer(String msg, PlayerConnection pc)
			throws PlayersDisconnectedException {
		try {
			pc.out.writeObject(msg);
			pc.out.flush();
			int index = playerConnections.indexOf(pc);
			System.out.printf("S>P%s: %s\n", index, msg);
		} catch (IOException ioe) {
			handleDisconnection(pc);
		}
	}

	private void sendStringToAllPlayer(String msg)
			throws PlayersDisconnectedException {
		PlayersDisconnectedException pde = null;
		for (PlayerConnection pc : playerConnections) {
			if (!pc.isActive()){
				continue;
			}
			try {
				sendStringToPlayer(msg, pc);
			} catch (PlayersDisconnectedException e) {
				ArrayList<PlayerColor> disc = e.getDisconnectedPlayers();
				if (pde == null){
					pde = new PlayersDisconnectedException(disc);
				}
				else{
					pde.add(disc);
				}
			}
		}
		if (pde != null){
			throw pde;
		}
	}

	private String readStringFromPlayer(PlayerConnection pc)
			throws PlayersDisconnectedException {
		int connectionIndex = playerConnections.indexOf(pc);
		// Try until a message is read or the player disconnects.
		while (true) {
			try {
				String msg = (String) pc.in.readObject();
				System.out.printf("P%s>S: %s\n", connectionIndex, msg);
				return msg;
			} catch (ClassNotFoundException cnf) {
				System.out
						.println("Fatal error during communication with player.");
				System.exit(1);
			} catch (IOException ioe) {
				handleDisconnection(pc);
			}
		}
	}

	private PlayerConnection getConnectionByColor(PlayerColor color) {
		int colorIndex = PlayerColor.indexOf(color);
		return playerConnections.get(colorIndex);
	}

	// disconnections / reconnections handlers
	private synchronized void handleDisconnection(PlayerConnection pc)
			throws PlayersDisconnectedException {
		int connectionIndex = playerConnections.indexOf(pc);
		pc.setActive(false);
		sendStringToAllPlayer("lock");
		try {
			wait(RECONNECTION_TIMEOUT);
		} catch (InterruptedException e) {
			// TODO
		}

		PlayerConnection newConn = playerConnections.get(connectionIndex);
		PlayerColor color = PlayerColor.valueOf(connectionIndex);
		if (!newConn.isActive()) {
			sendStringToAllPlayer(String.format("leave: %s", color));
			System.out.printf("Player %s has disconnected.\n", color);
			throw new PlayersDisconnectedException(color);
		} else {
			sendStringToAllPlayer("unlock");
		}
	}

	public synchronized void reconnectPlayer(PlayerColor color,
			PlayerConnection pc) {
		int connectionIndex = PlayerColor.indexOf(color);
		playerConnections.set(connectionIndex, pc);
		notifyAll();
	}

	/***
	 * Gives this match name.
	 */
	public String getName() {
		return name;
	}

}