package it.polimi.dei.provafinale.carcassonne;

import it.polimi.dei.provafinale.carcassonne.model.server.CarcassonneServer;

public class MainServer {
	public static void main(String[] args) {
		(new CarcassonneServer(12345)).startRequestMonitor();
	}
}
