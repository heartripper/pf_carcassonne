package it.polimi.dei.provafinale.carcassonne.view.menu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JPanel;

public class BannerPanel extends JPanel {

	private static final long serialVersionUID = -7347936654163580714L;
	private Image banner;
	private int width;

	public BannerPanel() {
		setPreferredSize(new Dimension(10, 175));
		String path = "src/main/resources/banner.png";
		banner = Toolkit.getDefaultToolkit().createImage(path);
		setBackground(new Color(70, 98, 151));
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		width = getWidth();
		g.drawImage(banner, (width - 528) / 2, 0, null);
	}

}
