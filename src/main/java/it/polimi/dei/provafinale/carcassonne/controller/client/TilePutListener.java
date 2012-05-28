package it.polimi.dei.provafinale.carcassonne.controller.client;

import it.polimi.dei.provafinale.carcassonne.Message;
import it.polimi.dei.provafinale.carcassonne.MessageType;

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
		Message msg = new Message(MessageType.PLACE, command);
		ClientController.getCurrentMatchController().sendMessage(msg);
	}

}
