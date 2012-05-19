package it.polimi.dei.provafinale.carcassonne.model.client;
import it.polimi.dei.provafinale.carcassonne.model.gameinterface.Message;


public class ClientController implements Runnable {

	private Message messageBuffer = null;
	
	public synchronized void sendMessage(Message msg){
		this.messageBuffer = msg;
		notify();
	}
	
	private void connect(){
		
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

}
