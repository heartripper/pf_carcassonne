package it.polimi.dei.provafinale.carcassonne;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public final class Utility {

	private Utility() {

	}

	/**
	 * Logs a message in the console.
	 * */
	public static void log(String msg) {
		System.out.println(msg);
	}

	/**
	 * Reads an image.
	 * 
	 * @param path
	 *            - the path in the classpath of the image to read.
	 * @return the read BufferedImage.
	 * */
	public static BufferedImage readImage(String path) {
		try {
			return ImageIO.read(Utility.class.getResourceAsStream(path));
		} catch (IOException e) {
			System.out.println("Error reading " + path);
			return null;
		}
	}
}