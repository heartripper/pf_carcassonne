package it.polimi.dei.provafinale.carcassonne.view.game;

import it.polimi.dei.provafinale.carcassonne.model.gamelogic.Coord;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.card.Card;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.card.SidePosition;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.player.PlayerColor;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

public class TilePainter {

	private final int tileDim = 125;
	private final String tilesPathFormat = "src/main/resources/tiles/%s.png";

	private BufferedImage tilePlaceHolder;
	private String placeHolderPath = "src/main/resources/placeholder.png";

	private Pattern followerPattern;

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

		String followerRegExp = "[NESW]=.-[RBVGN]";
		followerPattern = Pattern.compile(followerRegExp);
	}

	public void paintTile(String rep, Graphics g, int x, int y) {
		// Remove followers
		String base = rep.replaceAll("-.", "");
		BufferedImage img = getImage(base);
		g.drawImage(img, x, y, tileDim, tileDim, null);
		if (rep.indexOf('-') != -1) {

		}
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
		/* Rotate the image. */
		affineTransform.rotate(Math.toRadians(90 * rotCount));
		/* Draw the image using the AffineTransform. */
		BufferedImage rotateImage = new BufferedImage(tileDim, tileDim,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = rotateImage.createGraphics();
		g.drawImage(baseImage, affineTransform, null);
		g.dispose();
		return rotateImage;
	}

	private void paintFollower(String rep, Graphics g, int x, int y) {
		Matcher m = followerPattern.matcher(rep);
		char posChar = rep.charAt(m.start());
		char colChar = rep.charAt(m.end());
		PlayerColor color = PlayerColor.valueOf(String.valueOf(posChar));
		SidePosition pos = SidePosition.valueOf(String.valueOf(colChar));
		
		g.setColor(color.getColor());
		
		int smallDist = tileDim / 16;
		int mediumDist = tileDim / 8;
		Point p;
		switch (pos){
		case N: 
			p = new Point(7*smallDist, smallDist);
			break;
		case S:
			p = new Point(7*smallDist, 13*smallDist);
			break;
		case W:
			p = new Point(smallDist, 7*smallDist);
			break;
		case E:
			p = new Point(13*smallDist, 7*smallDist);
			break;
		default: 
			p = null;
			break;
		}
		
		g.drawOval(p.x, p.y, mediumDist, mediumDist);
		
	}
}
