package it.polimi.dei.provafinale.carcassonne.controller.client;

import it.polimi.dei.provafinale.carcassonne.Constants;
import it.polimi.dei.provafinale.carcassonne.controller.ClientInterface;
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
		String ipAddress;
		int port;

		internetGamePanel.setUIActive(false);

		/* Retrieves settings from view. */
		int connectionType = internetGamePanel.getConnType();
		int viewType = internetGamePanel.getViewType();

		/* Values in debug mode. */
		if (Constants.DEBUG_MODE) {
			ipAddress = Constants.DEBUG_ADDR;
			port = Constants.DEBUG_PORT;
		}
		/* Values in normal mode. */
		else {
			ipAddress = internetGamePanel.getIPFieldValue();
			String portString = internetGamePanel.getPortFieldValue();
			/* An error has occurred in server ip address or port number. */
			if (ipAddress.equals("") || portString.equals("")) {
				internetGamePanel.setNotifyText(Constants.FIELDS_ERROR);
				internetGamePanel.setUIActive(true);
				return;
			} else {
				try {
					port = Integer.parseInt(portString);
				} catch (NumberFormatException nfe) {
					internetGamePanel.setNotifyText(Constants.PORT_ERROR);
					internetGamePanel.setUIActive(true);
				}
			}
		}
		ClientInterface ci;
		/* Socket mode. */
		if (connectionType == 0) {
			ci = new ClientSocketInterface(ipAddress, port);
		}
		/* RMI mode. */
		else {
			ci = new ClientRMIInterface(ipAddress);
		}
		GamePanel panel;
		/* Swing mode. */
		if (viewType == Constants.VIEW_TYPE_GUI) {
			panel = new SwingGamePanel();
		}
		/* Textual mode. */
		else if (viewType == Constants.VIEW_TYPE_TEXTUAL) {
			panel = new TextualGamePanel();
		} else {
			throw new RuntimeException();
		}
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