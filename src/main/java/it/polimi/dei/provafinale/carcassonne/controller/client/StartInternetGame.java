package it.polimi.dei.provafinale.carcassonne.controller.client;

import it.polimi.dei.provafinale.carcassonne.Constants;
import it.polimi.dei.provafinale.carcassonne.controller.ConnectionLostException;
import it.polimi.dei.provafinale.carcassonne.view.CarcassonneFrame;
import it.polimi.dei.provafinale.carcassonne.view.ViewManager;
import it.polimi.dei.provafinale.carcassonne.view.game.GamePanel;
import it.polimi.dei.provafinale.carcassonne.view.game.SwingGamePanel;
import it.polimi.dei.provafinale.carcassonne.view.game.TextualGamePanel;
import it.polimi.dei.provafinale.carcassonne.view.menu.InternetGamePanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Class StartInternetGame implements an ActionListener in order to manage the
 * beginning of a new internet game.
 * 
 */
public class StartInternetGame implements ActionListener {

	private InternetGamePanel internetGamePanel;

	/**
	 * StartInternetGame constructor. Creates a new instance of class
	 * StartInternetGame.
	 * 
	 * @param gamePanel
	 *            the InternetGamePanel we want to show at the start of the
	 *            game.
	 */
	public StartInternetGame(InternetGamePanel gamePanel) {
		this.internetGamePanel = gamePanel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		/* Server ip address. */
		String host;
		/* Server port. */
		int port;

		internetGamePanel.setUIActive(false);

		/* Retrieves settings from view. */
		/* Connection protocol selection. */
		int connectionType = internetGamePanel.getConnType();
		/* Graphic mode selection. */
		int viewType = internetGamePanel.getViewType();
		/* Values in debug mode. */
		if (Constants.DEBUG_MODE) {
			host = Constants.DEBUG_ADDR;
			port = Constants.DEBUG_PORT;
		}
		/* Values in normal mode. */
		else {
			/* Retrieving the ip address. */
			host = internetGamePanel.getIPFieldValue();
			/* Retrieving the port number. */
			String portString = internetGamePanel.getPortFieldValue();
			/* An error has occurred in server ip address or port number. */
			if (host.equals("") || portString.equals("")) {
				internetGamePanel.setNotifyText(Constants.FIELDS_ERROR);
				internetGamePanel.setUIActive(true);
				return;
			}
			/* No error in server ip address and port number. */
			else {
				try {
					/*
					 * Conversion into integer of the String that represents the
					 * port.
					 */
					port = Integer.parseInt(portString);
				} catch (NumberFormatException nfe) {
					internetGamePanel.setNotifyText(Constants.PORT_ERROR);
					internetGamePanel.setUIActive(true);
				}
			}
		}
		/* Set up client interface */
		ClientInterface ci;
		/* Socket mode. */
		if (connectionType == 0) {
			ci = new ClientSocketInterface(host, port);
		}
		/* RMI mode. */
		else {
			ci = new ClientRMIInterface(host);
		}
		/* Set up view interface */
		GamePanel panel;
		/* Swing mode. */
		if (viewType == Constants.VIEW_TYPE_GUI) {
			panel = new SwingGamePanel();
		}
		/* Textual mode. */
		else if (viewType == Constants.VIEW_TYPE_TEXTUAL) {
			panel = new TextualGamePanel();
		}
		/* An error has occurred. */
		else {
			throw new RuntimeException();
		}
		/* Append game panel. */
		CarcassonneFrame frame = ViewManager.getInstance().getFrame();
		frame.setGamePanel(panel);
		frame.changeMainPanel(CarcassonneFrame.GAMEPANEL);
		/* Starting connection. */
		try {
			ci.connect();
			ClientController.startNewMatchController(ci, panel);
			internetGamePanel.setNotifyText(Constants.MATCH_IS_STARTING);
		} catch (ConnectionLostException cle) {
			internetGamePanel.setNotifyText(Constants.CONNECTION_ERROR);
			internetGamePanel.setUIActive(true);
		}
	}
}