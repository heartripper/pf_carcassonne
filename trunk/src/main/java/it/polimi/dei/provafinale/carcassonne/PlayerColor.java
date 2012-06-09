package it.polimi.dei.provafinale.carcassonne;

import java.awt.Color;

/**
 * The enum PlayerColor contains a list of all the possible player colors: RED,
 * BLUE, GREEN, YELLOW, BLACK.
 * 
 */
public enum PlayerColor {

	R("red", "R", Color.RED), B("blue", "B", Color.BLUE), G("green", "G",
			Color.GREEN), Y("yellow", "Y", Color.YELLOW), K("black", "K",
			Color.BLACK);

	private String fullName;
	private String symbol;
	private Color color;

	/**
	 * Gives the color corresponding to the identificator of a Player.
	 * 
	 * @param id
	 *            - an number that identifies a Player.
	 * @return the PlayerColor corresponding to the Player identificator.
	 */
	public static PlayerColor valueOf(int id) {
		PlayerColor[] colors = PlayerColor.values();
		/* A color can be found. */
		if (id < colors.length && id >= 0) {
			return colors[id];
		}
		/* A color cannot be found. */
		else {
			return null;
		}
	}

	/**
	 * Gives the instance of PlayerColor corresponding either to a symbol or to
	 * a full name.
	 * 
	 * @param s
	 *            - a String containing the symbol or the full name of a color.
	 * @return an instance of PlayerColor
	 * */
	public static PlayerColor getColorFor(String s) {
		for (PlayerColor col : values()) {
			if (s.equals(col.fullName) || s.equals(col.symbol)) {
				return col;
			}
		}
		throw new RuntimeException("No playerColor instance for " + s);
	}

	/**
	 * Gives the index of the given color among all color instances.
	 * 
	 * @param color
	 *            - the color to return the index
	 * @return the color's index
	 * */
	public static int indexOf(PlayerColor color) {
		PlayerColor[] values = values();
		for (int i = 0; i < values.length; i++) {
			if (values[i] == color) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * PlayerColor constructor. Creates a new instance of class PlayerColor.
	 * 
	 * @param fullName
	 *            - the full name of a color.
	 */
	PlayerColor(String fullName, String symbol, Color color) {
		this.fullName = fullName;
		this.symbol = symbol;
		this.color = color;
	}

	/**
	 * 
	 * @return the full name of a PlayerColor.
	 */
	public String getFullName() {
		return fullName;
	}

	/**
	 * 
	 * @return the color corresponding to the current PlayerColor.
	 * */
	public Color getColor() {
		return color;
	}
}
