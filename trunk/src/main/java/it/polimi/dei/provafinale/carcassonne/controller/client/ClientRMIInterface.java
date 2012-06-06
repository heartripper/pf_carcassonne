package it.polimi.dei.provafinale.carcassonne.controller.client;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import it.polimi.dei.provafinale.carcassonne.Constants;
import it.polimi.dei.provafinale.carcassonne.PlayerColor;
import it.polimi.dei.provafinale.carcassonne.controller.RMIClient;
import it.polimi.dei.provafinale.carcassonne.controller.RMIServer;
import it.polimi.dei.provafinale.carcassonne.controller.ConnectionLostException;
import it.polimi.dei.provafinale.carcassonne.controller.Message;
import it.polimi.dei.provafinale.carcassonne.controller.MessageType;
import it.polimi.dei.provafinale.carcassonne.logger.Logger;
import it.polimi.dei.provafinale.carcassonne.logger.LoggerService;

/**
 * Class ClientRMIInterface implements ClientInterface and CarcassonneRMIClient
 * in order to give an interface of a client that plays in RMI mode.
 * 
 */
public class ClientRMIInterface implements ClientInterface,
		RMIClient {

	private final int PollInterval = 5 * 1000;
	private String host;
	private Message serverBuffer, clientBuffer;
	private RMIServer server;
	private Logger logger;

	/**
	 * ClientRMIInterface constructor. Creates a new instance of class
	 * ClientRMIInterface.
	 * 
	 * @param host
	 *            a String that identifies an host.
	 */
	public ClientRMIInterface(String host) {
		this.host = host;
		this.logger = LoggerService.getService().register("RMI Interface");
	}

	/* ClientInterface methods */
	@Override
	public void connect() throws ConnectionLostException {
		Message request = new Message(MessageType.CONNECT, null);
		connectToRMIServer(request);
	}

	@Override
	public synchronized void sendMessage(Message msg)
			throws ConnectionLostException {
		while (clientBuffer != null) {
			try {
				wait(PollInterval);
				server.poll();
			} catch (InterruptedException ie) {

			} catch (RemoteException re) {
				throw new ConnectionLostException();
			}
		}

		clientBuffer = msg;
		notifyAll();
		logger.log("RMI|CLIENT|WRITE: " + msg);
		return;
	}

	@Override
	public synchronized Message readMessage() throws ConnectionLostException {
		while (serverBuffer == null) {
			try {
				wait(PollInterval);
				server.poll();
			} catch (InterruptedException ie) {

			} catch (RemoteException re) {
				throw new ConnectionLostException();
			}
		}

		Message msg = serverBuffer;
		serverBuffer = null;
		notifyAll();
		logger.log("RMI|CLIENT|READ: " + msg);
		return msg;
	}

	@Override
	public void reconnect(String matchName, PlayerColor color)
			throws ConnectionLostException {
		String payload = String.format("reconnect: %s, %s", color, matchName);
		Message request = new Message(MessageType.RECONNECT, payload);
		connectToRMIServer(request);
	}

	/* ClientRMIInterface methods */
	@Override
	public synchronized void sendMessageToPlayer(Message msg) {
		while (serverBuffer != null) {
			try {
				wait();
			} catch (InterruptedException ie) {

			}
		}

		serverBuffer = msg;
		logger.log("RMI|SERVER|WRITE: " + msg);
		notifyAll();
		return;
	}

	@Override
	public synchronized Message readMessageFromPlayer() {
		while (clientBuffer == null) {
			try {
				wait();
			} catch (InterruptedException ie) {

			}
		}

		Message msg = clientBuffer;
		clientBuffer = null;
		notifyAll();
		logger.log("RMI|SERVER|READ: " + msg);
		return msg;
	}

	// Helpers
	private void connectToRMIServer(Message request)
			throws ConnectionLostException {
		try {
			UnicastRemoteObject.exportObject(this, 0);
			Registry registry = LocateRegistry.getRegistry(host);
			server = (RMIServer) registry
					.lookup(Constants.RMI_SERVER_NAME);
			server.register(this, request);
		} catch (Exception re) {
			re.printStackTrace();
			throw new ConnectionLostException();
		}
	}
}
