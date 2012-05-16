package it.polimi.dei.provafinale.carcassonne.view.menu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JPanel;

public class MenuContainer extends JPanel{
	
	private Image banner;
	private int width;
	
	public MenuContainer(){
		String path = "src/main/resources/background.png";
		banner = Toolkit.getDefaultToolkit().createImage(path);
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		width = getWidth();
		g.drawImage(banner, (width-778)/2, 0, null);
	}
	
}
