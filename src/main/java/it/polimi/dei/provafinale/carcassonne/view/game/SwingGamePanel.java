package it.polimi.dei.provafinale.carcassonne.view.game;

import it.polimi.dei.provafinale.carcassonne.PlayerColor;
import it.polimi.dei.provafinale.carcassonne.controller.MessageType;
import it.polimi.dei.provafinale.carcassonne.controller.client.MessageSender;
import it.polimi.dei.provafinale.carcassonne.model.TileGrid;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.MatteBorder;

public class SwingGamePanel extends GamePanel {

	private static final long serialVersionUID = -5552501660516939765L;

	/* Tiles area. */
	private JPanel tilesArea;
	private TilesPanel tilesPanel;
	/* Overview of players area. */
	private JPanel players;
	private PlayerPanel[] playerPanels;
	/* Notifications area. */
	private CurrentTilePanel currentTilePanel;
	private JLabel messageLabel;
	private JButton rotateButton;
	private JTextField coordsField;
	private JButton putTileButton;
	private JComboBox followerComboBox;
	private JButton putFollowerButton;
	private JButton passButton;

	/* Player playing on this client */
	private PlayerColor clientColor = null;

	/**
	 * SwingGamePanel constructor. Creates a new instance of class
	 * SwingGamePanel.
	 */
	public SwingGamePanel() {

		/* Setting the class layout. */
		setLayout(new BorderLayout(0, 0));

		/* Initializing the area where to put tiles. */
		tilesArea = new JPanel();
		tilesArea.setLayout(new BorderLayout());
		add(tilesArea, BorderLayout.CENTER);

		/*
		 * Initializing the panel where to put notifications (e.g. current card,
		 * current player turn, rotation option, field to put coordinates with
		 * the put button, the option to add a follower with the put option and
		 * the option to conclude the turn).
		 */
		JPanel actionsPanel = new JPanel();
		actionsPanel.setPreferredSize(new Dimension(195, 10));
		actionsPanel.setBorder(new MatteBorder(0, 1, 0, 0, (Color) Color.GRAY));
		actionsPanel.setLayout(new BoxLayout(actionsPanel, BoxLayout.Y_AXIS));
		add(actionsPanel, BorderLayout.EAST);

		/* Notifications panel. */

		JPanel noticationsPanel = new JPanel();
		noticationsPanel.setLayout(new FlowLayout());
		/* Tile panel. */
		currentTilePanel = new CurrentTilePanel();
		noticationsPanel.add(currentTilePanel, BorderLayout.CENTER);
		/* Message label. */
		messageLabel = new JLabel();
		noticationsPanel.add(messageLabel, BorderLayout.SOUTH);

		/* Rotate panel. */

		JPanel rotatePanel = new JPanel();
		rotatePanel.setLayout(new BoxLayout(rotatePanel, BoxLayout.X_AXIS));
		/* Rotate button. */
		rotateButton = new JButton("Rotate");
		ActionListener rotate = new MessageSender(MessageType.ROTATE, null);
		rotateButton.addActionListener(rotate);
		rotatePanel.add(rotateButton);

		/* Coordinate panel. */

		JPanel coordsPanel = new JPanel();
		coordsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		/* Insert coordinate label. */
		JLabel lblInsertCoordinatesxy = new JLabel("Insert coordinates (x,y):");
		coordsPanel.add(lblInsertCoordinatesxy);
		/* Coordinates text field. */
		coordsField = new JTextField();
		coordsField.setColumns(10);
		coordsPanel.add(coordsField);
		/* Put button. */
		putTileButton = new JButton("Put tile");
		ActionListener place = new MessageSender(MessageType.PLACE, coordsField);
		putTileButton.addActionListener(place);
		coordsPanel.add(putTileButton);

		/* Follower panel. */

		JPanel followerPanel = new JPanel();
		/* Follower label. */
		JLabel lblFollower = new JLabel("Follower:");
		followerPanel.add(lblFollower);
		/* Follower options combo box. */
		String[] followerOptions = { "north", "east", "south", "west" };
		followerComboBox = new JComboBox(followerOptions);
		followerPanel.add(followerComboBox);
		/* Put follower button. */
		putFollowerButton = new JButton("Put follower");
		ActionListener follower = new MessageSender(MessageType.FOLLOWER,
				followerComboBox);
		putFollowerButton.addActionListener(follower);
		followerPanel.add(putFollowerButton);

		/* Pass panel. */
		JPanel passPanel = new JPanel();
		/* Pass button. */
		passButton = new JButton("Pass");
		ActionListener pass = new MessageSender(MessageType.PASS, null);
		passButton.addActionListener(pass);
		passPanel.add(passButton);

		/* Add the panels to the main panel. */
		actionsPanel.add(noticationsPanel);
		actionsPanel.add(rotatePanel);
		actionsPanel.add(coordsPanel);
		actionsPanel.add(followerPanel);
		actionsPanel.add(passPanel);

		/* Player (bottom) panel. */

		JPanel bottomPanel = new JPanel();
		bottomPanel.setBorder(new MatteBorder(1, 0, 0, 0, (Color) Color.GRAY));
		bottomPanel.setLayout(new BorderLayout(0, 0));
		add(bottomPanel, BorderLayout.SOUTH);
		/* Options Panel */
		JPanel optionsPanel = new JPanel();
		optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
		bottomPanel.add(optionsPanel, BorderLayout.EAST);
		/* Back to home panel (to put into options panel). */
		JPanel pnlBackToHome = new JPanel();
		optionsPanel.add(pnlBackToHome);
		/* Back to home button. */
		JButton btnBackToHome = new JButton("Back to Home");
		pnlBackToHome.add(btnBackToHome);
		/* Screenshot panel (to put into options panel). */
		JPanel pnlScreenshot = new JPanel();
		optionsPanel.add(pnlScreenshot);
		/* Take a screenshot button. */
		JButton btnTakeAScreenshot = new JButton("Take a ScreenShot");
		pnlScreenshot.add(btnTakeAScreenshot);
		/* Players panel (to put into options panel). */
		players = new JPanel();
		bottomPanel.add(players, BorderLayout.WEST);
	}

