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

	private static final long serialVersionUID = -7347936654163580714L;

	private Image banner;
	private int width;

	/**
	 * BannerPanel constructor. Creates a new instance of class BannerPanel.
	 */
	public BannerPanel() {
		/* Dimension. */
		setPreferredSize(new Dimension(10, 175));
		/* Logo. */
		String path = "src/main/resources/banner.png";
		banner = Toolkit.getDefaultToolkit().createImage(path);
		/* Background. */
		setBackground(new Color(70, 98, 151));
	}

	/**
	 * Paints the logo (banner) image.
	 */
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		width = getWidth();
		g.drawImage(banner, (width - 528) / 2, 0, null);
	}

}
