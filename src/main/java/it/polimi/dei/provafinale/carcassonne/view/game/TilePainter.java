package it.polimi.dei.provafinale.carcassonne.view.game;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class TilePainter {

	private final int TILE_DIM = 125;

	private BufferedImage tilePlaceHolder;
	private String placeHolderPath = "src/main/resources/placeholder.png";

	private String tilePath = "src/main/resources/card.png";
	private BufferedImage tileImage;
	
	private static TilePainter instance = null;
	
	public static TilePainter getInstance(){
		if(instance == null)
			instance = new TilePainter();
		
		return instance;
	}
	
	private TilePainter() {
		try {
			tilePlaceHolder = ImageIO.read(new File(placeHolderPath));
			tileImage = ImageIO.read(new File(tilePath));
		} catch (IOException ioe) {
			System.out.println("Error reading tile images.");
		}
	}

	public void paintTile(String rep, Graphics g, int x, int y) {
		g.drawImage(tileImage, x, y, TILE_DIM, TILE_DIM, null);
	}

	public void paintPlaceHolder(Graphics g, int x, int y) {
		g.drawImage(tilePlaceHolder, x, y, TILE_DIM, TILE_DIM, null);
	}
}
