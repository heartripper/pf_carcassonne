package it.polimi.dei.provafinale.carcassonne.model.gamelogic.player;

public enum PlayerColor {

	R("Rosso"), B("Blu"), V("Verde"), G("Giallo"), N("Nero");

	private String fullName;

	/**
	 * Give the color corresponding to the id of a player.
	 * 
	 * @param id
	 *            - an int that identifies a player.
	 * @return the PlayerColor corresponding to the Player id.
	 */
	public static PlayerColor valueOf(int id) {
		PlayerColor[] colors = PlayerColor.values();
		if (id < colors.length && id >= 0)
			return colors[id];
		else
			return null;
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
		for (int i = 0; i < values.length; i++)
			if (values[i] == color)
				return i;
		return -1;
	}

	/**
	 * Constructor: Create a new instance of PlayerColor.
	 * 
	 * @param fullName
	 *            - the full name of a color.
	 */
	PlayerColor(String fullName) {
		this.fullName = fullName;
	}

	/**
	 * Give the full name of a color.
	 * 
	 * @return the full name of a PlayerColor.
	 */
	public String getFullName() {
		return fullName;
	}

}
