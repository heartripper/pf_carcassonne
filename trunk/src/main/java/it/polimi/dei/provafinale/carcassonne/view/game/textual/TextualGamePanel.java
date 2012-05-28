package it.polimi.dei.provafinale.carcassonne.view.game.textual;

import it.polimi.dei.provafinale.carcassonne.controller.client.TextualListener;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JButton;

public class TextualGamePanel extends JPanel {

	private static final long serialVersionUID = 4426718873254694108L;
	private JTextField textField;
	private JTextArea textArea;
	private StringBuilder text = new StringBuilder();

	public TextualGamePanel() {

		setLayout(new BorderLayout(0, 0));

		textArea = new JTextArea();
		textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 15));
		JScrollPane scrollPane = new JScrollPane(textArea);
		textArea.setEditable(false);

		add(scrollPane, BorderLayout.CENTER);

		JPanel panel = new JPanel();
		add(panel, BorderLayout.SOUTH);

		textField = new JTextField();
		panel.add(textField);
		textField.setColumns(10);

		JButton btnSend = new JButton("Send!");
		panel.add(btnSend);
		btnSend.addActionListener(new TextualListener(textField));
	}

	public void print(String newText) {
		text.append(newText + "\n");
		textArea.setText(text.toString());
	}

}
