package it.polimi.dei.provafinale.carcassonne.controller.client;

import it.polimi.dei.provafinale.carcassonne.model.MatchHandler;
import it.polimi.dei.provafinale.carcassonne.view.viewInterface.GUIViewInterface;
import it.polimi.dei.provafinale.carcassonne.view.viewInterface.ViewInterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartLocalGame implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// Create duplex interface for Client - Match controller communication
		ClientLocalInterface cli = new ClientLocalInterface(2);

		// Start match handler
		MatchHandler mh = new MatchHandler(cli);
		Thread th = new Thread(mh);
		th.start();

		// Create view interface
		ViewInterface vi = new GUIViewInterface();

		// Create client controller
		ClientController.startNewMatchController(cli, vi);
	}

}
