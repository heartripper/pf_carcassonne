package it.polimi.dei.provafinale.carcassonne.controller.client;

import it.polimi.dei.provafinale.carcassonne.model.gameinterface.Message;
import it.polimi.dei.provafinale.carcassonne.model.gameinterface.MessageType;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.card.SidePosition;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

public class FollowerPutListener implements ActionListener{

	private JComboBox followerPosition;
	
	public FollowerPutListener(JComboBox followerPosition){
		this.followerPosition = followerPosition;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		int posIndex = followerPosition.getSelectedIndex();
		SidePosition pos = SidePosition.valueOf(posIndex);
		Message msg = new Message(MessageType.FOLLOWER, pos.toString());
		MatchController.getCurrentMatchController().sendMessage(msg);
	}
}
