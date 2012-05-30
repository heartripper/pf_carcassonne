package it.polimi.dei.provafinale.carcassonne.controller.client;

import it.polimi.dei.provafinale.carcassonne.Constants;
import it.polimi.dei.provafinale.carcassonne.controller.server.MatchHandler;
import it.polimi.dei.provafinale.carcassonne.view.CarcassonneFrame;
import it.polimi.dei.provafinale.carcassonne.view.ViewManager;
import it.polimi.dei.provafinale.carcassonne.view.game.GamePanel;
import it.polimi.dei.provafinale.carcassonne.view.game.SwingGamePanel;
import it.polimi.dei.provafinale.carcassonne.view.game.TextualGamePanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

/**
 * Class StartLocalGame implements an ActionListener in order to manage the
 * beginning of a new local game.
 * 
 */
public class StartLocalGame implements ActionListener {

	private final int[] NUM_PLAYER_VALUES = { 2, 3, 4, 5 };

	private JComboBox numPlayerBox;
	private JComboBox viewTypeBox;

	/**
	 * StartLocalGame constructor. Creates a new instance of class
	 * StartLocalGame.
	 * 
	 * @param numPlayerBox
	 *            the number of players selected in the JComboBox.
	 * @param viewType
	 *            the view type selected in the JComboBox.
	 */
	public StartLocalGame(JComboBox numPlayerBox, JComboBox viewType) {
		this.numPlayerBox = numPlayerBox;
		this.viewTypeBox = viewType;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		int numPlayers = NUM_PLAYER_VALUES[numPlayerBox.getSelectedIndex()];
		/* Create duplex interface for Client - Match controller communication. */
		ClientLocalInterface cli = new ClientLocalInterface(numPlayers);
		/* Start match handler. */
		MatchHandler mh = new MatchHandler(cli);
		Thread th = new Thread(mh);
		th.start();
		/* Create game panel. */
		int viewType = viewTypeBox.getSelectedIndex();
		GamePanel panel;
		/* Case Swing selection. */
		if (viewType == Constants.VIEW_TYPE_GUI) {
			panel = new SwingGamePanel();
		}
		/* Case textual selection. */
		else if (viewType == Constants.VIEW_TYPE_TEXTUAL) {
			panel = new TextualGamePanel();
		}
		/* Case an error has occurred. */
		else {
			System.out.println("Error in view type selection: value "
					+ viewType);
			return;
		}
		/* Append game panel. */
		CarcassonneFrame frame = ViewManager.getInstance().getFrame();
		frame.setGamePanel(panel);
		frame.changeMainPanel(CarcassonneFrame.GAMEPANEL);
		/* Create client controller. */
		ClientController.startNewMatchController(cli, panel);
	}

}
