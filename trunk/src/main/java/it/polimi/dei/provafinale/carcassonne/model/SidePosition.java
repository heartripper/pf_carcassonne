package it.polimi.dei.provafinale.carcassonne.model;

import it.polimi.dei.provafinale.carcassonne.Coord;

/**
 * The enum SidePosition gives the association of all the possible sides
 * position of a Tile (North, South, East, West) with a number.
 * 
 */
public enum SidePosition {
	/* Side position are stored using the convention N=1, E=2, S=3, W=4. */
	N(0), E(1), S(2), W(3);

	private int index;

	/**
	 * SidePosition constructor. Creates a new instance of a SidePosition.
	 * 
	 * @param index
	 *            - a number that represent the index of a sidePosition.
	 */
	SidePosition(int index) {
		this.index = index;
	}

	/**
	 * 
	 * @return the index of the current SidePosition.
	 */
	public int getIndex() {
		return index;
	}

	private static final Coord[] OFFSETS = { new Coord(0, 1), new Coord(1, 0),
			new Coord(0, -1), new Coord(-1, 0) };

	/**
	 * Gives the offset to reach a given SidePosition.
	 * 
	 * @param position
	 *            - a SidePosition we want to reach.
	 * @return the offset in form of Coord needed to reach the given
	 *         SidePosition.
	 */
	public static Coord getOffsetForPosition(SidePosition position) {
		int index = position.getIndex();
		return OFFSETS[index];
	}

	/**
	 * Retrieves the corresponding SidePosition given its index.
	 * 
	 * @param index
	 *            - the corresponding index of SidePosition.
	 * @return the desired SidePosition.
	 */
	public static SidePosition valueOf(int index) {
		SidePosition[] positions = SidePosition.values();
		/* Exists a position corresponding to the given index. */
		if (index >= 0 && index < positions.length) {
			return positions[index];
		}
		
		else {
			return null;
		}
	}

	/**
	 * Gives the SidePosition opposite to the current one.
	 * 
	 * @return the SidePosition opposite to the current one.
	 */
	public SidePosition getOpposite() {
		int oppositeIndex = (index + 2) % 4;
		return SidePosition.valueOf(oppositeIndex);
	}

}