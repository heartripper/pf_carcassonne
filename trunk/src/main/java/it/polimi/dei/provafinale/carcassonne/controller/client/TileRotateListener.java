package it.polimi.dei.provafinale.carcassonne.controller.client;

import it.polimi.dei.provafinale.carcassonne.Message;
import it.polimi.dei.provafinale.carcassonne.MessageType;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TileRotateListener implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		Message msg = new Message(MessageType.ROTATE, null);
		ClientController.getCurrentMatchController().sendMessage(msg);
	}
}
