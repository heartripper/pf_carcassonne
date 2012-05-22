package it.polimi.dei.provafinale.carcassonne.model.textinterface;

import it.polimi.dei.provafinale.carcassonne.model.gamelogic.Coord;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.card.Card;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.card.SidePosition;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.card.TileGrid;

public class TileGridRepresenter {

	private TileGrid grid;

	public TileGridRepresenter(TileGrid grid) {
		this.grid = grid;
	}

	/**
	 * Return the string representation of TileGrid.
	 */
	public String getRepresentation() {
		String gridOutput = "";
		int[] bounds = grid.getBounds();

		for (int j = bounds[2] + 1; j >= bounds[0] - 1; j--) {
			String[] lines = { "", "", "", "", "", "", "" };
			for (int i = bounds[3] - 1; i <= bounds[1] + 1; i++) {
				Coord currentCoord = new Coord(i, j);
				Card currentTile = grid.getTile(currentCoord);
				String[] rep = null;

				//Find out current representation
				if (currentTile != null) {
					rep = getTileArrayRepresentation(currentTile);
				} else if(grid.hasNeighborForCoord(currentCoord)){
					rep = getPlaceHolder(currentCoord);
				}else{
					rep = getWhiteSpace();
				}
				
				//Copy tile representation into line representation
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

	/**
	 * Gives a the representation for the textual view of a tile.
	 * 
	 * @param tile
	 *            - a tile
	 * @return the textual representation
	 * */
	public String getTileRepresentation(Card tile) {
		String[] arrayRep = getTileArrayRepresentation(tile);
		String representation = "";
		for (int i = 0; i < arrayRep.length; i++) {
			representation += arrayRep[i];
			if (i != arrayRep.length - 1)
				representation += "\n";
		}
		return representation;
	}

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
	 * Return the array of string representation of Card.
	 * 
	 * @return the String[] representation of a card with its sides and links.
	 */
	private String[] getTileArrayRepresentation(Card tile) {
		String north = tile.getSide(SidePosition.N).toString();
		String east = tile.getSide(SidePosition.E).toString();
		String south = tile.getSide(SidePosition.S).toString();
		String west = tile.getSide(SidePosition.W).toString();
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
		String[] representation = { "+#############+",
				String.format("#     %s     #", north),
				String.format("#   %s  %s  %s   #", linkNW, linkNS, linkNE),
				String.format("#%s   %s   %s#", west, linkEW, east),
				String.format("#   %s  %s  %s   #", linkSW, linkNS, linkES),
				String.format("#     %s     #", south), "+#############+" };
		return representation;
	}
}
