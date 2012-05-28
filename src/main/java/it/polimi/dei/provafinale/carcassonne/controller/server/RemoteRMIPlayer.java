package it.polimi.dei.provafinale.carcassonne.controller.server;

import java.rmi.RemoteException;

import it.polimi.dei.provafinale.carcassonne.controller.client.CarcassonneRMIClient;
import it.polimi.dei.provafinale.carcassonne.controller.client.ConnectionLostException;
import it.polimi.dei.provafinale.carcassonne.model.Message;

public class RemoteRMIPlayer implements RemotePlayer{

	private CarcassonneRMIClient client;
	private boolean active;
	
	public RemoteRMIPlayer(CarcassonneRMIClient client){
		this.client = client;
		this.active = true;
	}

	@Override
	public Message readMessage() throws ConnectionLostException {
		try{
			return client.readMessageFromPlayer();
		}catch(RemoteException re){
			throw new ConnectionLostException();
		}
	}

	@Override
	public void sendMessage(Message msg) throws ConnectionLostException {
		try{
			client.sendMessageToPlayer(msg);
		}catch(RemoteException re){
			throw new ConnectionLostException();
		}
	}

	@Override
	public void close() throws ConnectionLostException {
		//TODO
	}

	@Override
	public boolean isActive() {
		return active;
	}

	@Override
	public void setInactive() {
		active = false;
	}
	
}
