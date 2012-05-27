package it.polimi.dei.provafinale.carcassonne.controller.server;

import it.polimi.dei.provafinale.carcassonne.controller.client.CarcassonneRMIClient;
import it.polimi.dei.provafinale.carcassonne.model.gameinterface.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CarcassonneRMIServer extends Remote {
	public void register(CarcassonneRMIClient client, Message request)
			throws RemoteException;

	public void poll() throws RemoteException;
}
