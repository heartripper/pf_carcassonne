package it.polimi.dei.provafinale.carcassonne.controller;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This is the remote interface of the RMI Server.
 * */
public interface RMIServer extends Remote {

	/**
	 * Registers a player's request to play (two possible cases: first
	 * connection or reconnection).
	 * 
	 * @param client
	 *            - the remote interface of the client who required to play.
	 * @param request
	 *            - player's request.
	 * */
	void register(RMIClient client, Message request)
			throws RemoteException;

	/**
	 * Tests the connectivity between client and server.
	 * */
	void poll() throws RemoteException;
}
