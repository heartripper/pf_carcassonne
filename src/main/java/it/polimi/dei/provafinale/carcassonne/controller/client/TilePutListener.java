package it.polimi.dei.provafinale.carcassonne.controller.client;

import it.polimi.dei.provafinale.carcassonne.model.gameinterface.Message;
import it.polimi.dei.provafinale.carcassonne.model.gameinterface.MessageType;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;

public class TilePutListener implements ActionListener{

	private JTextField field;
	
	public TilePutListener(JTextField field){
		this.field = field;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String command = field.getText();
		if(!command.matches("[-]??[0-9]+,[-]??[0-9]+")){
			//Command inserted is malformed
		}else{
			Message msg = new Message(MessageType.PLACE, command);
			MatchController.getCurrentMatchController().sendMessage(msg);
		}
	}

}
