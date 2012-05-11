package it.polimi.dei.provafinale.carcassonne.model.player;

import java.util.Arrays;

public class PlayerCircularArray {

	private Player players[];
	private int size;
	private int currIndex;
	private int position;

	/**
	 * Constructor: create a new instance of PlayerCircularArray.
	 * 
	 * @param num
	 *            - an int that establishes the dimension of the array.
	 */
	public PlayerCircularArray(int num) {
		players = new Player[num];
		size = num;
		for(int i = 0; i < size; i++)
			players[i] = new Player();
	
		position = 0;
		currIndex = 0;
	}

	/**
	 * Add a player to the game.
	 * 
	 * @param player
	 *            - a Player.
	 * @return true if a new Player is added to the game, false instead.
	 */
	public boolean add(Player player) {
		if (currIndex >= size)
			return false;
		players[currIndex++] = player;
		return true;
	}

	/**
	 * Give the next player that have to play in the game.
	 * 
	 * @return the next Player that have to play.
	 */
	public Player getNext() {
		Player p = players[position++];
		if (position == size)
			position = 0;
		return p;
	}

	/**
	 * @param color the color of the player to return;
	 * @return the Player corresponding to given color
	 * */
	public Player getByIndex(int index){
		return players[index];
	}
	
	/**
	 * Give a chart of the players that played the game.
	 * 
	 * @return an array of Players, sorted by score.
	 */
	public Player[] getChart() {
		Player[] chart = players.clone();
		Arrays.sort(chart);
		return chart;
	}

	/**
	 * Return the string representation of Player.
	 */
	@Override
	public String toString() {
		String rep = "";
		for (int i = 0; i < size; i++) {
			Player p = players[i];
			rep += p.getStatus();
			if (i != size - 1) {
				rep += " | ";
			}
		}
		return rep;
	}
}