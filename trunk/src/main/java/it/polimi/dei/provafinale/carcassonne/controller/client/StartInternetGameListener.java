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
 * Class StartInternetGameListener implements an ActionListener in order to
 * manage the beginning of a new internet game.
 * 
 */
public class StartInternetGameListener implements ActionListener {

	private InternetGamePanel internetGamePanel;


	
	/**
	 * StartInternetGameListener constructor. Creates a new instance of class
	 * StartInternetGameListener.
	 * 
	 * @param gamePanel
	 *            the InternetGamePanel we want to show at the start of the
	 *            game.
	 */
	public StartInternetGameListener(InternetGamePanel gamePanel) {
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
		
		/*Create client interface*/
		ClientInterface ci;
		if (connectionType == 0) {
			ci = new ClientSocketInterface(ipAddress, port);
		} else {
			ci = new ClientRMIInterface(ipAddress);
		}

		try {
			ci.connect();
		} catch (ConnectionLostException cle) {
			internetGamePanel.setNotifyText(Constants.CONNECTION_ERROR);
			internetGamePanel.setUIActive(true);
			return;
		}

		/*Create game panel*/
		GamePanel panel;
		if (viewType == Constants.VIEW_TYPE_GUI) {
			panel = new SwingGamePanel();
		} else if (viewType == Constants.VIEW_TYPE_TEXTUAL) {
			panel = new TextualGamePanel();
		} else {
			throw new RuntimeException();
		}
		
		ClientController.startNewMatchController(ci, panel);		
		CarcassonneFrame frame = ViewManager.getInstance().getFrame();
		frame.setGamePanel(panel);
		frame.changeMainPanel(CarcassonneFrame.GAMEPANEL);
	}

}