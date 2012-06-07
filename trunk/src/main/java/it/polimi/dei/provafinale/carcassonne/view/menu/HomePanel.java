package it.polimi.dei.provafinale.carcassonne.view.menu;

import it.polimi.dei.provafinale.carcassonne.controller.client.MenuPanelSwitcherListener;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JButton;
import javax.swing.BoxLayout;
import javax.swing.Box;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.Component;

/**
 * The class HomePanel extends a JPanel in order to create the home page of the
 * game.
 * 
 */
public class HomePanel extends JPanel {

	private static final int PIXEL_SPACING = 20;

	private static final long serialVersionUID = 8988417171482193429L;

	private BufferedImage background;

	/**
	 * HomePanel constructor. Creates a new instance of class HomePanel.
	 * 
	 * @param background
	 *            an Image we want to set as background of the panel.
	 */
	public HomePanel(BufferedImage background) {

		this.background = background;

		BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
		setLayout(layout);

		Component verticalGlue01 = Box.createVerticalGlue();

		/* Adding new local game panel. */
		JPanel newLocalGamePanel = new JPanel();
		newLocalGamePanel.setBackground(new Color(0, 0, 0, 0));
		newLocalGamePanel.setLayout(new BoxLayout(newLocalGamePanel,
				BoxLayout.X_AXIS));
		/* Adding new local game button to new local game panel. */
		JButton btnNewLocalGame = new JButton("New Local Game");
		newLocalGamePanel.add(btnNewLocalGame);
		ActionListener local = new MenuPanelSwitcherListener(MenuPanel.LOCALGAMEPANEL);
		btnNewLocalGame.addActionListener(local);

		Component verticalStrut = Box.createVerticalStrut(PIXEL_SPACING);

		/* Adding new internet game panel. */
		JPanel newInternetGamePanel = new JPanel();
		newInternetGamePanel.setBackground(new Color(0, 0, 0, 0));
		newInternetGamePanel.setLayout(new BoxLayout(newInternetGamePanel,
				BoxLayout.X_AXIS));
		/* Adding new internet game button to new internet game panel. */
		JButton btnNewInternetGame = new JButton("New Internet Game");
		newInternetGamePanel.add(btnNewInternetGame);
		ActionListener internet = new MenuPanelSwitcherListener(
				MenuPanel.INTERNETGAMEPANEL);
		btnNewInternetGame.addActionListener(internet);

		Component verticalGlue02 = Box.createVerticalGlue();

		/* Add components to panel. */
		add(verticalGlue01);
		add(newLocalGamePanel);
		add(verticalStrut);
		add(newInternetGamePanel);
		add(verticalGlue02);

		repaint();
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
