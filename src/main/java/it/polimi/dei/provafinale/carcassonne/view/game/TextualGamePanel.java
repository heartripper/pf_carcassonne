package it.polimi.dei.provafinale.carcassonne.view.game;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import it.polimi.dei.provafinale.carcassonne.PlayerColor;
import it.polimi.dei.provafinale.carcassonne.controller.client.TextualListener;
import it.polimi.dei.provafinale.carcassonne.model.Tile;
import it.polimi.dei.provafinale.carcassonne.model.TileGrid;

public class TextualGamePanel extends GamePanel {

	private static final long serialVersionUID = -8074082087694368365L;

	private TileGridRepresenter representer;
	private PlayerColor clientColor;
	private PlayerColor currentPlayerColor;
	private JTextField textField;
	private JTextArea textArea;
	private StringBuilder text = new StringBuilder();

	/**
	 * TextualGamePanel constructor. Creates a new instance of class
	 * TextualGamePanel.
	 */
	public TextualGamePanel() {
		/* Setting the class layout. */
		setLayout(new BorderLayout(0, 0));
		/* Initializing the area where to put tiles and notifications. */
		textArea = new JTextArea();
		textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 15));
		textArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(textArea);
		add(scrollPane, BorderLayout.CENTER);
		/* Creating a panel to put in the bottom part of the screen. */
		JPanel panel = new JPanel();
		add(panel, BorderLayout.SOUTH);
		/* Setting in the bottom panel a TextField where to insert commands. */
		textField = new JTextField();
		textField.setColumns(10);
		panel.add(textField);
		/*
		 * Setting in the bottom panel a JButton to confirm the inserted
		 * commands.
		 */
		JButton btnSend = new JButton("Send!");
		btnSend.addActionListener(new TextualListener(textField));
		panel.add(btnSend);
	}

	/* Prints a message in textArea. */
	private void printMsg(String newText) {
		text.append(newText + "\n");
		textArea.setText(text.toString());
	}

	/* Implementation of ViewInterface methods. */

	@Override
	public void initialize(TileGrid grid, int numPlayers,
			PlayerColor clientColor) {
		/* Setting the grid. */
		this.representer = new TileGridRepresenter(grid);
		/*
		 * Setting the color of the client that uses the current instance of
		 * TextualGamePanel (clientColor could be null if case the client
		 * manages all the colors).
		 */
		this.clientColor = clientColor;
		/*
		 * Printing on the textArea a String that announces the start of a
		 * match.
		 */
		String notification = String.format("Match starts with %s players.\n",
				numPlayers);
		if (clientColor != null) {
			notification += String.format("You are player %s.\n", clientColor);
		}
		printMsg(notification);
	}

	/* Prints the current grid on the textArea. */
	@Override
	public void updateGridRepresentation() {
		printMsg(representer.getRepresentation());
	}

	/* Prints the representation of the current tile on the textArea. */
	@Override
	public void updateCurrentTile(String rep) {
		/* Convert the String representation of a tile into the Tile one. */
		Tile tile = new Tile(rep);
		/* Converts the Tile into a printable String. */
		String tileRepresentation = TileGridRepresenter
				.getTileRepresentation(tile);
		printMsg("Your tile:\n" + tileRepresentation);
	}

	/* Gives the current scores of the players that are playing the match. */
	@Override
	public void updateScore(String msg) {
		printMsg("Players scores: " + msg);
	}

	@Override
	public void setUIActive(boolean enabled) {
		/* If the current player is active in the match... */
		if (enabled) {
			String msg = "";
			/*
			 * If there is no a setted clientColor, adds to the message to be
			 * send the client which the message is destinated to.
			 */
			if (clientColor == null) {
				msg += String.format("(%s) ", currentPlayerColor);
			}
			msg += "Enter command:";
			/* Sends the message. */
			printMsg(msg);
		}
	}

	/* Gives informations about who is the turn of. */
	@Override
	public void setCurrentPlayer(PlayerColor color) {
		currentPlayerColor = color;
		/* Case is the turn of this client. */
		if (clientColor != null && clientColor.equals(color)) {
			printMsg("It's your turn");
		}
		/* Case is the turn of another client. */
		else {
			printMsg(String.format("It's player %s turn", color.getFullName()));
		}
	}

	/* Prints notifications on the textArea. */
	@Override
	public void showNotify(String msg) {
		printMsg(msg);
	}
}