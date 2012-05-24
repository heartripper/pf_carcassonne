package it.polimi.dei.provafinale.carcassonne.controller.client;

import it.polimi.dei.provafinale.carcassonne.view.viewInterface.ViewInterface;

public class MatchController {

	private static Thread thread;
	private static MatchControllerImpl currentMatch;

	public static void startNewMatchController(ClientInterface ci,
			ViewInterface vi) {
		if (thread != null) {
			return;
		}

		currentMatch = new MatchControllerImpl(ci, vi);
		thread = new Thread(currentMatch);
		thread.start();
	}

	public static MatchControllerImpl getCurrentMatchController() {
		return currentMatch;
	}

}
