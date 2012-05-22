package it.polimi.dei.provafinale.carcassonne.view.game;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class TilePainter {

	private final int TILE_DIM = 125;
	private final String TILES_PATH_FORMAT = "src/main/resources/tiles/%s.png";
	
	private BufferedImage tilePlaceHolder;
	private String placeHolderPath = "src/main/resources/placeholder.png";

	private String tilePath = "src/main/resources/card.png";
	private BufferedImage tileImage;
	private HashMap<String, BufferedImage> imgLib;
	
	private static TilePainter instance = null;
	
	public static TilePainter getInstance(){
		if(instance == null)
			instance = new TilePainter();
		
		return instance;
	}
	
	private TilePainter() {
		imgLib = new HashMap<String, BufferedImage>();
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
	
	private BufferedImage readImage(String rep){
		if(imgLib.containsKey(rep)){
			return imgLib.get(rep);
		}else{
			String path = String.format(TILES_PATH_FORMAT, rep);
			try{
				BufferedImage img = ImageIO.read(new File(path));
				imgLib.put(rep, img);
				return img;
			}catch(IOException e){
				System.out.println("Error reading image for card " + rep);
				return null;
			}
		}
	}
}