	/* Implementation of ViewInterface methods. */

	@Override
	public void initialize(TileGrid grid, int numPlayers,
			PlayerColor clientColor) {
		/* Create tilesPanel */
		tilesPanel = new TilesPanel(grid);
		tilesArea.add(tilesPanel, BorderLayout.CENTER);
		/* Setup players */
		playerPanels = new PlayerPanel[numPlayers];
		for (int i = 0; i < numPlayers; i++) {
			PlayerPanel p = new PlayerPanel(PlayerColor.valueOf(i));
			players.add(p);
			playerPanels[i] = p;
		}
		/* Setup client player color */
		if (clientColor != null) {
			int index = PlayerColor.indexOf(clientColor);
			playerPanels[index].setClientPlayer();
			this.clientColor = clientColor;
		}
		/* Disable UI */
		setUIActive(false);
		/* Update representation of tiles */
		updateGridRepresentation();
	}

	/* Prints the current grid on the special area. */
	@Override
	public void updateGridRepresentation() {
		tilesPanel.updateRepresentation();
		Graphics g = tilesPanel.getGraphics();
		tilesPanel.paint(g);
	}

	/* Prints the representation of the current tile on the special area. */
	@Override
	public void updateCurrentTile(String rep) {
		currentTilePanel.setCurrentTile(rep);
	}

	/* Gives the current scores of the players that are playing the match. */
	@Override
	public void updateScore(String msg) {
		/*
		 * Analyzing the String msg, that contains the scores of all the
		 * players.
		 */
		String[] scores = msg.split(",");
		for (String s : scores) {
			String[] split = s.split("=");
			/* Score assignment. */
			PlayerColor color = PlayerColor.valueOf(split[0].trim());
			int colorIndex = PlayerColor.indexOf(color);
			int score = Integer.parseInt(split[1].trim());
			playerPanels[colorIndex].setScore(score);
		}
	}

	/* Sets informations about who is the turn of. */
	@Override
	public void setCurrentPlayer(PlayerColor color) {
		int selectedIndex = PlayerColor.indexOf(color);
		for (int i = 0; i < playerPanels.length; i++) {
			playerPanels[i].setActive(i == selectedIndex);
		}

		/*Show a string to indicate current player*/
		String text;
		if (clientColor != null && clientColor.equals(color)) {
			text = "It's your turn";
		} else {
			text = String.format("It's player %s turn", color.getFullName());
		}
		showNotify(text);
		
		/*Reset fields*/
		coordsField.setText("");
		followerComboBox.setSelectedIndex(0);
	}

	/* Prints notifications on the textArea. */
	@Override
	public void showNotify(String msg) {
		messageLabel.setText(msg);
	}

	/* Activates and disactivates the grapghic interface. */
	@Override
	public void setUIActive(boolean enabled) {
		rotateButton.setEnabled(enabled);
		coordsField.setEnabled(enabled);
		putTileButton.setEnabled(enabled);
		followerComboBox.setEnabled(enabled);
		putFollowerButton.setEnabled(enabled);
		passButton.setEnabled(enabled);
	}

}