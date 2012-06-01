package it.polimi.dei.provafinale.carcassonne.view.menu;

import it.polimi.dei.provafinale.carcassonne.controller.client.MenuPanelSwitcher;
import it.polimi.dei.provafinale.carcassonne.controller.client.StartLocalGame;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.BoxLayout;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JButton;

/**
 * The class LocalGamePanel extends a JPanel in order to create the local
 * menu of the game.
 * 
 */
public class LocalGamePanel extends JPanel {

	private static final int PIXEL_SPACING = 20;

	private static final long serialVersionUID = -6424162656647447061L;

	private BufferedImage background;

	/**
	 * LocalGamePanel constructor. Creates a new instance of class
	 * LocalGamePanel.
	 * 
	 * @param background
	 *            an Image we want to set as background of the panel.
	 */
	public LocalGamePanel(BufferedImage background) {
		/* Setting background attribute. */
		this.background = background;

		/* Setting layout. */
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		/* Layout option. */
		Component verticalGlue01 = Box.createVerticalGlue();
		add(verticalGlue01);

		/* Creating title panel. */
		JPanel titlePanel = new JPanel();
		titlePanel.setBackground(new Color(0, 0, 0, 0));
		titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
		add(titlePanel);
		/* Adding new local game label to title panel. */
		JLabel lblNewLocalGame = new JLabel("NEW LOCAL GAME");
		titlePanel.add(lblNewLocalGame);

		/* Layout option. */
		Component verticalStrut01 = Box.createVerticalStrut(PIXEL_SPACING);
		add(verticalStrut01);

		/* Creating select players number panel. */
		JPanel selectPlayersNumberPanel = new JPanel();
		selectPlayersNumberPanel.setBackground(new Color(0, 0, 0, 0));
		selectPlayersNumberPanel.setLayout(new FlowLayout());
		add(selectPlayersNumberPanel);
		/* Layout option. */
		Component horizontalGlue01 = Box.createHorizontalGlue();
		selectPlayersNumberPanel.add(horizontalGlue01);
		/* Adding label insert players number to select players number panel. */
		JLabel lblInsertPlayersNumber = new JLabel("Insert players number:");
		selectPlayersNumberPanel.add(lblInsertPlayersNumber);
		/*
		 * Adding combo box player to select players number panel to select the
		 * number of players that will play the game.
		 */
		String[] data = { "2", "3", "4", "5" };
		JComboBox playerComboBox = new JComboBox(data);
		selectPlayersNumberPanel.add(playerComboBox);
		/* Layout option. */
		Component horizontalGlue02 = Box.createHorizontalGlue();
		selectPlayersNumberPanel.add(horizontalGlue02);

		/* Layout option. */
		Component verticalStrut = Box.createVerticalStrut(PIXEL_SPACING);
		add(verticalStrut);

		/* Creating gui panel. */
		JPanel guiPanel = new JPanel();
		guiPanel.setBackground(new Color(0, 0, 0, 0));
		add(guiPanel);
		/* Adding label select gui to gui panel. */
		JLabel lblSelectGui = new JLabel("Select GUI:");
		guiPanel.add(lblSelectGui);
		/* Adding combobox gui to gui panel. */
		String[] gui = { "Swing", "Textual" };
		JComboBox guiComboBox = new JComboBox(gui);
		guiPanel.add(guiComboBox);

		/* Creating play game panel. */
		JPanel playGamePanel = new JPanel();
		playGamePanel.setBackground(new Color(0, 0, 0, 0));
		playGamePanel.setLayout(new BoxLayout(playGamePanel, BoxLayout.X_AXIS));
		add(playGamePanel);
		/* Layout option. */
		Component horizontalGlue03 = Box.createHorizontalGlue();
		playGamePanel.add(horizontalGlue03);
		/* Adding button play game to play game panel. */
		JButton btnPlayGame = new JButton("Play Game!");
		btnPlayGame.addActionListener(new StartLocalGame(playerComboBox,
				guiComboBox));
		playGamePanel.add(btnPlayGame);
		/* Layout option. */
		Component horizontalGlue = Box.createHorizontalGlue();
		playGamePanel.add(horizontalGlue);

		/* Layout option. */
		Component verticalGlue02 = Box.createVerticalGlue();
		add(verticalGlue02);

		/* Creating back to home panel. */
		JPanel backToHomePanel = new JPanel();
		backToHomePanel.setBackground(new Color(0, 0, 0, 0));
		backToHomePanel.setLayout(new BoxLayout(backToHomePanel,
				BoxLayout.X_AXIS));
		add(backToHomePanel);
		/* Adding button back to home to home panel. */
		JButton btnBackToHome = new JButton("Back to home");
		backToHomePanel.add(btnBackToHome);
		ActionListener home = new MenuPanelSwitcher(MenuPanel.HOMEPANEL);
		btnBackToHome.addActionListener(home);
	}

	/**
	 * Paints the background image on the panel.
	 */
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		int x = (getWidth() - background.getWidth()) / 2;
		int y = (getHeight() -background.getHeight());
		g.drawImage(background, x, y, null);
	}

}
