package it.polimi.dei.provafinale.carcassonne.logger;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * The Logger class extends a JPanel and serves to show any message on the
 * simulated console.
 * 
 */
public class Logger extends JPanel {

	private static final int FONT_SIZE = 14;

	private static final long serialVersionUID = -8795155705201105543L;

	private StringBuilder log;
	private JTextArea textArea;

	/**
	 * Class Logger constructor. Creates a new instance of class Logger.
	 */
	public Logger() {
		/* Setting layout. */
		setLayout(new BorderLayout());
		/* Setting textArea. */
		textArea = new JTextArea();
		textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, FONT_SIZE));
		JScrollPane scroll = new JScrollPane(textArea);
		add(scroll, BorderLayout.CENTER);
		/* Creating a StringBuilder. */
		log = new StringBuilder();
	}

	/**
	 * Displays a message in textArea.
	 * 
	 * @param msg
	 *            the String message we want to display on the textArea.
	 */
	public void log(String msg) {
		/* Formatting msg. */
		log.append(msg + "\n");
		/* Setting msg content on the textArea. */
		textArea.setText(log.toString());
	}
}
