package it.polimi.dei.provafinale.carcassonne.view.game;

import it.polimi.dei.provafinale.carcassonne.MessageType;
import it.polimi.dei.provafinale.carcassonne.PlayerColor;
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

	private JPanel tilesArea;
	private TilesPanel tilesPanel;
	private JPanel players;
	private PlayerPanel[] playerPanels;

	private CurrentTilePanel currentTilePanel;
	private JLabel messageLabel;

	private JButton rotateButton;
	private JTextField coordsField;
	private JButton putTileButton;
	private JComboBox followerComboBox;
	private JButton putFollowerButton;
	private JButton passButton;

	public SwingGamePanel() {
		setLayout(new BorderLayout(0, 0));

		tilesArea = new JPanel();
		tilesArea.setLayout(new BorderLayout());
		add(tilesArea, BorderLayout.CENTER);

		// Notification panel
		JPanel actionsPanel = new JPanel();
		actionsPanel.setPreferredSize(new Dimension(195, 10));
		actionsPanel.setBorder(new MatteBorder(0, 1, 0, 0, (Color) Color.GRAY));
		add(actionsPanel, BorderLayout.EAST);
		actionsPanel.setLayout(new BoxLayout(actionsPanel, BoxLayout.Y_AXIS));

		// Current Tile panel
		JPanel noticationsPanel = new JPanel();
		noticationsPanel.setLayout(new FlowLayout());
		currentTilePanel = new CurrentTilePanel();
		noticationsPanel.add(currentTilePanel, BorderLayout.CENTER);
		messageLabel = new JLabel();
		noticationsPanel.add(messageLabel, BorderLayout.SOUTH);

		// Rotate panel
		JPanel rotatePanel = new JPanel();
		rotatePanel.setLayout(new BoxLayout(rotatePanel, BoxLayout.X_AXIS));
		rotateButton = new JButton("Rotate");
		ActionListener rotate = new MessageSender(MessageType.ROTATE, null);
		rotateButton.addActionListener(rotate);
		rotatePanel.add(rotateButton);

		// Coords panel
		JPanel coordsPanel = new JPanel();
		coordsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		JLabel lblInsertCoordinatesxy = new JLabel("Insert coordinates (x,y):");
		coordsPanel.add(lblInsertCoordinatesxy);
		coordsField = new JTextField();
		coordsField.setColumns(10);
		coordsPanel.add(coordsField);
		putTileButton = new JButton("Put tile");
		ActionListener place = new MessageSender(MessageType.PLACE, coordsField);
		putTileButton.addActionListener(place);
		coordsPanel.add(putTileButton);

		// Follower panel
		JPanel followerPanel = new JPanel();
		JLabel lblFollower = new JLabel("Follower:");
		followerPanel.add(lblFollower);
		String[] followerOptions = { "north", "east", "south", "west" };
		followerComboBox = new JComboBox(followerOptions);
		followerPanel.add(followerComboBox);
		putFollowerButton = new JButton("Put follower");
		ActionListener follower = new MessageSender(MessageType.FOLLOWER,
				followerComboBox);
		putFollowerButton.addActionListener(follower);
		followerPanel.add(putFollowerButton);

		// Pass button
		JPanel passPanel = new JPanel();
		passButton = new JButton("Pass");
		ActionListener pass = new MessageSender(MessageType.PASS, null);
		passButton.addActionListener(pass);
		passPanel.add(passButton);

		actionsPanel.add(noticationsPanel);
		actionsPanel.add(rotatePanel);
		actionsPanel.add(coordsPanel);
		actionsPanel.add(followerPanel);
		actionsPanel.add(passPanel);

		JPanel bottomPanel = new JPanel();
		bottomPanel.setBorder(new MatteBorder(1, 0, 0, 0, (Color) Color.GRAY));
		add(bottomPanel, BorderLayout.SOUTH);
		bottomPanel.setLayout(new BorderLayout(0, 0));

		// Options panel
		JPanel optionsPanel = new JPanel();
		optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
		bottomPanel.add(optionsPanel, BorderLayout.EAST);
		JPanel pnlBackToHome = new JPanel();
		JButton btnBackToHome = new JButton("Back to Home");
		pnlBackToHome.add(btnBackToHome);
		JPanel pnlScreenshot = new JPanel();
		JButton btnTakeAScreenshot = new JButton("Take a ScreenShot");
		pnlScreenshot.add(btnTakeAScreenshot);
		optionsPanel.add(pnlBackToHome);
		optionsPanel.add(pnlScreenshot);

		players = new JPanel();
		bottomPanel.add(players, BorderLayout.WEST);
	}

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
		}

		/* Disable UI */
		setUIActive(false);

		/* Update representation of tiles */
		updateGridRepresentation();
	}

	@Override
	public void updateGridRepresentation() {
		tilesPanel.updateRepresentation();
		Graphics g = tilesPanel.getGraphics();
		tilesPanel.paint(g);
	}

	@Override
	public void updateCurrentTile(String rep) {
		currentTilePanel.setCurrentTile(rep);
	}

	@Override
	public void updateScore(String msg) {
		String[] scores = msg.split(",");
		for (String s : scores) {
			String[] split = s.split("=");
			PlayerColor color = PlayerColor.valueOf(split[0].trim());
			int colorIndex = PlayerColor.indexOf(color);
			int score = Integer.parseInt(split[1].trim());
			playerPanels[colorIndex].setScore(score);
		}
	}

	@Override
	public void setCurrentPlayer(PlayerColor color) {
		int selectedIndex = PlayerColor.indexOf(color);
		for (int i = 0; i < playerPanels.length; i++) {
			playerPanels[i].setActive(i == selectedIndex);
		}
	}

	@Override
	public void showNotify(String msg) {
		messageLabel.setText(msg);
	}

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