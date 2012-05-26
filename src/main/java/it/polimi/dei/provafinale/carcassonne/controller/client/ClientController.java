package it.polimi.dei.provafinale.carcassonne.controller.client;

import it.polimi.dei.provafinale.carcassonne.view.viewInterface.ViewInterface;

public class ClientController {

	private static Thread thread;
	private static ClientControllerImpl currentMatch;

	/**
	 * Initializes an instance of MatchController, if no threads are already
	 * initialized.
	 * 
	 * @param ci
	 *            a ClientInterface.
	 * @param vi
	 *            a ViewInterface.
	 */
	public static void startNewMatchController(ClientInterface ci,
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
