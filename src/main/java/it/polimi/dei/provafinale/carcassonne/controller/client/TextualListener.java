package it.polimi.dei.provafinale.carcassonne.controller.client;

import it.polimi.dei.provafinale.carcassonne.model.gameinterface.Message;
import it.polimi.dei.provafinale.carcassonne.model.gameinterface.MessageType;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;

public class TextualListener implements ActionListener{

	private JTextField textField;
	
	public TextualListener (JTextField textField){
		this.textField = textField;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String command = textField.getText();
		Message msgToServer;
		if(command.equals("rotate")){
			msgToServer = new Message(MessageType.ROTATE, null);
		}
		else if(command.equals("pass")){
			msgToServer = new Message(MessageType.PASS, null);
		}
		else if(command.matches("[NESW]")){
			msgToServer = new Message(MessageType.FOLLOWER, command);
		}
		else if(command.matches("[-]??[0-9]+,[-]??[0-9]+")){
			msgToServer = new Message(MessageType.PLACE, command);
		}
		else{
			
		}
		
	}

}
