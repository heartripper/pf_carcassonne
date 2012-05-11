package it.polimi.dei.provafinale.carcassonne.model;

import it.polimi.dei.provafinale.carcassonne.model.card.Card;
import it.polimi.dei.provafinale.carcassonne.model.card.SidePosition;
import it.polimi.dei.provafinale.carcassonne.model.player.Player;
import it.polimi.dei.provafinale.carcassonne.model.player.PlayerColor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class GameEngine {

	private final String ERROR_NUMBER_FORMAT_MESSAGE = "Error number format.";
	private final String ERROR_READING_INPUT_MESSAGE = "Error reading input.";
	private final String INPUT_ERROR_MESSAGE = "Input error!";
	private final String INSERT_PLAYERS_NUMBER_MESSAGE = "Insert players number (2-5):";

	private static GameEngine instance;

	private Match match;
	private PlayerColor currentPlayer;
	private Card currentTile;
	
	private BufferedReader input;

	/**
	 * Constructor: create a new instance of GameEngine.
	 */
	private GameEngine() {
		InputStreamReader reader = new InputStreamReader(System.in);
		input = new BufferedReader(reader);
	}

	/**
	 * Get an instance of GameEngine and, if it doesn't exist, create a new one.
	 * 
	 * @return an instance of GameEngine.
	 */
	public static GameEngine getInstance() {
		if (instance == null) {
			instance = new GameEngine();
		}
		return instance;
	}

	/**
	 * 
	 * @return
	 */
	private int readPlayersNumber() {
		int playersNumber = 0;
		System.out.println(INSERT_PLAYERS_NUMBER_MESSAGE);
		String line = new String();

		while (playersNumber == 0) {
			try {
				line = input.readLine();
				int number = Integer.parseInt(line);
				if (number <= 5 && number >= 2) {
					playersNumber = number;
				} else {
					System.out.println(INPUT_ERROR_MESSAGE
							+ INSERT_PLAYERS_NUMBER_MESSAGE);
				}
			} catch (IOException e) {
				System.out.println(ERROR_READING_INPUT_MESSAGE);
			} catch (NumberFormatException e) {
				System.out.println(ERROR_NUMBER_FORMAT_MESSAGE
						+ INSERT_PLAYERS_NUMBER_MESSAGE);
			}
		}
		return playersNumber;
	}

	/**
	 * Create a new match and add the players to the match. If the match is not
	 * finished (there are cards) manage the players turn: for each turn print
	 * the match status, the drew card, manage the opportunity to rotate, to
	 * position and to add a coin on an entity. If the match is finished print
	 * the final grid and the players chart.
	 */
	public void startGame() {
		int playersNumber = readPlayersNumber();
		match = new Match(playersNumber);

		while (match.hasMoreCards()) {
			// Starts the turn
			currentPlayer = match.getNextPlayer().getColor();
			currentTile = match.drawCard();
			
			System.out.println(match);
			System.out.printf("Player %s turn:\n", currentPlayer);
			// show card.
			System.out.println(currentTile);

			boolean endTurn = false;
			boolean cardAdded = false;
			while (!endTurn) {
				String command = readCommand();

				if (command == null)
					continue;

				if (command.equals("ruota") && !cardAdded) {
					manageCardRotation();
				} else if (cardAdded && command.equals("passo")) {
					endTurn = true;
				} else if (command.matches("[-]??[0-9]+,[-]??[0-9]+")) { //?? means 0 or 1 times.
					cardAdded = manageCardPositioning(command);
				} else if (command.matches("[NESO]") && cardAdded) {
					endTurn = manageCoinPositioning(command);
				} else {
					System.out.println("Inserted invalid command.");
				}  
			}
			System.out.println(match.checkForCompletedEntities(currentTile));
		}

		// When the match is finished...
		// Print the grid
		System.out.println(match);
		// Finalize the game
		System.out.println("\nEnd game.\nChecking for non completed entities:");
		System.out.println(match.finalizeMatch());
		// Print the chart
		System.out.println("\nChart:");
		Player[] chart = match.getPlayerChart();
		for (int i = 0; i < chart.length; i++) {
			Player p = chart[i];
			System.out.printf("%s. %s (%s)\n", i + 1, p, p.getScore());
		}
	}

	private void manageCardRotation() {
		currentTile.rotate();
		System.out.println("Rotated card:\n" + currentTile);
		return;
	}

	private boolean manageCardPositioning(String command) {
		String[] split = command.split(",");
		int x = Integer.parseInt(split[0]);
		int y = Integer.parseInt(split[1]);

		if (match.putTile(currentTile, new Coord(x,y))) {
			System.out.printf("Card put in %s,%s.\n", x, y);
			return true;
		} else {
			System.out.println("Invalid position for card.");
			return false;
		}
	}

	private boolean manageCoinPositioning(String command) {
		try {
			SidePosition position = SidePosition.valueOf(command);
			if (match.addFollower(currentTile, position, currentPlayer)) {
				System.out.println("Coin was added to specific position.");
				return true;
			} else {
				System.out.println("You cant' add a coin there.");
				return false;
			}
		} catch (Exception e) {
			System.out.println("You entered an invalid position.");
			return false;
		}
	}

	private String readCommand() {
		try {
			System.out.printf("(Player %s) Insert command:\n", currentPlayer);
			String c = input.readLine();
			return c;
		} catch (IOException ioe) {
			System.out.println("Error while reading input.");
			return null;
		}
	}
}