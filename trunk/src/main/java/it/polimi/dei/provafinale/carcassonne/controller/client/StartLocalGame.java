package it.polimi.dei.provafinale.carcassonne.controller.client;

import it.polimi.dei.provafinale.carcassonne.Constants;
import it.polimi.dei.provafinale.carcassonne.model.MatchHandler;
import it.polimi.dei.provafinale.carcassonne.view.viewInterface.GUIViewInterface;
import it.polimi.dei.provafinale.carcassonne.view.viewInterface.TextualViewInterface;
import it.polimi.dei.provafinale.carcassonne.view.viewInterface.ViewInterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

public class StartLocalGame implements ActionListener {

	private final int[] NUM_PLAYER_VALUES = { 2, 3, 4, 5 };

	private JComboBox numPlayerBox;
	private JComboBox viewTypeBox;

	public StartLocalGame(JComboBox numPlayerBox, JComboBox viewType) {
		this.numPlayerBox = numPlayerBox;
		this.viewTypeBox = viewType;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		int numPlayers = NUM_PLAYER_VALUES[numPlayerBox.getSelectedIndex()];

		// Create duplex interface for Client - Match controller communication
		ClientLocalInterface cli = new ClientLocalInterface(numPlayers);

		// Start match handler
		MatchHandler mh = new MatchHandler(cli);
		Thread th = new Thread(mh);
		th.start();

		// Create view interface
		int viewType = viewTypeBox.getSelectedIndex();
		ViewInterface vi;
		if(viewType == Constants.VIEW_TYPE_GUI){
			vi = new GUIViewInterface();
		} else if(viewType == Constants.VIEW_TYPE_TEXTUAL){
			vi = new TextualViewInterface();
		} else {
			System.out.println("Error in view type selection: value " + viewType);
			return;
		}

		// Create client controller
		ClientController.startNewMatchController(cli, vi);
	}

}
