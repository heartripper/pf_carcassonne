package it.polimi.dei.provafinale.carcassonne.controller.server;

import it.polimi.dei.provafinale.carcassonne.controller.Message;
import it.polimi.dei.provafinale.carcassonne.controller.client.CarcassonneRMIClient;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Remote interface of Carcassonne RMI Server.
 * */
public interface CarcassonneRMIServer extends Remote {
	/**
	 * Registers a player's request to play.
	 * 
	 * @param client
	 *            - the remote interface of the client who required to play.
	 * @param request
	 *            - player's request.
	 * */
	void register(CarcassonneRMIClient client, Message request)
			throws RemoteException;

	/**
	 * Tests the connectivity between client and server.
	 * */
	void poll() throws RemoteException;
}
