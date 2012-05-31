package it.polimi.dei.provafinale.carcassonne.view.game;

import it.polimi.dei.provafinale.carcassonne.Coord;
import it.polimi.dei.provafinale.carcassonne.model.SidePosition;

import java.util.HashMap;
import java.util.Map;

/**
 * Class TileRepresentationGrid holds the status of the game grid as String
 * representation of tiles.
 * 
 */
public class TileRepresentationGrid {

	private Map<Coord, String> tiles;
	private int top = 0, right = 0, bottom = 0, left = 0;

	/**
	 * Constructs a new instance of this class.
	 * */
	public TileRepresentationGrid() {
		this.tiles = new HashMap<Coord, String>();
	}

	/**
	 * Executes the passed update on the grid.
	 * 
	 * @param update
	 *            - the String containing the update to execute in the form
	 *            tileRepresentation, x, y
	 * */
	public void execUpdate(String update) {
		String[] split = update.split(",");
		String tileRep = split[0];
		int x = Integer.parseInt(split[1].trim());
		int y = Integer.parseInt(split[2].trim());
		Coord c = new Coord(x, y);

		if (tiles.containsKey(c)) {
			tiles.remove(c);
		} else {
			top = (y < top ? y : top);
			right = (x > right ? x : right);
			left = (x < left ? x : left);
			bottom = (y > bottom ? y : bottom);
		}

		tiles.put(c, tileRep);
	}

	/**
	 * Gives the textual representation of a tiles placed at a given coord.
	 * 
	 * @param coord
	 *            - the coords of desired tile
	 * */
	public String getTileRepresentation(Coord c) {
		return tiles.get(c);
	}

	/**
	 * Checks if there is a neighbor tile for a given position.
	 * 
	 * @param coord
	 *            - the coord to check
	 * @return true if there is at least a neighbor tile for the given coord.
	 * */
	public boolean hasTileNeighbor(Coord coord) {
		for (SidePosition pos : SidePosition.values()) {
			Coord offset = SidePosition.getOffsetForPosition(pos);
			if (tiles.containsKey(coord.add(offset))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Gives the grid bounds.
	 * 
	 * @return an int[] containing the grid's bounds
	 * */
	public int[] getBounds() {
		int[] bounds = { top, right, bottom, left };
		return bounds;
	}
}
