package it.polimi.dei.provafinale.carcassonne.controller.client;

import it.polimi.dei.provafinale.carcassonne.controller.Message;
import it.polimi.dei.provafinale.carcassonne.controller.MessageType;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;

/**
 * Class TextualListener implements an ActionListener in order to send to the
 * controller the instructions given by the user.
 * 
 */
public class TextualListener implements ActionListener {

	private JTextField textField;

	/**
	 * TextualListener constructor. Creates a new instance of class
	 * TextualListener.
	 * 
	 * @param textField
	 *            a JTextField in which the user puts a message to send to the
	 *            controller.
	 */
	public TextualListener(JTextField textField) {
		this.textField = textField;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = textField.getText();
		Message msg;
		
		if (command.equals("rotate")) {
			msg = new Message(MessageType.ROTATE, null);
		}
		
		else if (command.equals("pass")) {
			msg = new Message(MessageType.PASS, null);
		}
		
		else if (command.matches("[NESW]")) {
			msg = new Message(MessageType.FOLLOWER, command);
		}
		
		else if (command.matches("[-]??[0-9]+,[-]??[0-9]+")) {
			msg = new Message(MessageType.PLACE, command);
		}
		/* Invalid command. */
		else {
			msg = new Message(MessageType.INVALID_MOVE, null);
		}
		textField.setText("");
		ClientController.getCurrentMatchController().sendMessage(msg);
	}

}
