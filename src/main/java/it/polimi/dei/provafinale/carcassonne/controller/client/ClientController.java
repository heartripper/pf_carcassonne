package it.polimi.dei.provafinale.carcassonne.controller.client;

import it.polimi.dei.provafinale.carcassonne.controller.ClientInterface;

/**
 * Class ClientController creates the client controller.
 * 
 */
public class ClientController {

	private static Thread thread;
	private static ClientControllerImpl currentMatch;

	/**
	 * ClientController constructor. Creates a new instance of class
	 * ClientController.
	 */
	private ClientController() {

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
