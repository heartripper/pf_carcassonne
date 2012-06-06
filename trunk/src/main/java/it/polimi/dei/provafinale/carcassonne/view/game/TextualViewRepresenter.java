package it.polimi.dei.provafinale.carcassonne.view.game;

import it.polimi.dei.provafinale.carcassonne.Coord;
import it.polimi.dei.provafinale.carcassonne.PlayerColor;
import it.polimi.dei.provafinale.carcassonne.model.EntityType;
import it.polimi.dei.provafinale.carcassonne.model.Side;
import it.polimi.dei.provafinale.carcassonne.model.SidePosition;
import it.polimi.dei.provafinale.carcassonne.model.Tile;

/**
 * The class TileGridRepresenter manages the textual representation of tiles in
 * the grid.
 * 
 */
public class TextualViewRepresenter {

	private static final int TILE_HEIGHT = 7;

	private TileRepresentationGrid grid;

	/**
	 * TileGridRepresenter constructor. Creates a new instance of class
	 * TileGridRepresenter.
	 * 
	 * @param grid
	 *            the grid to represent.
	 */
	public TextualViewRepresenter(TileRepresentationGrid grid) {
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
		
		StringBuilder gridOutput = new StringBuilder();

		for (int j = grid.greatestY(); j >= grid.smallestY(); j--) {
			/* Initializing string builder. */
			StringBuilder[] lines = new StringBuilder[TILE_HEIGHT];
			
			for (int i = 0; i < lines.length; i++) {
				lines[i] = new StringBuilder("");
			}

			for (int i = grid.smallestX(); i <= grid.greatestX(); i++) {
				Coord currentCoord = new Coord(i, j);
				String tileRep = grid.getTileRepresentation(currentCoord);
				Tile currentTile = null;
				if (tileRep != null) {
					currentTile = new Tile(tileRep);
				}

				String[] rep = null;
				/* Exists a tile at the given position. */
				if (currentTile != null) {
					rep = getTileArrayRepresentation(currentTile);
				}
				/* Exists a placeholder at the given position. */
				else if (grid.hasTileNeighbor(currentCoord)) {
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

			for (StringBuilder s : lines) {
				gridOutput.append(s.toString() + "\n");
			}
		}
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
		String north = getSideRep(tile.getSide(SidePosition.N));
		String east = getSideRep(tile.getSide(SidePosition.E));
		String south = getSideRep(tile.getSide(SidePosition.S));
		String west = getSideRep(tile.getSide(SidePosition.W));
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

	/**
	 * Gives the String representation of a Side.
	 * 
	 * @param s
	 *            a Side.
	 * @return the String representation of s.
	 */
	private static String getSideRep(Side s) {
		EntityType type = s.getType();
		String rep = (type == EntityType.N ? " " : type.toString());
		PlayerColor follower = s.getFollower();

		if (follower != null) {
			return String.format("%s:%s", rep, follower.toString());
		} else {
			return String.format(" %s ", rep);
		}
	}
}
