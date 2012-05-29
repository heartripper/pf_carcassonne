package it.polimi.dei.provafinale.carcassonne.model;

import it.polimi.dei.provafinale.carcassonne.PlayerColor;
import it.polimi.dei.provafinale.carcassonne.model.Player;

public class PlayerCircularArray {

	private Player players[];
	private int size;
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
			players[i].setColor(PlayerColor.valueOf(i));
		}
		position = 0;
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
	 * Add followers to players.
	 * 
	 * @param followers
	 *            - an array of integers which contains at index i the number of
	 *            followers to add to the player whose color has index i;
	 * */
	public void addFollowers(int[] followers){
		for(int i = 0; i < players.length; i++){
			players[i].addFollowers(followers[i]);
		}
	}
	
	
	/**
	 * Add scores to players.
	 * 
	 * @param scores
	 *            - an Array of integer containing scores, with the convention
	 *            that score at index i is added to player whose color has index
	 *            i
	 * */
	public void addScores(int[] scores) {
		for (int i = 0; i < players.length; i++) {
			players[i].addScore(scores[i]);
		}
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