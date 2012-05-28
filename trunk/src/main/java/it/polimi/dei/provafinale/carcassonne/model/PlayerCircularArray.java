package it.polimi.dei.provafinale.carcassonne.model;

import it.polimi.dei.provafinale.carcassonne.model.Player;

public class PlayerCircularArray {

	private Player players[];
	private int size;
	private int currIndex;
	private int position;

	/**
	 * PlayerCircular Array constructor. Creates a new instance of class
	 * PlayerCircularArray.
	 * 
	 * @param num
	 *            - a number that establishes the dimension of the array to
	 *            create.
	 */
	public PlayerCircularArray(int num) {
		/* Creating the array. */
		players = new Player[num];
		size = num;
		/* Initializing the array. */
		for (int i = 0; i < size; i++) {
			players[i] = new Player();
		}
		/* Starting values. */
		position = 0;
		currIndex = 0;
	}

	/**
	 * Gives the Player that has to play the next turn in the game.
	 * 
	 * @return the next Player that has to play the next turn in the game.
	 */
	public Player getNext() {
		/* Find a Player that is active. */
		while (!players[position].isActive()) {
			position = (position + 1) % size;
		}
		/* Obtaining Player informations. */
		Player p = players[position];
		position = (position + 1) % size;
		return p;
	}

	/**
	 * Gives the Player corresponding to the given color.
	 * 
	 * @param color
	 *            the color that identifies the Player to return.
	 * @return the Player corresponding to the given color.
	 */
	public Player getByColor(PlayerColor color) {
		int index = PlayerColor.indexOf(color);
		return players[index];
	}

	/**
	 * Gives the number of players currently active in the game.
	 * 
	 * @return the number of players currently active in the game.
	 */
	public int getSize() {
		int i = 0;
		for (Player p : players) {
			if (p.isActive()) {
				i++;
			}
		}
		return i;
	}

	/**
	 * Gives the chart of the players that played the game.
	 * 
	 * @return an array scores.
	 */
	public int[] getScores() {
		int[] scores = new int[size];
		for (int i = 0; i < size; i++) {
			scores[i] = players[i].getScore();
		}
		return scores;
	}

}