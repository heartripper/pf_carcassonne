package it.polimi.dei.provafinale.carcassonne.model.card;

import it.polimi.dei.provafinale.carcassonne.model.Coord;

public enum SidePosition {
	// Side position are stored using the convention N=1, E=2, S=3, W=4
	N(0), E(1), S(2), W(3);

	int index;

	/**
	 * Constructor: set a new instance of a side position.
	 * 
	 * @param index
	 *            - an index of a sidePosition.
	 */
	SidePosition(int index) {
		this.index = index;
	}

	/**
	 * Give the corresponding index of a side position
	 * 
	 * @return an index of a SidePosition.
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * 
	 */
	private static final Coord[] OFFSETS = { new Coord(0, 1), new Coord(1, 0),
			new Coord(0, -1), new Coord(-1, 0) };

	/**
	 * Give the offset to reach a given side position.
	 * 
	 * @param position
	 *            - a SidePosition.
	 * @return the offset in form of Coord to reach the given SidePosition.
	 */
	public static Coord getOffsetForPosition(SidePosition position) {
		int index = position.getIndex();
		return OFFSETS[index];
	}

	/**
	 * Retrieves the corresponding side position given its index.
	 * 
	 * @param index
	 *            - the index of the sidePosition.
	 * @return the desired sidePosition.
	 */
	public static SidePosition valueOf(int index) {
		SidePosition[] positions = SidePosition.values();
		if (index >= 0 && index < positions.length)
			return positions[index];
		else
			return null;
	}

	/**
	 * Give the side position opposite to the current one.
	 * 
	 * @return the SidePosition opposite to the current one.
	 */
	public SidePosition getOpposite() {
		int oppositeIndex = (index + 2) % 4;
		return SidePosition.valueOf(oppositeIndex);
	}
}