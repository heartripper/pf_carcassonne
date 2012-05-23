package it.polimi.dei.provafinale.carcassonne.view.game;

import it.polimi.dei.provafinale.carcassonne.model.gamelogic.card.TileGrid;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.player.PlayerColor;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.BoxLayout;
import java.awt.Dimension;
import java.awt.Color;

import javax.swing.JLabel;
import java.awt.FlowLayout;

import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.border.MatteBorder;

public class GamePanel extends JPanel {

	private static final long serialVersionUID = 4955959778710528121L;
	
	private JPanel tilesArea;
	private PlayerPanel[] playerPanels;
	private JPanel players;

	public GamePanel() {
		setLayout(new BorderLayout(0, 0));

		tilesArea = new JPanel();
		add(tilesArea, BorderLayout.CENTER);

		// Notification panel
		JPanel notificationPanel = new JPanel();
		notificationPanel.setPreferredSize(new Dimension(195, 10));
		notificationPanel.setBorder(new MatteBorder(0, 1, 0, 0,
				(Color) Color.GRAY));
		add(notificationPanel, BorderLayout.EAST);
		notificationPanel.setLayout(new BoxLayout(notificationPanel,
				BoxLayout.Y_AXIS));

		//Current Tile panel
		JPanel tilePanel = new JPanel();

		//Rotate panel
		JPanel rotatePanel = new JPanel();
		rotatePanel.setLayout(new BoxLayout(rotatePanel, BoxLayout.X_AXIS));
		JButton btnRotate = new JButton("Rotate");
		rotatePanel.add(btnRotate);

		//Coords panel
		JPanel coordsPanel = new JPanel();
		coordsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		JLabel lblInsertCoordinatesxy = new JLabel("Insert coordinates (x,y):");
		coordsPanel.add(lblInsertCoordinatesxy);
		JTextField coordsField = new JTextField();
		coordsField.setColumns(10);
		coordsPanel.add(coordsField);
		JButton btnPutTile = new JButton("Put tile");
		coordsPanel.add(btnPutTile);
		
		//Follower panel
		JPanel followerPanel = new JPanel();
		JLabel lblFollower = new JLabel("Follower:");
		followerPanel.add(lblFollower);
		String[] followerOptions = { "none", "north", "east", "south", "west" };
		JComboBox comboBox = new JComboBox(followerOptions);
		followerPanel.add(comboBox);
		JButton btnPutFollower = new JButton("Put follower");
		followerPanel.add(btnPutFollower);

		notificationPanel.add(tilePanel);
		notificationPanel.add(rotatePanel);
		notificationPanel.add(coordsPanel);
		notificationPanel.add(followerPanel);

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
		TilesPanel panel = new TilesPanel(grid);
		tilesArea.add(panel);
	}
}
