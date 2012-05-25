package it.polimi.dei.provafinale.carcassonne.view.game;

import it.polimi.dei.provafinale.carcassonne.model.gamelogic.card.Card;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class TilePainter {

	private final int tileDim = 125;
	private final String tilesPathFormat = "src/main/resources/tiles/%s.png";

	private BufferedImage tilePlaceHolder;
	private String placeHolderPath = "src/main/resources/placeholder.png";

	private Map<String, BufferedImage> imgLib;

	private static TilePainter instance = null;

	public static TilePainter getInstance() {
		if (instance == null) {
			instance = new TilePainter();
		}
		return instance;
	}

	private TilePainter() {
		imgLib = new HashMap<String, BufferedImage>();
		String sourceInitialTales = "src/main/resources/tiles.txt";
		String line;

		try {
			FileReader fr = new FileReader(new File(sourceInitialTales));
			BufferedReader in = new BufferedReader(fr);
			while ((line = in.readLine()) != null) {
				BufferedImage currentImg = readBaseImage(line);
				if (currentImg != null) {
					imgLib.put(line, currentImg);
				}
			}
			
			tilePlaceHolder = ImageIO.read(new File(placeHolderPath));
		} catch (IOException ioe) {
			System.out.println("Error reading tile images.");
		}
	}

	public void paintTile(String rep, Graphics g, int x, int y) {
		BufferedImage img = getImage(rep);
		g.drawImage(img, x, y, tileDim, tileDim, null);
	}

	public void paintPlaceHolder(Graphics g, int x, int y) {
		g.drawImage(tilePlaceHolder, x, y, tileDim, tileDim, null);
	}

	private BufferedImage readBaseImage(String rep) {
		String fileName = rep.replaceAll("[ ]??[NSWE]??[NSWE]=", "");
		String path = String.format(tilesPathFormat, fileName);
		try {
			BufferedImage img = ImageIO.read(new File(path));
			return img;
		} catch (IOException e) {
			System.out.println("Error reading image for card " + rep);
			return null;
		}
	}

	private BufferedImage getImage(String rep) {
		/* Case base image. */
		if (imgLib.containsKey(rep)) {
			return imgLib.get(rep);
		}
		/* Case rotated image. */
		else {
			Card c = new Card(rep);
			int rotCount = 0;
			do {
				c.rotate();
				rotCount++;
			} while (!imgLib.containsKey(c.toString()) && rotCount < 4);

			if (rotCount == 4) {
				System.out.println("Error: base tile not found for " + rep
						+ ".");
				return null;
			}
			
			BufferedImage baseImage = imgLib.get(c.toString());
			BufferedImage image = rotateImage(baseImage, rotCount);
			imgLib.put(rep, image);
			return image;
		}
	}

	private BufferedImage rotateImage(BufferedImage baseImage, int rotCount) {
		// creating the AffineTransform instance
		AffineTransform affineTransform = new AffineTransform();
		/*Rotate the image.*/
		affineTransform.rotate(Math.toRadians(90 * rotCount));
		/*Draw the image using the AffineTransform.*/
		BufferedImage rotateImage = new BufferedImage(tileDim, tileDim,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = rotateImage.createGraphics();
		g.drawImage(baseImage, affineTransform, null);
		g.dispose();
		return rotateImage;
	}
}
