package it.polimi.dei.provafinale.carcassonne.model.gamelogic.card;

import it.polimi.dei.provafinale.carcassonne.model.gamelogic.Coord;

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
	 * Checks if for a given coordinate there is a neighbor card.
	 * 
	 * @param - coord a Coordinate
	 * @return true if there is a neighbor tile, false otherwise.
	 * */
	public boolean hasNeighborForCoord(Coord coord) {
		for (SidePosition pos : SidePosition.values()) {
			Coord offset = SidePosition.getOffsetForPosition(pos);
			if (grid.containsKey(coord.add(offset)))
				return true;
		}
		return false;
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
	public boolean isTileCompatible(Card tile, Coord coord) {
		// If the cell is already in use we can't put a tile there
		if (grid.get(coord) != null)
			return false;

		// The first tile that goes into (0,0) is the initial tile and is
		// compatible by default.
		if (coord.getX() == 0 && coord.getY() == 0)
			return true;

		// Check that we have at least one neighbor
		if (!hasNeighborForCoord(coord))
			return false;

		// Check that all sides matches
		for (SidePosition position : SidePosition.values()) {
			Coord offset = SidePosition.getOffsetForPosition(position);
			Card neighborTile = grid.get(coord.add(offset));
			if (neighborTile != null) {
				Side currentSide = tile.getSide(position);
				SidePosition oppositePosition = position.getOpposite();
				Side oppositeSide = neighborTile.getSide(oppositePosition);
				if (currentSide.getType() != oppositeSide.getType())
					return false;
			}
		}

		// Tile is compatible
		return true;
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
	public void putTile(Card tile, Coord coord) {
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
	}

	/**
	 * Gets the neighbor of a tile at a position.
	 * 
	 * @param card
	 *            - a Card
	 * @param position
	 *            - a SidePosition
	 * */
	public Card getTileNeighbor(Card card, SidePosition position) {
		Coord coord = card.getCoordinates();
		if (coord == null)
			return null;
		Coord offset = SidePosition.getOffsetForPosition(position);
		return grid.get(coord.add(offset));
	}

	/**
	 * Gets the occupied area in the map
	 * */
	public int[] getBounds() {
		int[] bounds = { upperEdge, rightEdge, lowerEdge, leftEdge };
		return bounds;
	}

}