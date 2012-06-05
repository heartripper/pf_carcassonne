package it.polimi.dei.provafinale.carcassonne.controller;


import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This is the remote interface of RMI Client.
 * */
public interface RMIClient extends Remote {

	/**
	 * Sends a message to the client.
	 * 
	 * @param msg
	 *            - the Message to send to client.
	 * */
	void sendMessageToPlayer(Message msg) throws RemoteException;

	/**
	 * Reads a message from the client.
	 * 
	 * @return the Message read from client.
	 * */
	Message readMessageFromPlayer() throws RemoteException;
}
