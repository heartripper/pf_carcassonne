package it.polimi.dei.provafinale.carcassonne.model.gamelogic;

public class Coord {
	private int x;
	private int y;

	/**
	 * Constructor: Create a new instance of Coord.
	 * 
	 * @param x
	 *            - an int that represent the horizontal position in the grid.
	 * @param y
	 *            - an int that represent the vertical position in the grid.
	 */
	public Coord(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Get the horiziontal position in the grid.
	 * 
	 * @return the int value that represent the horizontal position in the grid.
	 */
	public int getX() {
		return x;
	}

	/**
	 * Get the vertical position in the grid.
	 * 
	 * @return the int value that represent the vertical position in the grid.
	 */
	public int getY() {
		return y;
	}

	/**
	 * Sum two coordinates.
	 * 
	 * @param p
	 *            - a Coord to add to the current one.
	 * @return the Coord given by the result of the sum of the two Coord.
	 */
	public Coord add(Coord p) {
		return new Coord(this.x + p.x, this.y + p.y);
	}

	/**
	 * Compare two coordinates.
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Coord))
			return false;
		Coord coord = (Coord) obj;
		return (coord.x == x && coord.y == y);
	}

	/**
	 * Calculate the object hash.
	 */
	@Override
	public int hashCode() {
		return 72 * x + y;
	}

	/**
	 * Return the string representation of Coord.
	 */
	@Override
	public String toString() {
		String base = String.format("(%s,%s)", x, y);
		int spaceToFill = 7 - base.length();
		while (spaceToFill > 0) {
			if (spaceToFill % 2 == 0) {
				base = " " + base;
			} else {
				base = base + " ";
			}
			spaceToFill--;
		}
		return base;
	}

}