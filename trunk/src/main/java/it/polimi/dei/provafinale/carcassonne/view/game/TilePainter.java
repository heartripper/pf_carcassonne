package it.polimi.dei.provafinale.carcassonne.view.game;

import it.polimi.dei.provafinale.carcassonne.Constants;
import it.polimi.dei.provafinale.carcassonne.PlayerColor;
import it.polimi.dei.provafinale.carcassonne.model.SidePosition;
import it.polimi.dei.provafinale.carcassonne.model.Tile;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
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

/**
 * Class to paint tiles images. Base tile images are load from files, while the
 * images of tiles obtained by basic by rotation are obtained by rotation too.
 * */
public final class TilePainter {

	private static final int ROTATION_DEGREES = 90;
	private static final int MAXIMUM_NUMBER_OF_ROTATION = 4;
	private final int tileDim;
	private BufferedImage tilePlaceHolder;
	private Map<String, BufferedImage> tileLib;
	private Map<String, BufferedImage> followersLib;
	private Pattern followerPattern;

	private static TilePainter instance = null;

	/**
	 * Returns the current instance of TilePainter which has all basic images
	 * loaded.
	 * */
	public static synchronized TilePainter getInstance() {
		if (instance == null) {
			instance = new TilePainter();
		}
		return instance;
	}

	/**
	 * TilePainter constructor. Creates a new instance of class TilePainter,
	 * which object loads placeholder and base tile images from files. It also
	 * compiles the pattern to match followers in tile representations.
	 * */
	private TilePainter() {
		/* Creating tileLib and followersLib. */
		this.tileLib = new HashMap<String, BufferedImage>();
		this.followersLib = new HashMap<String, BufferedImage>();
		/* Follower regular expression. */
		String followerRegExp = "(N|E|S|W)=.-(R|B|V|G|N)";
		this.followerPattern = Pattern.compile(followerRegExp);
		/* Setting tile dimension. */
		this.tileDim = Constants.TILE_PIXEL_DIMENSION;
		/* Read from file. */
		String line;
		BufferedReader in = null;
		try {
			/* Read base tile images */
			FileReader fr = new FileReader(new File(Constants.BASE_TILE_PATH));
			in = new BufferedReader(fr);
			while ((line = in.readLine()) != null) {
				BufferedImage currentImg = readBaseImage(line);
				/* Adding the tile into tileLib. */
				if (currentImg != null) {
					tileLib.put(line, currentImg);
				}
			}
			/* Read placeholder image */
			tilePlaceHolder = ImageIO
					.read(new File(Constants.PLACEHOLDER_PATH));
			/* Read followers image */
			for (PlayerColor c : PlayerColor.values()) {
				String path = String.format(
						Constants.FOLLOWERS_IMG_PATH_FORMAT, c);
				BufferedImage img = ImageIO.read(new File(path));
				/* Adding the follower into followersLib. */
				followersLib.put(c.toString(), img);
			}
		} catch (IOException ioe) {
			System.out.println("Error reading tile images.");
		}
		/* Closing in. */
		if (in != null) {
			try {
				in.close();
			} catch (IOException e) {
				System.out.println("Error opening tile file.");
			}
		}

	}

	/**
	 * Paints a tile.
	 * 
	 * @param rep
	 *            the String representation of the tile to paint.
	 * @param g
	 *            the Graphics to draw on.
	 * @param x
	 *            the horizontal offset of the tile.
	 * @param y
	 *            the vertical offset of the tile.
	 * */
	public void paintTile(String rep, Graphics g, int x, int y) {
		/* Remove followers from rep. */
		String base = rep.replaceAll("-.", "");
		/* Find the base tile into the repository. */
		BufferedImage img = getImage(base);
		/* Draw the tile. */
		g.drawImage(img, x, y, tileDim, tileDim, null);
		/* In case there was a follower in rep... */
		if (rep.indexOf('-') != -1) {
			/* print it. */
			paintFollower(rep, g, x, y);
		}
	}

	/**
	 * Paints a placeholder tile.
	 * 
	 * @param g
	 *            the Graphics to draw on.
	 * @param x
	 *            the horizontal offset of the tile.
	 * @param y
	 *            the vertical offset of the tile.
	 */
	public void paintPlaceHolder(Graphics g, int x, int y) {
		g.drawImage(tilePlaceHolder, x, y, tileDim, tileDim, null);
	}

