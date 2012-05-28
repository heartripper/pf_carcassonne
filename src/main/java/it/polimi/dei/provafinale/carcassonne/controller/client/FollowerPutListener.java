package it.polimi.dei.provafinale.carcassonne.controller.client;

import it.polimi.dei.provafinale.carcassonne.Message;
import it.polimi.dei.provafinale.carcassonne.MessageType;
import it.polimi.dei.provafinale.carcassonne.model.SidePosition;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

public class FollowerPutListener implements ActionListener {

	private JComboBox followerPosition;

	/**
	 * FollowerPutListener constructor. Creates a new instance of class
	 * FollowerPutListener.
	 * 
	 * @param followerPosition
	 */
	public FollowerPutListener(JComboBox followerPosition) {
		this.followerPosition = followerPosition;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		int posIndex = followerPosition.getSelectedIndex();
		SidePosition pos = SidePosition.valueOf(posIndex);
		Message msg = new Message(MessageType.FOLLOWER, pos.toString());
		ClientController.getCurrentMatchController().sendMessage(msg);
	}

}
