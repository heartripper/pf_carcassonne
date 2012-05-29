package it.polimi.dei.provafinale.carcassonne.controller.client;


public class ClientController {

	private static Thread thread;
	private static ClientControllerImpl currentMatch;

	private ClientController(){
		
	}
	
	/**
	 * Initializes an instance of MatchController, if no threads are already
	 * initialized.
	 * 
	 * @param ci
	 *            a ClientInterface to communicate with the controller.
	 * @param vi
	 *            a ViewInterface to communicate with the graphic interface.
	 */
	public static synchronized void startNewMatchController(ClientInterface ci,
			ViewInterface vi) {
		/* A thread already exists. */
		if (thread != null) {
			return;
		}

		currentMatch = new ClientControllerImpl(ci, vi);
		thread = new Thread(currentMatch);
		thread.start();
	}

	/**
	 * 
	 * @return the current instance of the class attribute currentMatch.
	 */
	public static ClientControllerImpl getCurrentMatchController() {
		return currentMatch;
	}

}
