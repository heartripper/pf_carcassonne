package it.polimi.dei.provafinale.carcassonne;

import it.polimi.dei.provafinale.carcassonne.controller.server.CarcassonneServer;

public class MainServer {
	public static void main(String[] args) {
		/*V1*/
		//(new CarcassonneServer(12345)).startRequestMonitor();
		
		/*V2*/
		CarcassonneServer server = new CarcassonneServer(CarcassonneServer.RMI, 12345);
		server.start();
	}
}
