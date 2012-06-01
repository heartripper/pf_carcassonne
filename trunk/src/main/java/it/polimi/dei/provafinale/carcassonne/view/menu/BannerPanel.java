package it.polimi.dei.provafinale.carcassonne.view.menu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JPanel;

/**
 * The class BannerPanel extends a JPanel in order to manage the creation of the
 * banner that stays in the top part of the initial pages of the game (Home,
 * Internet Game, Local Game).
 * 
 */
public class BannerPanel extends JPanel {

	private static final int BANNERPANEL_HEIGHT = 175;
	private static final int BANNERPANEL_WIDTH = 10;

	private static final long serialVersionUID = -7347936654163580714L;

	private Image banner;
	private int width;
	private Color bannerBackground = new Color(70, 98, 151);

	/**
	 * BannerPanel constructor. Creates a new instance of class BannerPanel.
	 */
	public BannerPanel() {

		setPreferredSize(new Dimension(BANNERPANEL_WIDTH, BANNERPANEL_HEIGHT));

		String path = "src/main/resources/banner.png";
		banner = Toolkit.getDefaultToolkit().createImage(path);

		setBackground(bannerBackground);
	}

	/**
	 * Paints the logo (banner) image.
	 */
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		width = getWidth();
		g.drawImage(banner, (width - banner.getWidth(null)) / 2, 0, null);
	}

}
