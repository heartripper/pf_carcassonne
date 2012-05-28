package it.polimi.dei.provafinale.carcassonne.controller.client;

import it.polimi.dei.provafinale.carcassonne.model.Message;
import it.polimi.dei.provafinale.carcassonne.model.MessageType;

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
		Message msg;
		if(command.equals("rotate")){
			msg = new Message(MessageType.ROTATE, null);
		}
		else if(command.equals("pass")){
			msg = new Message(MessageType.PASS, null);
		}
		else if(command.matches("[NESW]")){
			msg = new Message(MessageType.FOLLOWER, command);
		}
		else if(command.matches("[-]??[0-9]+,[-]??[0-9]+")){
			msg = new Message(MessageType.PLACE, command);
		}
		else{
			msg = new Message(MessageType.INVALID_MOVE, null);
		}
		
		textField.setText("");
		ClientController.getCurrentMatchController().sendMessage(msg);
	}

}
