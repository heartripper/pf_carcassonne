package it.polimi.dei.provafinale.carcassonne.view.game;

import it.polimi.dei.provafinale.carcassonne.Coord;
import it.polimi.dei.provafinale.carcassonne.model.SidePosition;
import it.polimi.dei.provafinale.carcassonne.model.Tile;
import it.polimi.dei.provafinale.carcassonne.model.TileGrid;

public class TileGridRepresenter {

	private TileGrid grid;

	/**
	 * TileGridRepresenter constructor. Creates a new instance of class
	 * TileGridRepresenter.
	 * 
	 * @param grid the grid to represent.
	 */
	public TileGridRepresenter(TileGrid grid) {
		this.grid = grid;
	}

	/**
	 * Gets the String representation of an instance of class
	 * TileGridRepresenter.
	 * 
	 * @return the String representation of an instance of class
	 *         TileGridRepresenter.
	 */
	public String getRepresentation() {
		/* Creating a new instance of a StringBuilder. */
		StringBuilder gridOutput = new StringBuilder();
		/* Obtaining grid bounds. */
		int[] bounds = grid.getBounds();

		for (int j = bounds[2] + 1; j >= bounds[0] - 1; j--) {
			/* Initializing string builder. */
			StringBuilder[] lines = new StringBuilder[7];
			for (int i = 0; i < lines.length; i++) {
				lines[i] = new StringBuilder("");
			}
			for (int i = bounds[3] - 1; i <= bounds[1] + 1; i++) {
				/* Creating a coordinate. */
				Coord currentCoord = new Coord(i, j);
				/* Obtaining the tile at the given coordinate. */
				Tile currentTile = grid.getTile(currentCoord);
				/* Initializing the tile representation (null). */
				String[] rep = null;
				/* Exists a tile at the given position. */
				if (currentTile != null) {
					/* Find out current representation. */
					rep = getTileArrayRepresentation(currentTile);
				}
				/* Exists a placeholder at the given position. */
				else if (grid.hasNeighborForCoord(currentCoord)) {
					rep = getPlaceHolder(currentCoord);
				}
				/* There are no tiles at the given position. */
				else {
					rep = getWhiteSpace();
				}
				/* Copy tile representation into line representation. */
				for (int k = 0; k < lines.length; k++) {
					lines[k].append(rep[k]);
				}
			}
			/* Copy tiles representation into GridOutput. */
			for (StringBuilder s : lines) {
				gridOutput.append(s.toString() + "\n");
			}
		}
		/* Return the String representation of the grid. */
		return gridOutput.toString();
	}

	/**
	 * Gives the String representation for the textual view of a tile.
	 * 
	 * @param tile
	 *            - an instance of class Tile.
	 * @return the String that represent the textual view of a tile.
	 * */
	public static String getTileRepresentation(Tile tile) {
		/* Getting the array representation of the tile. */
		String[] arrayRep = getTileArrayRepresentation(tile);
		/* Building the String representation of the tile. */
		StringBuilder representation = new StringBuilder();
		for (int i = 0; i < arrayRep.length; i++) {
			representation.append(arrayRep[i] + "\n");
		}
		return representation.toString();
	}

	/**
	 * Gives the String representation of an empty tile.
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
	 * Return the array of string representation of Card.
	 * 
	 * @return the String[] representation of a card with its sides and links.
	 */
	private static String[] getTileArrayRepresentation(Tile tile) {
		/* String representation of SidePosition. */
		String north = tile.getSide(SidePosition.N).toString();
		String east = tile.getSide(SidePosition.E).toString();
		String south = tile.getSide(SidePosition.S).toString();
		String west = tile.getSide(SidePosition.W).toString();
		/*
		 * Association of the representation of a link to the corresponding
		 * textual symbol.
		 */
		String linkNE = (tile.sideLinked(SidePosition.N, SidePosition.E) ? "\\"
				: " ");
		String linkNS = (tile.sideLinked(SidePosition.N, SidePosition.S) ? "|"
				: " ");
		String linkNW = (tile.sideLinked(SidePosition.N, SidePosition.W) ? "/"
				: " ");
		String linkES = (tile.sideLinked(SidePosition.E, SidePosition.S) ? "/"
				: " ");
		String linkEW = (tile.sideLinked(SidePosition.E, SidePosition.W) ? "-"
				: " ");
		String linkSW = (tile.sideLinked(SidePosition.S, SidePosition.W) ? "\\"
				: " ");
		/* Creating the array representation of the tile. */
		String[] representation = { "+#############+",
				String.format("#     %s     #", north),
				String.format("#   %s  %s  %s   #", linkNW, linkNS, linkNE),
				String.format("#%s   %s   %s#", west, linkEW, east),
				String.format("#   %s  %s  %s   #", linkSW, linkNS, linkES),
				String.format("#     %s     #", south), "+#############+" };

		return representation;
	}
}