	/**
	 * Reads a base image.
	 * 
	 * @param rep
	 *            - the String representation of the base image to read.
	 * @return the base image read.
	 * */
	private BufferedImage readBaseImage(String rep) {
		/*
		 * Obtaining the "path representation" of the tile (so we can find it in
		 * the repository).
		 */
		String fileName = rep.replaceAll("[ ]??[NSWE]??[NSWE]=", "");
		/* Create the complete path of the tile. */
		String path = String.format(Constants.TILE_PATH_FORMAT, fileName);
		/* Read from file successful. */
		try {
			return ImageIO.read(new File(path));
		} catch (IOException e) {
			System.out.println("Error reading image for card " + rep);
			return null;
		}
	}

	/**
	 * Giving the String representation of a tile, retrieves the image
	 * representing a Tile.
	 * 
	 * @param rep
	 *            - the String representation of the tile.
	 * @return the BufferedImage representing the tile.
	 * */
	private BufferedImage getImage(String rep) {
		/* Case base image. */
		if (tileLib.containsKey(rep)) {
			return tileLib.get(rep);
		}
		/* Case rotated image. */
		else {
			Tile c = new Tile(rep);
			int rotCount = 0;
			do {
				c.rotate();
				rotCount++;
			} while (!tileLib.containsKey(c.toString()) && rotCount < MAXIMUM_NUMBER_OF_ROTATION);
			/* Reached the maximum number of possible rotations. */
			if (rotCount == MAXIMUM_NUMBER_OF_ROTATION) {
				System.out.println("Error: base tile not found for " + rep
						+ ".");
				return null;
			}
			/* Getting the base image. */
			BufferedImage baseImage = tileLib.get(c.toString());
			/* Getting the rotated image. */
			BufferedImage image = rotateImage(baseImage, rotCount);
			/* Put the rotated image into tileLib. */
			tileLib.put(rep, image);
			/* Return the rotated image. */
			return image;
		}
	}

	/**
	 * Creates a new image from a base one and rotates it a given number of
	 * times.
	 * 
	 * @param baseImage
	 *            - the BufferedImage to rotate.
	 * @param rotCount
	 *            - the number of rotations
	 * @return the rotated image
	 * */
	private BufferedImage rotateImage(BufferedImage baseImage, int rotCount) {
		// creating the AffineTransform instance
		AffineTransform affineTransform = new AffineTransform();
		/* Rotate the image. */
		double radians = -Math.toRadians(ROTATION_DEGREES * rotCount);
		float position = ((float) tileDim / 2);
		affineTransform.rotate(radians, position, position);
		/* Draw the image using the AffineTransform. */
		BufferedImage rotateImage = new BufferedImage(tileDim, tileDim,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = rotateImage.createGraphics();
		g.drawImage(baseImage, affineTransform, null);
		g.dispose();
		return rotateImage;
	}

	/**
	 * Paints the follower on a tile.
	 * 
	 * @param rep
	 *            - The tile String representation
	 * @param g
	 *            - the Graphics to paint on
	 * @param x
	 *            - the Horizontal offset of the tile
	 * @param y
	 *            - the Vertical offset of the tile
	 * */
	private void paintFollower(String rep, Graphics g, int x, int y) {
		Matcher m = followerPattern.matcher(rep);
		if (!m.find()) {
			return;
		}

		char posChar = rep.charAt(m.start());
		char colChar = rep.charAt(m.end() - 1);
		PlayerColor color = PlayerColor.valueOf(String.valueOf(colChar));
		SidePosition pos = SidePosition.valueOf(String.valueOf(posChar));

		g.setColor(color.getColor());

		int smallSpacer = tileDim / 16;
		int bigSpacer = 2 * tileDim / 5;

		Point p;
		switch (pos) {
		case N:
			p = new Point(bigSpacer, smallSpacer);
			break;
		case S:
			p = new Point(bigSpacer, 2*bigSpacer - smallSpacer);
			break;
		case W:
			p = new Point(smallSpacer, bigSpacer);
			break;
		case E:
			p = new Point(2*bigSpacer - smallSpacer, bigSpacer);
			break;
		default:
			p = new Point(tileDim / 2, tileDim / 2);
			break;
		}

		int followerDim = Constants.FOLLOWER_PIXEL_DIMENSION;
		BufferedImage img = followersLib.get(color.toString());
		g.drawImage(img, x + p.x, y + p.y, followerDim, followerDim, null);

	}
}
