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

/**
 * The class TextualGamePanel extends a GamePanel in order to represent the view
 * of the game that a player has: a screen that contains the tiles already put,
 * the notifications and the options in the center of the page, a line where to
 * insert choices in the bottom part of the page.
 * 
 */
public class TextualGamePanel extends GamePanel {

	private static final long serialVersionUID = -8074082087694368365L;

	private PlayerColor clientColor;
	private PlayerColor currentPlayerColor;
	private JTextField textField;
	private JTextArea textArea;
	private StringBuilder text = new StringBuilder();

	private TileRepresentationGrid tileRepGrid;
	private TileGridRepresenter representer;

	/**
	 * TextualGamePanel constructor. Creates a new instance of class
	 * TextualGamePanel.
	 */
	public TextualGamePanel() {
		super();

		/* Initialize grid */
		tileRepGrid = new TileRepresentationGrid();
		representer = new TileGridRepresenter(tileRepGrid);
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
	public void initialize(String initMsg) {
		String[] split = initMsg.split(",");

		/* Setup initial tile */
		String initialTile = split[0].trim();
		tileRepGrid.execUpdate(initialTile + ", 0, 0");

		/*
		 * Setting the color of the client that uses the current instance of
		 * TextualGamePanel (clientColor could be null if case the client
		 * manages all the colors).
		 */
		String color = split[2].trim();
		clientColor = (color.equals("null") ? null : PlayerColor.valueOf(color));

		/*
		 * Printing on the textArea a String that announces the start of a
		 * match.
		 */
		int playerNumber = Integer.parseInt(split[3].trim());

		/* Print introduction */
		String notification = String.format("Match starts with %s players.\n",
				playerNumber);
		if (clientColor != null) {
			notification += String
					.format("You are player %s.\n\n", clientColor);
		}
		printMsg(notification);
		printMsg(representer.getRepresentation());
	}

	/* Prints the current grid on the textArea. */
	@Override
	public void updateGridRepresentation(String s) {
		tileRepGrid.execUpdate(s);
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

		String msg;
		if (clientColor != null) {
			msg = "Your tile:\n";
		} else {
			msg = (String.format("Player %s tile:\n",
					currentPlayerColor.getFullName()));
		}
		printMsg(msg + tileRepresentation);
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