package it.polimi.dei.provafinale.carcassonne.controller.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextField;

import it.polimi.dei.provafinale.carcassonne.Message;
import it.polimi.dei.provafinale.carcassonne.MessageType;
import it.polimi.dei.provafinale.carcassonne.model.SidePosition;

public class MessageSender implements ActionListener {

	private MessageType type;
	private JComponent payloadSource;

	public MessageSender(MessageType type, JComponent payloadSource) {
		this.type = type;
		this.payloadSource = payloadSource;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String payload;
		MessageType newType;

		if(payloadSource == null){
			payload = null;
		} else if (payloadSource instanceof JTextField) {
			JTextField source = (JTextField) payloadSource;
			payload = source.getText();
		} else if (payloadSource instanceof JComboBox) {
			JComboBox source = (JComboBox) payloadSource;
			int selectedIndex = source.getSelectedIndex();
			payload = SidePosition.valueOf(selectedIndex).toString();
		} else {
			throw new RuntimeException("Can't handle given JComponent");
		}

		if (this.type == MessageType.PLACE
				&& !payload.matches("[-]??[0-9]+,[-]??[0-9]+")) {
			newType = MessageType.INVALID_MOVE;
			payload = null;
		} else {
			newType = type;
		}
		
		Message msg = new Message(newType, payload);
		ClientController.getCurrentMatchController().sendMessage(msg);
	}
}
