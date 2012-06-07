package it.polimi.dei.provafinale.carcassonne;

import it.polimi.dei.provafinale.carcassonne.view.ViewManager;

/**
 * The class MainClient contains the main method of the Client.
 * 
 */
public class MainClient {

	private MainClient() {
		
	}
	
	/**
	 * Main method of Client.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		ViewManager.getInstance();
	}
}
