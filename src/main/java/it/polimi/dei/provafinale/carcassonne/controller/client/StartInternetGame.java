package it.polimi.dei.provafinale.carcassonne.controller.client;

import it.polimi.dei.provafinale.carcassonne.Constants;
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
		String addr;
		int port;
		
		try{
			internetGamePanel.setUIActive(false);
			if(Constants.DEBUG_MODE){
				addr = Constants.DEBUG_ADDR;
				port = Constants.DEBUG_PORT;
			} else {
				addr = internetGamePanel.getIPFieldValue();
				String portString = internetGamePanel.getPortFieldValue();
				
				if(addr.equals("") || portString.equals("")){
					internetGamePanel.setNotifyText(Constants.FIELDS_ERROR);
					internetGamePanel.setUIActive(true);
					return;
				} else {
					port = Integer.parseInt(portString);
				}
			}

			ClientInterface ci = new ClientSocketInterface(addr, port);
			ci.connect();
			MatchController.startNewMatchController(ci);
			internetGamePanel.setNotifyText(Constants.MATCH_IS_STARTING);
		}catch(NumberFormatException nfe){
			internetGamePanel.setNotifyText(Constants.PORT_ERROR);
			internetGamePanel.setUIActive(true);
		}catch(ConnectionLostException cle){
			internetGamePanel.setNotifyText(Constants.CONNECTION_ERROR);
			internetGamePanel.setUIActive(true);
		}
	}
}