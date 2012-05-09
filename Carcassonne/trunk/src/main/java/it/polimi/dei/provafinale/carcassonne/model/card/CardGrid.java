package it.polimi.dei.provafinale.carcassonne.model.card;

import it.polimi.dei.provafinale.carcassonne.model.Coord;

import java.util.HashMap;

public class CardGrid {

	HashMap<Coord, Card> grid;
	private int upperEdge;
	private int rightEdge;
	private int lowerEdge;
	private int leftEdge;

	/**
	 * Constructor: create a new instance of CardGrid.
	 */
	public CardGrid() {
		grid = new HashMap<Coord, Card>();
	}

	/**
	 * Give the card corresponding to the given coordinate.
	 * 
	 * @param coord
	 *            - the Coord of a Card.
	 * @return the Card corresponding to a given Coord.
	 */
	public Card getCard(Coord coord) {
		return grid.get(coord);
	}

	/**
	 * Set a card on a giving coordinate.
	 * 
	 * @param coord
	 *            - a Coord.
	 * @param card
	 *            - a Card to put on a Coord.
	 */
	public void setCard(Coord coord, Card card) {
		grid.put(coord, card);
		int x = coord.getX();
		int y = coord.getY();
		rightEdge = (x > rightEdge ? x : rightEdge);
		/*
		 * if (x>rightEdge) rightEdge = x; else rightEdge = rightEdge;
		 */
		leftEdge = (x < leftEdge ? x : leftEdge);
		upperEdge = (y > upperEdge ? y : upperEdge);
		lowerEdge = (y < lowerEdge ? y : lowerEdge);
	}

	/**
	 * Check the compatibility of a card with a coordinate.
	 * 
	 * @param card
	 *            - a Card.
	 * @param coord
	 *            - the Coord of a Card.
	 * @return true if a Card is compatible with a given Coord, false instead.
	 */
	public boolean checkCardCompatibility(Card card, Coord coord) {
		// Cards in (0,0) is the first added so it's compatible by default;
		if (coord.getX() == 0 && coord.getY() == 0)
			return true;
		// If the cell is already in use we can't put a card there
		if (grid.get(coord) != null)
			return false;
		boolean hasNeighbor = false, isCompatible = true;
		for (SidePosition position : SidePosition.values()) {
			Coord offset = SidePosition.getOffsetForPosition(position);
			Card neighborCard = grid.get(coord.add(offset));
			if (neighborCard != null) {
				hasNeighbor = true;
				Side currentSide = card.getSide(position);
				Side oppositeSide = neighborCard
						.getSide(position.getOpposite());
				if (currentSide.getType() != oppositeSide.getType()) {
					isCompatible = false;
					break;
				}
			}
		}
		return (hasNeighbor && isCompatible);
	}

	/**
	 * Check if a card can be put into the grid.
	 * 
	 * @param card
	 *            - a Card.
	 * @return true if a Card has at least one Coord where to put it, false
	 *         instead.
	 */
	public boolean canBePutIntoGrid(Card card) {
		for (Coord coord : grid.keySet()) {
			for (SidePosition pos : SidePosition.values()) {
				Coord newPos = coord
						.add(SidePosition.getOffsetForPosition(pos));
				if (checkCardCompatibility(card, newPos)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * The representation of an empty card.
	 * 
	 * @return the String that represent an empty Card.
	 */
	private String[] getWhiteSpace() {
		String[] whiteSpace = { "               ", "               ",
				"               ", "               ", "               ",
				"               ", "               " };
		return whiteSpace;
	}

	/**
	 * The representation of a card with its coordinate.
	 * 
	 * @param coord
	 *            - a Coord of a Card.
	 * @return the String that represent a Card with its Coord.
	 */
	private String[] getPlaceHolder(Coord coord) {
		String[] placeHolder = { "+.............+", ".             .",
				".             .",
				String.format(".   %s   .", coord.toString()),
				".             .", ".             .", "+.............+" };
		return placeHolder;
	}

	/**
	 * Return the string representation of CardGrid.
	 */
	public String toString() {
		String gridOutput = "";
		for (int j = upperEdge + 1; j >= lowerEdge - 1; j--) {
			String[] lines = { "", "", "", "", "", "", "" };
			for (int i = leftEdge - 1; i <= rightEdge + 1; i++) {
				Coord currentCoord = new Coord(i, j);
				String[] rep = null;
				if (grid.containsKey(currentCoord)) {
					rep = grid.get(currentCoord).getArrayRep();
				} else {
					for (SidePosition position : SidePosition.values()) {
						Coord neighborCoord = currentCoord.add(SidePosition
								.getOffsetForPosition(position));
						if (grid.containsKey(neighborCoord)) {
							rep = getPlaceHolder(currentCoord);
							break;
						}
					}
				}
				if (rep == null) {
					rep = getWhiteSpace();
				}
				for (int k = 0; k < lines.length; k++) {
					lines[k] += rep[k];
				}
			}
			for (String s : lines) {
				gridOutput += (s + "\n");
			}
		}
		return gridOutput;
	}

}