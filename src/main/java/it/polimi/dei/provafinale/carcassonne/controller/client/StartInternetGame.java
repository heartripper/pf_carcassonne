package it.polimi.dei.provafinale.carcassonne.controller.client;

import it.polimi.dei.provafinale.carcassonne.view.menu.InternetGamePanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;

public class StartInternetGame implements ActionListener{

	private InternetGamePanel gamePanel;
	
	public StartInternetGame(InternetGamePanel gamePanel){
		this.gamePanel = gamePanel;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		try{
			String addr = gamePanel.getIPFieldValue();
			String portString = gamePanel.getPortFieldValue();
			
			if(addr.equals("") || portString.equals("")){
				gamePanel.getMessageLabel().setText("Please fill in all fields.");
				return;
			}
			
			int port = Integer.parseInt(portString);
			ClientInterface ci = new ClientSocketInterface(addr, port);
			ci.connect();
			ClientMatchController cc = new ClientMatchController(ci);
			Thread th = new Thread(cc);
			th.start();
		}catch(NumberFormatException nfe){
			gamePanel.getMessageLabel().setText("Please insert a valid port value.");
		}catch(ConnectionLostException cle){
			gamePanel.getMessageLabel().setText("Connection to server failed.");
		}
	}

	
	
}
