package it.polimi.dei.provafinale.carcassonne.model.gamelogic.card;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.print.DocFlavor.INPUT_STREAM;

/**
 * Class to represent the tile stack. Contains methods to draw a tile, get the
 * initial tile and verify if there are still tiles to draw.
 * */
public class TileStack {

	private static ArrayList<Card> readTileStack = null;
	
	private static final String FILE_PATH = "src/main/resources/carcassonne.dat";

	private ArrayList<Card> tiles;
	private Card initialTile;

	public TileStack() {
		tiles = readTiles();
		initialTile = tiles.get(0);
		tiles.remove(0);
	}

	/**
	 * Randomly choose and then remove a tile from the stack. After that return
	 * the drew tile.
	 * 
	 * @return the drew tile.
	 */
	public Card drawTile() {
		int size = tiles.size();
		Random rand = new Random();
		int randomIndex = rand.nextInt(size);
		Card drewTile = tiles.get(randomIndex);
		tiles.remove(randomIndex);
		return drewTile;
	}

	/**
	 * Used to determine if there are still tiles in the stack.
	 * 
	 * @return true if there is at least a tiles in the stack, false instead.
	 */
	public boolean hasMoreTiles() {
		return (tiles.size() != 0);
	}

	/**
	 * Obtain the initial tile, which is the one that is put into play before
	 * the beginning of the match.
	 * 
	 * @return the initial tile.
	 * */
	public Card getInitialTile() {
		return initialTile;
	}

	/**
	 * Gives the number of tiles remaining in the stack.
	 * */
	public int remainingTilesNumber(){
		return tiles.size();
	}
	
	// helpers
	/**
	 * Reads tile representation from carcassonne.dat, creates a new Tile for
	 * each representation and then put the new tile into tile list.
	 * */
	private synchronized ArrayList<Card> readTiles() {
		if(readTileStack != null)
			return readTileStack;
		
		readTileStack = new ArrayList<Card>();
		try {
			FileReader fr = new FileReader(new File(FILE_PATH));
			BufferedReader input = new BufferedReader(fr);
			String line;

			line = input.readLine();
			while (line != null) {
				// Card creation
				Card tile = new Card(line);
				readTileStack.add(tile);
				line = input.readLine(); // Go on to next line
			}
			input.close();
		} catch (IOException e) {
			// TODO: throw exception
			System.out.println("Error opening tile file.");
		}
		return readTileStack;
	}
}
