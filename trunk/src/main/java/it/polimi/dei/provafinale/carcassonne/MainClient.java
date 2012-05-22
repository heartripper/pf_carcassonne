package it.polimi.dei.provafinale.carcassonne;

import it.polimi.dei.provafinale.carcassonne.controller.client.CarcassonneClient;

public class MainClient {
	public static void main(String[] args) {
		(new CarcassonneClient("localhost", 12345)).start();
	}
}
