package it.polimi.dei.provafinale.carcassonne.controller.server;

import java.util.List;

import it.polimi.dei.provafinale.carcassonne.controller.client.ConnectionLostException;
import it.polimi.dei.provafinale.carcassonne.model.gameinterface.GameInterface;
import it.polimi.dei.provafinale.carcassonne.model.gameinterface.Message;
import it.polimi.dei.provafinale.carcassonne.model.gameinterface.MessageType;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.player.PlayerColor;


public class ServerGameInterface implements GameInterface {

	private static final int RECONNECTION_TIMEOUT = 15 * 1000;
	private List<RemotePlayer> remotePlayers;
	private String name;
	private int numPlayers;

	public ServerGameInterface(List<RemotePlayer> remotePlayers) {
		this.remotePlayers = remotePlayers;
		this.name = Integer.toHexString(hashCode());
		this.numPlayers = remotePlayers.size();
	}

	@Override
	public int askPlayerNumber() {
		return numPlayers;
	}
	@Override
	public Message readFromPlayer(PlayerColor color)
			throws PlayersDisconnectedException {
		RemotePlayer remotePlayer = getRemotePlayerByColor(color);
		while(true){
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

	@Override
	public void sendPlayer(PlayerColor color, Message msg)
			throws PlayersDisconnectedException {
		RemotePlayer remotePlayer = getRemotePlayerByColor(color);
		while(true){
			try {
				Message toSend;
				if(msg.type == MessageType.UPDATE_SINGLE){
					toSend = new Message(MessageType.UPDATE, msg.payload);
				}else{
					toSend = msg;
				}
				
				int index = remotePlayers.indexOf(remotePlayer);
				String protocolMsg = msg.toProtocolMessage();
				System.out.printf("S>P%s: \"%s\"\n", index, protocolMsg);
				remotePlayer.sendMessage(toSend);
				return;
			} catch (ConnectionLostException cle) {
				handleDisconnection(remotePlayer);
			}
		}
	}

	@Override
	public void sendAllPlayer(Message msg) throws PlayersDisconnectedException {
		PlayersDisconnectedException pde = null;

		for (RemotePlayer player : remotePlayers) {
			if(!player.isActive()){
				continue;
			}

			int index = remotePlayers.indexOf(player);
			PlayerColor color = PlayerColor.valueOf(index);			
			try {
				if (msg.type == MessageType.START) {
					String payload = String.format("%s, %s, %s, %s",
							msg.payload, name, color, numPlayers);
					Message newMsg = new Message(MessageType.START, payload);
					sendPlayer(color, newMsg);
				} else {
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
		
		if (pde != null) {
			throw pde;
		}
	}

	// Helper methods
	private RemotePlayer getRemotePlayerByColor(PlayerColor color) {
		int colorIndex = PlayerColor.indexOf(color);
		return remotePlayers.get(colorIndex);
	}

	// disconnections / reconnections handlers
	private synchronized void handleDisconnection(RemotePlayer player)
			throws PlayersDisconnectedException {
		PlayersDisconnectedException pde = null;
		int playerIndex = remotePlayers.indexOf(player);
		
		player.setInactive();
		
		try{
			sendAllPlayer(new Message(MessageType.LOCK, null));
		}catch (PlayersDisconnectedException e) {
			pde = e;
		}
		
		//Let's wait for player to reconnect
		try {
			wait(RECONNECTION_TIMEOUT);
		} catch (InterruptedException e) {
			// TODO
		}

		RemotePlayer newPlayer = remotePlayers.get(playerIndex);
		PlayerColor color = PlayerColor.valueOf(playerIndex);
		Message msg;
		if (!newPlayer.isActive()) {
			msg = new Message(MessageType.LEAVE, color.toString());
			System.out.printf("Player %s has disconnected.\n", color);
			if(pde == null){
				pde = new PlayersDisconnectedException(color);
			}else{
				pde.add(color);
			}
		} else {
			msg = new Message(MessageType.UNLOCK, null);
			System.out.printf("Player %s has reconnected.\n", color);
		}
		
		try{
			sendAllPlayer(msg);
		}catch(PlayersDisconnectedException e){
			if(pde == null){
				pde = e;
			}else{
				pde.add(e.getDisconnectedPlayers());
			}
		}
		
		if(pde != null){
			throw pde;
		}
	}

	public synchronized void reconnectPlayer(PlayerColor color, RemotePlayer player) {
		int connectionIndex = PlayerColor.indexOf(color);
		remotePlayers.set(connectionIndex, player);
		notifyAll();
	}

	/***
	 * Gives this match name.
	 */
	public String getName() {
		return name;
	}

}