package it.polimi.dei.provafinale.carcassonne;

import it.polimi.dei.provafinale.carcassonne.controller.server.CarcassonneServer;

/**
 * The class MainServer contains the main method of the Server.
 * 
 */
public class MainServer {
	
	private MainServer(){
		
	}
	
	/**
	 * Main method of Server.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		CarcassonneServer server = new CarcassonneServer(
				CarcassonneServer.BOTH, 12345);
		server.start();
	}
}
