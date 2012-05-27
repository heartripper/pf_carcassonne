package it.polimi.dei.provafinale.carcassonne;

import it.polimi.dei.provafinale.carcassonne.controller.client.ClientInterface;
import it.polimi.dei.provafinale.carcassonne.controller.client.ClientRMIInterface;
import it.polimi.dei.provafinale.carcassonne.controller.client.ConnectionLostException;
import it.polimi.dei.provafinale.carcassonne.controller.client.ClientController;
import it.polimi.dei.provafinale.carcassonne.view.viewInterface.TextualViewInterface;
import it.polimi.dei.provafinale.carcassonne.view.viewInterface.ViewInterface;

public class MainClient {
	public static void main(String[] args) {
//		ClientInterface ci = new ClientSocketInterface("localhost", 12345);
//		try{
//			ci.connect();
//		}catch(ConnectionLostException e){
//			System.out.println("Error connecting to server.");
//			return;
//		}
		
		ClientInterface ci = new ClientRMIInterface("127.0.0.1");
		try{
			ci.connect();
		}catch(ConnectionLostException cle){
			System.out.println("connection to server failed.");
			cle.printStackTrace();
			return;
		}
		
		
		ViewInterface vi = new TextualViewInterface();
		ClientController.startNewMatchController(ci, vi);
	}
}
