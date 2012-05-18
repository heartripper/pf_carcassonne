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

import it.polimi.dei.provafinale.carcassonne.model.Coord;
import it.polimi.dei.provafinale.carcassonne.model.card.Card;
import it.polimi.dei.provafinale.carcassonne.model.card.TileGrid;
import it.polimi.dei.provafinale.carcassonne.model.card.TileStack;
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
		// ViewManager.getInstance();
		
		cardImg = Toolkit.getDefaultToolkit().createImage(pathCard);
		
		JFrame frame = new JFrame("test");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(600, 600);
		
		
		TileGrid grid = new TileGrid();
		grid.putTile(new Card("N=N S=C O=S E=S NS=0 NE=0 NO=0 OE=1 SE=0 SO=0"), new Coord(0, 0));
		grid.putTile(new Card("N=C S=C O=N E=N NS=0 NE=0 NO=0 OE=0 SE=0 SO=0"), new Coord(0, -1));
		grid.putTile(new Card("N=C S=C O=N E=N NS=0 NE=0 NO=0 OE=0 SE=0 SO=0"), new Coord(0, -2));
		grid.putTile(new Card("N=C S=C O=S E=S NS=0 NE=0 NO=0 OE=0 SE=0 SO=0"), new Coord(-1,0));
			
		TilesPanel tilesPanel = new TilesPanel(grid);
		
		frame.add(tilesPanel);
		
		try{
			Thread.sleep(1);
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		frame.setVisible(true);
		
		try{
			Thread.sleep(1);
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
}
