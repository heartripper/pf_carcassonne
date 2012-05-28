package it.polimi.dei.provafinale.carcassonne.logger;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Logger extends JPanel{

	private static final long serialVersionUID = -8795155705201105543L;
	private StringBuilder log;
	private JTextArea textArea;

	public Logger() {
		setLayout(new BorderLayout());
		textArea = new JTextArea();
		textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
		JScrollPane scroll = new JScrollPane(textArea);
		add(scroll, BorderLayout.CENTER);
		log = new StringBuilder();
	}
	
	public void log(String msg){
		log.append(msg + "\n");
		textArea.setText(log.toString());
	}
}
