package it.polimi.dei.provafinale.carcassonne.controller.client;

import it.polimi.dei.provafinale.carcassonne.model.gameinterface.GameInterface;
import it.polimi.dei.provafinale.carcassonne.model.gameinterface.Message;
import it.polimi.dei.provafinale.carcassonne.model.gameinterface.MessageType;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.player.PlayerColor;
import it.polimi.dei.provafinale.carcassonne.model.server.PlayersDisconnectedException;

public class ClientLocalInterface implements GameInterface, ClientInterface {

	private Message clientMessage;
	private Message modelMessage;
	private int playerNumber;

	public ClientLocalInterface(int playerNumber) {
		this.playerNumber = playerNumber;
	}

	// Client interface
	@Override
	public void connect() throws ConnectionLostException {
		// Nothing to do here in local game.
		return;
	}

	@Override
	public synchronized void sendMessage(Message msg)
			throws ConnectionLostException {
		while(clientMessage != null){
			try{
				wait();
			}catch(InterruptedException e){}
		}
		clientMessage = msg;
		notifyAll();
//		System.out.println("Client says: " + msg.toProtocolMessage());
	}

	@Override
	public synchronized Message readMessage() throws ConnectionLostException {
		while(modelMessage == null){
			try{
				wait();
			}catch(InterruptedException e){}
		}
		Message msg = modelMessage;
		modelMessage = null;
		notifyAll();
//		System.out.println("Client reads: " + msg);
		return msg;
	}

	@Override
	public void reconnect(String matchName, PlayerColor color)
			throws ConnectionLostException {
		// Nothing to do here in local game.
		return;
	}

	// Game interface
	@Override
	public int askPlayerNumber() {
		return playerNumber;
	}

	@Override
	public synchronized Message readFromPlayer(PlayerColor player)
			throws PlayersDisconnectedException {
		while(clientMessage == null){
			try{
				wait();
			}catch(InterruptedException e){}
		}
		Message msg = clientMessage;
		clientMessage = null;
		notifyAll();
//		System.out.println("Server reads: " + msg);
		return msg;
	}

	@Override
	public synchronized void sendPlayer(PlayerColor color, Message msg)
			throws PlayersDisconnectedException {
		if (msg.type == MessageType.UPDATE_SINGLE) {
			msg = new Message(MessageType.UPDATE, msg.payload);
		}
		while(modelMessage != null){
			try{
				wait();
			}catch(InterruptedException e){}
		}
		modelMessage = msg;
		notifyAll();
//		System.out.println("Server says: " + msg.toProtocolMessage());
	}

	@Override
	public synchronized void sendAllPlayer(Message msg) throws PlayersDisconnectedException {
		if (msg.type == MessageType.START) {
			String tile = msg.payload;
			String payload = String.format("%s, %s, %s, %s", tile, "localgame",
					"null", playerNumber);
			msg = new Message(MessageType.START, payload);
		}
		while(modelMessage != null){
			try{
				wait();
			}catch(InterruptedException e){}
		}
		modelMessage = msg;
		notifyAll();
//		System.out.println("Server says to all: " + msg.toProtocolMessage());
	}
}
