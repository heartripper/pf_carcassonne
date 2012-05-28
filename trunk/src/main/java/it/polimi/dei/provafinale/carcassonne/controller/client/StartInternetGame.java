package it.polimi.dei.provafinale.carcassonne.controller.client;

import it.polimi.dei.provafinale.carcassonne.Constants;
import it.polimi.dei.provafinale.carcassonne.view.menu.InternetGamePanel;
import it.polimi.dei.provafinale.carcassonne.view.viewInterface.GUIViewInterface;
import it.polimi.dei.provafinale.carcassonne.view.viewInterface.TextualConsoleViewInterface;
import it.polimi.dei.provafinale.carcassonne.view.viewInterface.ViewInterface;

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
		ViewInterface vi;
		if(viewType == 0){
			vi = new GUIViewInterface();
		} else {
			vi = new TextualConsoleViewInterface();
		}
		
		try{
			ci.connect();
			ClientController.startNewMatchController(ci, vi);
			internetGamePanel.setNotifyText(Constants.MATCH_IS_STARTING);
		}catch(ConnectionLostException cle){
			internetGamePanel.setNotifyText(Constants.CONNECTION_ERROR);
			internetGamePanel.setUIActive(true);
		}
	}
}