package it.polimi.dei.provafinale.carcassonne.model;

import java.awt.Color;

public enum PlayerColor {

	R("Rosso", Color.RED), B("Blu", Color.BLUE), V("Verde", Color.GREEN), G(
			"Giallo", Color.YELLOW), N("Nero", Color.BLACK);

	private String fullName;
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
	PlayerColor(String fullName, Color color) {
		this.fullName = fullName;
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
