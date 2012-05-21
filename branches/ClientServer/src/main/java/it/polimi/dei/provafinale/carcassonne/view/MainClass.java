package it.polimi.dei.provafinale.carcassonne.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import it.polimi.dei.provafinale.carcassonne.model.gamelogic.Coord;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.card.Card;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.card.TileGrid;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.card.TileStack;
import it.polimi.dei.provafinale.carcassonne.view.game.GamePanel;
import it.polimi.dei.provafinale.carcassonne.view.game.TilesPainter;
import it.polimi.dei.provafinale.carcassonne.view.game.TilesPanel;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class MainClass {
	
	private static String pathCard = "src/main/resources/card.png";
	private static Image cardImg;
	
	public static void main(String[] args) {
//		ViewManager.getInstance();
		
//		cardImg = Toolkit.getDefaultToolkit().createImage(pathCard);
//		
		JFrame frame = new JFrame("test");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(600, 600);

		GamePanel gm = new GamePanel();
		frame.add(gm);
		
		gm.setPlayers(5);
		gm.getPlayers()[0].setClientPlayer();

		
		
		frame.setVisible(true);
		


	}
}
