package it.polimi.dei.provafinale.carcassonne.model.card;

import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

public class CardReader {

	private static final String FILE_PATH = "src/main/resources/carcassonne.dat";

	/**
	 * Constructor: Create a new instance of CardReader.
	 */
	private CardReader() {

	}

	/**
	 * Read the cards from a path and put them in a structure.
	 * 
	 * @return an ArrayList of Card containing all the read cards.
	 */
	public static ArrayList<Card> readCards() {
		ArrayList<Card> cards = new ArrayList<Card>();

		try {
			FileReader fr = new FileReader(new File(FILE_PATH));
			BufferedReader input = new BufferedReader(fr);
			String line;
			
			line = input.readLine();
			while (line != null) {
				// Card creation
				Card card = new Card(line);
				cards.add(card);
				line = input.readLine(); // Go on to next line
			}
		} catch (IOException e) {
			// TODO: throw exception
			System.out
					.println("Errore nell'apertura del file carcassonne.dat.");
		}
		return cards;
	}
}
