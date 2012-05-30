package it.polimi.dei.provafinale.carcassonne.controller.server;

/**
 * Runs a server to accept players requests to play. It can handles both Sockets
 * and RMI requests using separate monitors.
 */
public class CarcassonneServer {

	public static final int BOTH = 0;
	public static final int SOCKET = 1;
	public static final int RMI = 2;

	private int technology;
	private int socketPort;

	/**
	 * Constructs a new CarcassonneServer instance.
	 * 
	 * @param technology
	 *            - an integer representing the technology to use.
	 * @param socketPort
	 *            - the port used to listen to socket requests.
	 * */
	public CarcassonneServer(int technology, int socketPort) {
		this.socketPort = socketPort;
		this.technology = technology;
	}

	/**
	 * Starts the monitors server was instructed to use.
	 * */
	public void start() {
		/* Create matches handler. */
		MatchesManager manager = new MatchesManager();
		Thread managerThread = new Thread(manager);
		managerThread.start();
		/* If requested, start socket request monitor. */
		if (technology == SOCKET || technology == BOTH) {
			SocketRequestMonitor srm = new SocketRequestMonitor(manager,
					socketPort);
			Thread socketThread = new Thread(srm);
			socketThread.start();
		}
		/* If requested, start RMI request monitor */
		if (technology == RMI || technology == BOTH) {
			RMIRequestMonitor.registerRMIRequestMonitor(manager);
		}
		/* Waits matches manager thread */
		try {
			managerThread.join();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
	
}
