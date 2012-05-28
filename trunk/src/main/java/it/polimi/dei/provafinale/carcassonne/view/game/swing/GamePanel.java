package it.polimi.dei.provafinale.carcassonne.view.game.swing;

import it.polimi.dei.provafinale.carcassonne.controller.client.FollowerPutListener;
import it.polimi.dei.provafinale.carcassonne.controller.client.PassListener;
import it.polimi.dei.provafinale.carcassonne.controller.client.TilePutListener;
import it.polimi.dei.provafinale.carcassonne.controller.client.TileRotateListener;
import it.polimi.dei.provafinale.carcassonne.model.PlayerColor;
import it.polimi.dei.provafinale.carcassonne.model.TileGrid;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.BoxLayout;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JLabel;
import java.awt.FlowLayout;

import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.border.MatteBorder;

public class GamePanel extends JPanel {

	private static final long serialVersionUID = 4955959778710528121L;

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

	public GamePanel() {
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
		rotateButton.addActionListener(new TileRotateListener());
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
		putTileButton.addActionListener(new TilePutListener(coordsField));
		coordsPanel.add(putTileButton);

		// Follower panel
		JPanel followerPanel = new JPanel();
		JLabel lblFollower = new JLabel("Follower:");
		followerPanel.add(lblFollower);
		String[] followerOptions = { "north", "east", "south", "west" };
		followerComboBox = new JComboBox(followerOptions);
		followerPanel.add(followerComboBox);
		putFollowerButton = new JButton("Put follower");
		putFollowerButton.addActionListener(new FollowerPutListener(
				followerComboBox));
		followerPanel.add(putFollowerButton);

		//Pass button
		JPanel passPanel = new JPanel();
		passButton = new JButton("Pass");
		passButton.addActionListener(new PassListener());
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

	public void createPlayerPanels(int num) {
		this.playerPanels = new PlayerPanel[num];
		for (int i = 0; i < num; i++) {
			PlayerPanel p = new PlayerPanel(PlayerColor.valueOf(i));
			players.add(p);
			playerPanels[i] = p;
		}
	}

	public PlayerPanel[] getPlayerPanels() {
		return playerPanels;
	}

	public void setTilesPanelGrid(TileGrid grid) {
		tilesPanel = new TilesPanel(grid);
		tilesArea.add(tilesPanel);
	}

	public void updateTileGridPanel() {
		tilesPanel.updateRepresentation();
		Graphics g = tilesPanel.getGraphics();
		tilesPanel.paint(g);
	}

	public void setCurrentTile(String rep) {
		currentTilePanel.setCurrentTile(rep);
	}

	public void setUIActive(boolean enabled) {
		rotateButton.setEnabled(enabled);
		coordsField.setEnabled(enabled);
		putTileButton.setEnabled(enabled);
		followerComboBox.setEnabled(enabled);
		putFollowerButton.setEnabled(enabled);
	}

	public void setMessageText(String msg) {
		messageLabel.setText(msg);
	}
}
