package it.polimi.dei.provafinale.carcassonne.controller.client;

import it.polimi.dei.provafinale.carcassonne.controller.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Remote interface of RMI Client
 * */
public interface CarcassonneRMIClient extends Remote {

	/**
	 * Sends a message to client.
	 * 
	 * @param msg
	 *            - the Message to send to client
	 * */
	void sendMessageToPlayer(Message msg) throws RemoteException;

	/**
	 * Reads a message from the client.
	 * @return the Message read from client
	 * */
	Message readMessageFromPlayer() throws RemoteException;
}
