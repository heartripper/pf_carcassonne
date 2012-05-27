package it.polimi.dei.provafinale.carcassonne.controller.client;

import it.polimi.dei.provafinale.carcassonne.model.gameinterface.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CarcassonneRMIClient extends Remote {
	public void sendMessageToPlayer(Message msg) throws RemoteException;
	public Message readMessageFromPlayer() throws RemoteException;
}
