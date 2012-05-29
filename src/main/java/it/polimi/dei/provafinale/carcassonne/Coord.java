package it.polimi.dei.provafinale.carcassonne;

/**
 * The class Coord indicates a coordinate: x represent the horizontal position,
 * y the vertical one.
 * 
 */
public class Coord {
	private int x;
	private int y;

	/**
	 * Coord constructor. Creates a new instance of class Coord.
	 * 
	 * @param x
	 *            - a number that represents the horizontal position in the
	 *            grid.
	 * @param y
	 *            - a number that represents the vertical position in the grid.
	 */
	public Coord(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * 
	 * @return the number that represents the horizontal position in the grid.
	 */
	public int getX() {
		return x;
	}

	/**
	 * 
	 * @return the number that represents the vertical position in the grid.
	 */
	public int getY() {
		return y;
	}

	/**
	 * Adds a coordinate to the current one.
	 * 
	 * @param p
	 *            - a Coord we want to add to the current one.
	 * @return the Coord given by the result of the sum of the two coordinates.
	 */
	public Coord add(Coord p) {
		return new Coord(this.x + p.x, this.y + p.y);
	}

	@Override
	public boolean equals(Object obj) {
		/* Case obj is not an instance of class Coord. */
		if (!(obj instanceof Coord)) {
			return false;
		}

		Coord coord = (Coord) obj;
		return (coord.x == x && coord.y == y);
	}

	@Override
	public int hashCode() {
		/* Calculating and returning hash. */
		return 72 * x + y;
	}

	@Override
	public String toString() {
		String base = String.format("(%s,%s)", x, y);
		int spaceToFill = 7 - base.length();
		while (spaceToFill > 0) {
			/* Case spaceToFill even. */
			if (spaceToFill % 2 == 0) {
				base = " " + base;
			}
			/* Case spaceToFill odd. */
			else {
				base = base + " ";
			}
			spaceToFill--;
		}
		return base;
	}

}