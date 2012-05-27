package it.polimi.dei.provafinale.carcassonne.model.gamelogic.tile;

/**
 * Class to represent a connection between two sides of a tile. It represents
 * connections between Sides and not between sidewPositions so it's easier to
 * perform card rotation.
 * */
public class SideConnection {
	private Side side1;
	private Side side2;

	/**
	 * SideConnection constructor. Creates a new instance of class
	 * SideConnection (constructs a SideConnection instance given the two sides
	 * that are connected).
	 * 
	 * @param side1
	 *            - a Side.
	 * @param side2
	 *            - a Side.
	 * */
	public SideConnection(Side side1, Side side2) {
		this.side1 = side1;
		this.side2 = side2;
	}

	@Override
	public int hashCode() {
		return side1.hashCode() + side2.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		/* The passed object is not an instance of SideConnection. */
		if (!(obj instanceof SideConnection)) {
			return false;
		}
		/* Checks if the passed SideConnection is equal to the current one. */
		SideConnection otherConn = (SideConnection) obj;
		return (side1 == otherConn.side1 && side2 == otherConn.side2)
				|| (side1 == otherConn.side2 && side2 == otherConn.side1);
	}

}
