package it.polimi.dei.provafinale.carcassonne.controller.server;

import it.polimi.dei.provafinale.carcassonne.Constants;
import it.polimi.dei.provafinale.carcassonne.controller.RMIClient;
import it.polimi.dei.provafinale.carcassonne.controller.RMIServer;
import it.polimi.dei.provafinale.carcassonne.controller.Message;
import it.polimi.dei.provafinale.carcassonne.controller.RemotePlayer;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Class to monitor players' requests to play made via RMI.
 * */
public class RMIRequestMonitor implements RMIServer {

	private MatchesManager matchesManager;

	/**
	 * Registers a RMI Request Monitor in the RMI Registry.
	 * 
	 * @param matchesManager
	 *            - the matches manager which will handle players' requests
	 * */
	public static void registerRMIRequestMonitor(MatchesManager matchesManager) {
		RMIRequestMonitor monitor = new RMIRequestMonitor(matchesManager);
		try {
			RMIServer stub = (RMIServer) UnicastRemoteObject
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
	public void register(RMIClient client, Message request)
			throws RemoteException {
		RemotePlayer player = new RemoteRMIPlayer(client);
		matchesManager.enqueuePlayer(player, request);
	}

	@Override
	public void poll() throws RemoteException {
		return;
	}

}
