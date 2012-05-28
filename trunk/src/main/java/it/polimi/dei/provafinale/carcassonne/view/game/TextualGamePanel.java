package it.polimi.dei.provafinale.carcassonne.view.game;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import it.polimi.dei.provafinale.carcassonne.controller.client.TextualListener;
import it.polimi.dei.provafinale.carcassonne.model.PlayerColor;
import it.polimi.dei.provafinale.carcassonne.model.Tile;
import it.polimi.dei.provafinale.carcassonne.model.TileGrid;

public class TextualGamePanel extends GamePanel{

	private static final long serialVersionUID = -8074082087694368365L;

	private TileGridRepresenter representer;
	private PlayerColor clientColor;
	private PlayerColor currentPlayerColor;
	
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

	private void printMsg(String newText) {
		text.append(newText + "\n");
		textArea.setText(text.toString());
	}
	
	//ViewInterface methods
	@Override
	public void initialize(TileGrid grid, int numPlayers,
			PlayerColor clientColor) {
		
		this.representer = new TileGridRepresenter(grid);
		this.clientColor = clientColor;
		
		String notification = String.format("Match starts with %s players.\n",
				numPlayers);
		if (clientColor != null) {
			notification += String.format("You are player %s.\n", clientColor);
		}
		
		printMsg(notification);
	}

	@Override
	public void updateGridRepresentation() {
		printMsg(representer.getRepresentation());
	}

	@Override
	public void updateCurrentTile(String rep) {
		Tile tile = new Tile(rep);
		String tileRepresentation = TileGridRepresenter
				.getTileRepresentation(tile);
		printMsg("Your tile:\n" + tileRepresentation);
	}

	@Override
	public void updateScore(String msg) {
		printMsg("Players scores: " + msg);
	}

	@Override
	public void setUIActive(boolean enabled) {
		if(enabled){
			String msg = "";
			if(clientColor == null){
				msg += String.format("(%s) ", currentPlayerColor);
			}
			msg += "Enter command:";
			printMsg(msg);
		}
	}

	@Override
	public void setCurrentPlayer(PlayerColor color) {
		currentPlayerColor = color;
		
		if (clientColor != null && clientColor.equals(color)) {
			printMsg("It's your turn");
		} else {
			printMsg(String.format("It's player %s turn",
					color.getFullName()));
		}
	}

	@Override
	public void showNotify(String msg) {
		printMsg(msg);
	}	
}