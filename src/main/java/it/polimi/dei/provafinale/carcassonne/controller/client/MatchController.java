package it.polimi.dei.provafinale.carcassonne.controller.client;

import it.polimi.dei.provafinale.carcassonne.view.viewInterface.ViewInterface;

public class MatchController {

	private static Thread thread;
	private static MatchControllerImpl currentMatch;

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

		currentMatch = new MatchControllerImpl(ci, vi);
		thread = new Thread(currentMatch);
		thread.start();
	}

	/**
	 * 
	 * @return the current instance of the class attribute currentMatch.
	 */
	public static MatchControllerImpl getCurrentMatchController() {
		return currentMatch;
	}

}
