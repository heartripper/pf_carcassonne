package it.polimi.dei.provafinale.carcassonne.controller.server;

import it.polimi.dei.provafinale.carcassonne.Constants;
import it.polimi.dei.provafinale.carcassonne.controller.client.CarcassonneRMIClient;
import it.polimi.dei.provafinale.carcassonne.model.gameinterface.Message;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RMIRequestMonitor implements CarcassonneRMIServer {

	private MatchesManager matchesManager;

	public static void registerRMIRequestMonitor(MatchesManager matchesManager) {
		RMIRequestMonitor monitor = new RMIRequestMonitor(matchesManager);
		try {
			CarcassonneRMIServer stub = (CarcassonneRMIServer) UnicastRemoteObject
					.exportObject(monitor, 0);
			Registry registry = LocateRegistry.getRegistry();
			registry.rebind(Constants.RMI_SERVER_NAME, stub);
			System.out.println("RMI server ready.");
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private RMIRequestMonitor(MatchesManager matchesManager) {
		this.matchesManager = matchesManager;
	}

	@Override
	public void register(CarcassonneRMIClient client, Message request)
			throws RemoteException {
		RemotePlayer player = new RemoteRMIPlayer(client);
		matchesManager.enqueuePlayer(player, request);
	}

	@Override
	public void poll() throws RemoteException {
		return;
	}

}
