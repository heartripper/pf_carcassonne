package it.polimi.dei.provafinale.carcassonne.view.game;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import java.util.Map;

import javax.imageio.ImageIO;

public class TilePainter {

	private final int tileDim = 125;
	private final String tilesPathFormat = "src/main/resources/tiles/%s.png";

	private BufferedImage tilePlaceHolder;
	private String placeHolderPath = "src/main/resources/placeholder.png";

	private BufferedImage tileImage;

	private Map<String, BufferedImage> imgLib;

	private static TilePainter instance = null;

	public static TilePainter getInstance() {
		if (instance == null) {
			instance = new TilePainter();
		}
		return instance;
	}

	private TilePainter() {
//		imgLib = new HashMap<String, BufferedImage>();
//		String sourceInitialTales = "src/main/resources/tiles.txt";
		// String line;

		try {
//			FileReader fr = new FileReader(new File(sourceInitialTales));
//			BufferedReader in = new BufferedReader(fr);

//			 while ((line = in.readLine()) != null) {
//			
//			 }
		} catch (Exception e) {
			// TODO: handle exception
		}

		try {
			tileImage = ImageIO.read(new File(
					"src/main/resources/tiles/CCNN000000.png"));
			tilePlaceHolder = ImageIO.read(new File(placeHolderPath));
		} catch (IOException ioe) {
			System.out.println("Error reading tile images.");
		}
	}

	public void paintTile(String rep, Graphics g, int x, int y) {
		g.drawImage(tileImage, x, y, tileDim, tileDim, null);
	}

	public void paintPlaceHolder(Graphics g, int x, int y) {
		g.drawImage(tilePlaceHolder, x, y, tileDim, tileDim, null);
	}

	private BufferedImage readImage(String rep) {
		if (imgLib.containsKey(rep)) {
			return imgLib.get(rep);
		} else {
			String fileName = rep.replaceAll("[ ]??[NSWE]??[NSWE]=", "");
			String path = String.format(tilesPathFormat, fileName);
			try {
				BufferedImage img = ImageIO.read(new File(path));
				imgLib.put(rep, img);
				return img;
			} catch (IOException e) {
				System.out.println("Error reading image for card " + rep);
				return null;
			}
		}
	}
}
