package it.polimi.dei.provafinale.carcassonne.model;


import it.polimi.dei.provafinale.carcassonne.Coord;

import java.util.HashMap;
import java.util.Map;

/**
 * Class representing game grid.
 * */
public class TileGrid {

	Map<Coord, Tile> grid; // TODO verificare che la visibilità di questo
							// attributo sia corretta
	private int upperEdge;
	private int rightEdge;
	private int lowerEdge;
	private int leftEdge;

	/**
	 * TileGrid constructor. Creates a new instance of TileGrid.
	 */
	public TileGrid() {
		grid = new HashMap<Coord, Tile>();
	}

	/**
	 * Checks if for a given Coord there is a neighbor Tile.
	 * 
	 * @param coord
	 *            an instance of Coord.
	 * @return true if there is a neighbor tile, false otherwise.
	 */
	public boolean hasNeighborForCoord(Coord coord) {
		for (SidePosition pos : SidePosition.values()) {
			Coord offset = SidePosition.getOffsetForPosition(pos);
			/* Looks if a neighbor Tile exists. */
			if (grid.containsKey(coord.add(offset))) {
				return true;
			}
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
	public boolean isTileCompatible(Tile tile, Coord coord) {
		/* If the cell is already in use we can't put a tile there. */
		if (grid.get(coord) != null) {
			return false;
		}
		/*
		 * The first tile that goes into (0,0) is the initial tile and is
		 * compatible by default.
		 */
		if (coord.getX() == 0 && coord.getY() == 0) {
			return true;
		}
		/* Check that we have at least one neighbor. */
		if (hasNeighborForCoord(coord) == false) {
			return false;
		}
		/* Check that all sides matches. */
		for (SidePosition position : SidePosition.values()) {
			Coord offset = SidePosition.getOffsetForPosition(position);
			Tile neighborTile = grid.get(coord.add(offset));
			if (neighborTile != null) {
				/* Start verifying that the two SidePosition are compatible. */
				Side currentSide = tile.getSide(position);
				SidePosition oppositePosition = position.getOpposite();
				Side oppositeSide = neighborTile.getSide(oppositePosition);
				if (currentSide.getType() != oppositeSide.getType()) {
					return false;
				}
			}
		}
		/* Tile is compatible. */
		return true;
	}

	/**
	 * Checks if a tile can be put into the grid.
	 * 
	 * @param tile
	 *            - a Tile we want to put in the grid.
	 * @return true if the Tile has at least one compatible place where to put
	 *         it, false instead.
	 */
	public boolean hasAPlaceFor(Tile tile) {
		for (Coord coord : grid.keySet()) {
			/* Calculates possible Coord in grid for the given Tile. */
			for (SidePosition pos : SidePosition.values()) {
				Coord offset = SidePosition.getOffsetForPosition(pos);
				Coord newPos = coord.add(offset);
				/* Checks compatibility. */
				if (isTileCompatible(tile, newPos)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Gives the Tile corresponding to the position included in the given Coord.
	 * 
	 * @param coord
	 *            - the Coord of a Tile.
	 * @return the Tile corresponding to a given Coord.
	 */
	public Tile getTile(Coord coord) {
		return grid.get(coord);
	}

	/**
	 * Sets a tile on the gived Coord.
	 * 
	 * @param tile
	 *            a Tile to be put into the grid at given Coord.
	 * @param coord
	 *            a Coord.
	 */
	public void putTile(Tile tile, Coord coord) {
		grid.put(coord, tile);
		int x = coord.getX();
		int y = coord.getY();
		/* Updating bounds of area to represent. */
		rightEdge = (x > rightEdge ? x : rightEdge);
		leftEdge = (x < leftEdge ? x : leftEdge);
		upperEdge = (y < upperEdge ? y : upperEdge);
		lowerEdge = (y > lowerEdge ? y : lowerEdge);
		/*
		 * Insert into the tile its position and a reference to the grid, useful
		 * to find its neighbors.
		 */
		tile.setCoords(coord);
	}

	/**
	 * Gets the Tile neighbor, opposite to the given SidePosition.
	 * 
	 * @param tile
	 *            - a Tile.
	 * @param position
	 *            - a SidePosition we want to know the neighbor.
	 * */
	public Tile getTileNeighbor(Tile tile, SidePosition position) {
		Coord coord = tile.getCoordinates();
		/* If the Tile has not corresponding Coord, it can't have a neighbor. */
		if (coord == null) {
			return null;
		}
		/* Calculate neighbor Coord and obtain it. */
		Coord offset = SidePosition.getOffsetForPosition(position);
		return grid.get(coord.add(offset));
	}

	/**
	 * Gets the dimension (upperbound) of the area currently occupied by tiles.
	 * 
	 * @return the bounds of the occupied area.
	 */
	public int[] getBounds() {
		return new int[] { upperEdge, rightEdge, lowerEdge, leftEdge };
	}

}