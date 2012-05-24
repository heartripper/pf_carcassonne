package it.polimi.dei.provafinale.carcassonne;

import it.polimi.dei.provafinale.carcassonne.controller.client.ClientInterface;
import it.polimi.dei.provafinale.carcassonne.controller.client.ClientSocketInterface;
import it.polimi.dei.provafinale.carcassonne.controller.client.ConnectionLostException;
import it.polimi.dei.provafinale.carcassonne.controller.client.MatchController;
import it.polimi.dei.provafinale.carcassonne.view.viewInterface.TextualViewInterface;
import it.polimi.dei.provafinale.carcassonne.view.viewInterface.ViewInterface;

public class MainClient {
	public static void main(String[] args) {
		ClientInterface ci = new ClientSocketInterface("localhost", 12345);
		try{
			ci.connect();
		}catch(ConnectionLostException e){
			System.out.println("Error connecting to server.");
			return;
		}
		
		ViewInterface vi = new TextualViewInterface();
		MatchController.startNewMatchController(ci, vi);
	}
}
