package it.polimi.dei.provafinale.carcassonne;

import it.polimi.dei.provafinale.carcassonne.controller.server.CarcassonneServer;

public class MainServer {
	public static void main(String[] args) {
		CarcassonneServer server = new CarcassonneServer(CarcassonneServer.BOTH, 12345);
		server.start();
	}
}
