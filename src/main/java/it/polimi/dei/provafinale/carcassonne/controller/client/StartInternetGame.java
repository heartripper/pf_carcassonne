package it.polimi.dei.provafinale.carcassonne.controller.client;

import it.polimi.dei.provafinale.carcassonne.Constants;
import it.polimi.dei.provafinale.carcassonne.view.game.GamePanel;
import it.polimi.dei.provafinale.carcassonne.view.game.SwingGamePanel;
import it.polimi.dei.provafinale.carcassonne.view.game.TextualGamePanel;
import it.polimi.dei.provafinale.carcassonne.view.menu.InternetGamePanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartInternetGame implements ActionListener{

	private InternetGamePanel internetGamePanel;
	
	public StartInternetGame(InternetGamePanel gamePanel){
		this.internetGamePanel = gamePanel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String host;
		int port;
		
		internetGamePanel.setUIActive(false);
		
		/*Retrieves settings from view*/
		int connectionType = internetGamePanel.getConnType();
		int viewType = internetGamePanel.getViewType();
		
		if(Constants.DEBUG_MODE){
			host = Constants.DEBUG_ADDR;
			port = Constants.DEBUG_PORT;
		} else {
			host = internetGamePanel.getIPFieldValue();
			String portString = internetGamePanel.getPortFieldValue();
			
			if(host.equals("") || portString.equals("")){
				internetGamePanel.setNotifyText(Constants.FIELDS_ERROR);
				internetGamePanel.setUIActive(true);
				return;
			} else {
				try{
					port = Integer.parseInt(portString);
				}catch(NumberFormatException nfe){
					internetGamePanel.setNotifyText(Constants.PORT_ERROR);
					internetGamePanel.setUIActive(true);
				}
			}
		}
		
		/*Set up client interface*/
		ClientInterface ci;
		if(connectionType == 0){
			ci = new ClientSocketInterface(host, port);
		} else {
			ci = new ClientRMIInterface(host);
		}
		
		/*Set up view interface*/
		GamePanel panel;
		if(viewType == Constants.VIEW_TYPE_GUI){
			panel = new SwingGamePanel();
		} else if(viewType == Constants.VIEW_TYPE_TEXTUAL) {
			panel = new TextualGamePanel();
		} else {
			panel = null;
		}
		
		try{
			ci.connect();
			ClientController.startNewMatchController(ci, panel);
			internetGamePanel.setNotifyText(Constants.MATCH_IS_STARTING);
		}catch(ConnectionLostException cle){
			internetGamePanel.setNotifyText(Constants.CONNECTION_ERROR);
			internetGamePanel.setUIActive(true);
		}
	}
}