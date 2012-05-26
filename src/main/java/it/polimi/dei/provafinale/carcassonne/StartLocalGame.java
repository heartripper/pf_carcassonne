package it.polimi.dei.provafinale.carcassonne;

import it.polimi.dei.provafinale.carcassonne.controller.client.ClientLocalInterface;
import it.polimi.dei.provafinale.carcassonne.controller.client.ClientController;
import it.polimi.dei.provafinale.carcassonne.model.MatchHandler;
import it.polimi.dei.provafinale.carcassonne.view.viewInterface.TextualViewInterface;

public class StartLocalGame {
	public static void main(String[] args) {
		ClientLocalInterface cli = new ClientLocalInterface(2);
		//Create match handler
		MatchHandler mh = new MatchHandler(cli);
		Thread th = new Thread(mh);
		th.start();
		
		//Create textual view
		TextualViewInterface vi = new TextualViewInterface();
		
		//Create client controller
		ClientController.startNewMatchController(cli, vi);
	}
}
