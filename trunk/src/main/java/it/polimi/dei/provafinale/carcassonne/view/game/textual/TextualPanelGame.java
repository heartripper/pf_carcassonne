package it.polimi.dei.provafinale.carcassonne.view.game.textual;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.TextArea;

import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JButton;

public class TextualPanelGame extends JPanel {
	private JTextField textField;
	private JTextArea textArea;
	private StringBuilder text = new StringBuilder();

	public TextualPanelGame() {

		setLayout(new BorderLayout(0, 0));

		textArea = new JTextArea();
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
	}

	public void print(String newText) {
		text.append(newText + "\n");
		textArea.setText(text.toString());
	}

}
