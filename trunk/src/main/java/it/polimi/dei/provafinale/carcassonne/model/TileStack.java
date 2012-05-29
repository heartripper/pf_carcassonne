package it.polimi.dei.provafinale.carcassonne.model;

import it.polimi.dei.provafinale.carcassonne.Constants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Class to represent the tile stack. Contains methods to draw a tile, get the
 * initial tile and verify if there are still tiles to draw.
 * */
public class TileStack {

	private static List<Tile> readTileStack = null;

	private List<Tile> tiles;
	private Tile initialTile;

	/**
	 * TileStack constructor. Creates a new instance of class TileStack.
	 */
	public TileStack() {
		tiles = readTiles();
		initialTile = tiles.get(0);
		tiles.remove(0);
	}

	/**
	 * Randomly choose and then remove a Tile from the stack. After that return
	 * the drew Tile.
	 * 
	 * @return the drew Tile.
	 */
	public Tile drawTile() {
		/* Setting Tile dimension. */
		int size = tiles.size();
		/* Random choice of a Tile. */
		Random rand = new Random();
		int randomIndex = rand.nextInt(size);
		/* Obtain the Tile representation. */
		Tile drewTile = tiles.get(randomIndex);
		/*
		 * Remove the index of the given Tile from the list of the available
		 * random numbers.
		 */
		tiles.remove(randomIndex);
		/* Return the tile representation. */
		return drewTile;
	}

	/**
	 * Determines if there are still tiles in the stack.
	 * 
	 * @return true if there is at least a tiles in the stack, false instead.
	 */
	public boolean hasMoreTiles() {
		return (tiles.size() != 0);
	}

	/**
	 * Obtains the initial Tile, which is the one that is put into the grid
	 * before the beginning of the match.
	 * 
	 * @return the initial Tile.
	 * */
	public Tile getInitialTile() {
		return initialTile;
	}

	/* Helper methods. */

	/**
	 * Reads tile representation from carcassonne.dat, creates a new Tile for
	 * each representation and then put the new tile into tile list.
	 * */

	private synchronized List<Tile> readTiles() {
		/*
		 * Case we have already read some tiles: we return the array that
		 * contains the read tiles.
		 */
		if (readTileStack != null) {
			return new ArrayList<Tile>(readTileStack);
		}
		/* Case we haven't read tiles yet. */
		/* Obtaining the path to reach file carcassonne.dat. */
		String path;
		if (Constants.USE_FEW_TILES) {
			path = Constants.LESS_TILES_PATH;
		} else {
			path = Constants.TILES_PATH;
		}
		/* Creating a new ArrayList to put the list of tiles in. */
		readTileStack = new ArrayList<Tile>();
		BufferedReader input = null;
		try {
			FileReader fr = new FileReader(new File(path));
			input = new BufferedReader(fr);
			String line;
			line = input.readLine();
			while (line != null) {
				/* Card creation. */
				Tile tile = new Tile(line);
				readTileStack.add(tile);
				/* Go on to next line */
				line = input.readLine();
			}
		} catch (IOException e) {
			// TODO: throw exception
			System.out.println("Error opening tile file.");
		}
		/* Close stream if it has been opened. */
		if (input != null) {
			try {
				input.close();
			} catch (IOException e) {
				System.out.println("Error opening tile file.");
			}
		}
		return new ArrayList<Tile>(readTileStack);
	}
}