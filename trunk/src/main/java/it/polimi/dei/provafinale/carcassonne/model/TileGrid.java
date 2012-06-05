package it.polimi.dei.provafinale.carcassonne.model;

import it.polimi.dei.provafinale.carcassonne.Coord;

import java.util.HashMap;
import java.util.Map;

/**
 * Class representing a grid of tiles.
 * */
public class TileGrid {

	private Map<Coord, Tile> grid;

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
		if (!hasNeighborForCoord(coord)) {
			return false;
		}
		/* Check that all sides matches. */
		for (SidePosition position : SidePosition.values()) {
			Coord offset = SidePosition.getOffsetForPosition(position);
			Tile neighborTile = grid.get(coord.add(offset));
			if (neighborTile != null) {
				Side currentSide = tile.getSide(position);
				SidePosition oppositePosition = position.getOpposite();
				Side oppositeSide = neighborTile.getSide(oppositePosition);
				if (currentSide.getType() != oppositeSide.getType()) {
					return false;
				}
			}
		}

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
		Tile t = new Tile(tile.toString());
		int maxNumRotations = 4;

		for (int i = 0; i < maxNumRotations; i++) {
			for (Coord coord : grid.keySet()) {
				/* Calculates possible Coord for the neighbors. */
				for (SidePosition pos : SidePosition.values()) {
					Coord offset = SidePosition.getOffsetForPosition(pos);
					Coord newPos = coord.add(offset);

					if (isTileCompatible(tile, newPos)) {
						return true;
					}
				}
			}
			t.rotate();
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
		Coord coord = tile.getCoords();
		/* A Tile that has not corresponding Coord can't have a neighbor. */
		if (coord == null) {
			return null;
		}

		Coord offset = SidePosition.getOffsetForPosition(position);
		return grid.get(coord.add(offset));
	}
}