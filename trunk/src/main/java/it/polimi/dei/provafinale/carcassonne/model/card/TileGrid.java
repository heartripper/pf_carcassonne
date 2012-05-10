package it.polimi.dei.provafinale.carcassonne.model.card;

import it.polimi.dei.provafinale.carcassonne.model.Coord;

import java.util.HashMap;

public class TileGrid {

	HashMap<Coord, Card> grid;
	private int upperEdge;
	private int rightEdge;
	private int lowerEdge;
	private int leftEdge;

	/**
	 * Constructor: create a new instance of CardGrid.
	 */
	public TileGrid() {
		grid = new HashMap<Coord, Card>();
	}

	/**
	 * Check if a tile can be put into the grid.
	 * 
	 * @param tile
	 *            - a Tile.
	 * @return true if the Tile has at least one compatible place where to put
	 *         it, false instead.
	 */
	public boolean hasAPlaceFor(Card tile) {
		for (Coord coord : grid.keySet()) {
			for (SidePosition pos : SidePosition.values()) {
				Coord offset = SidePosition.getOffsetForPosition(pos);
				Coord newPos = coord.add(offset);
				if (isTileCompatible(tile, newPos)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Give the tile corresponding to the given coordinate.
	 * 
	 * @param coord
	 *            - the Coord of a Tile.
	 * @return the Tile corresponding to a given Coord.
	 */
	public Card getTile(Coord coord) {
		return grid.get(coord);
	}

	/**
	 * Set a tile on a giving coordinate.
	 * 
	 * @param put
	 *            - a Tile to be put into the grid at given Coord.
	 * @param coord
	 *            - a Coord.
	 */
	public boolean putTile(Card tile, Coord coord) {
		if (!isTileCompatible(tile, coord))
			return false;

		grid.put(coord, tile);
		int x = coord.getX();
		int y = coord.getY();

		// Updating bounds of area to represent.
		rightEdge = (x > rightEdge ? x : rightEdge);
		leftEdge = (x < leftEdge ? x : leftEdge);
		upperEdge = (y > upperEdge ? y : upperEdge);
		lowerEdge = (y < lowerEdge ? y : lowerEdge);

		// Insert into the tile its position and a reference to the grid to find
		// its neighbors.
		tile.setPositionInfo(this, coord);

		return true;
	}

	public Card getTileNeighbor(Card card, SidePosition position){
		Coord coord = card.getCoordinates();
		if(coord == null)
			return null;
		Coord offset = SidePosition.getOffsetForPosition(position);
		return grid.get(coord.add(offset));
	}
	
	/**
	 * Check the compatibility of a tile with a coordinate.
	 * 
	 * @param tile
	 *            - a Tile.
	 * @param coord
	 *            - a Coord.
	 * @return true if the Tile is compatible with the given Coord, false
	 *         instead.
	 */
	private boolean isTileCompatible(Card tile, Coord coord) {
		// If the cell is already in use we can't put a tile there
		if (grid.get(coord) != null)
			return false;

		// The first tile that goes into (0,0) is the initial tile and is
		// compatible by default.
		if (coord.getX() == 0 && coord.getY() == 0)
			return true;

		// Check that all sides match with one of the same type.
		boolean hasNeighbor = false;
		for (SidePosition position : SidePosition.values()) {
			Coord offset = SidePosition.getOffsetForPosition(position);
			Card neighborTile = grid.get(coord.add(offset));
			if (neighborTile != null) {
				hasNeighbor = true;
				Side currentSide = tile.getSide(position);
				SidePosition oppositePosition = position.getOpposite();
				Side oppositeSide = neighborTile.getSide(oppositePosition);
				if (currentSide.getType() != oppositeSide.getType())
					return false;
			}
		}
		return hasNeighbor;
	}

	// Textual representation logic.

	/**
	 * The representation of an empty tile.
	 * 
	 * @return an array of String composing a placeholder for a cell with no
	 *         neighbor Tiles.
	 */
	private String[] getWhiteSpace() {
		String[] whiteSpace = { "               ", "               ",
				"               ", "               ", "               ",
				"               ", "               " };
		return whiteSpace;
	}

	/**
	 * The representation of an empty tile with its coordinate.
	 * 
	 * @param coord
	 *            - a Coord.
	 * @return an array of lines composing the placeholder for a cell with
	 *         neighbors Tiles.
	 */
	private String[] getPlaceHolder(Coord coord) {
		String[] placeHolder = { "+.............+", ".             .",
				".             .",
				String.format(".   %s   .", coord.toString()),
				".             .", ".             .", "+.............+" };
		return placeHolder;
	}

	/**
	 * Return the string representation of TileGrid.
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