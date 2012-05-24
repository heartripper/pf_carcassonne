package it.polimi.dei.provafinale.carcassonne.model.gamelogic.player;

import it.polimi.dei.provafinale.carcassonne.model.gamelogic.player.Player;

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
		for(int i = 0; i < size; i++){
			players[i] = new Player();
		}
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
		if (currIndex >= size){
			return false;
		}
		players[currIndex++] = player;
		return true;
	}

	/**
	 * Give the next player that have to play in the game.
	 * 
	 * @return the next Player that have to play.
	 */
	public Player getNext() {
		while(!players[position].isActive()){
			position = (position + 1) % size;
		}
		
		Player p = players[position];
		position = (position + 1) % size;
		return p;
	}

	/**
	 * @param color the color of the player to return;
	 * @return the Player corresponding to given color
	 * */
	public Player getByColor(PlayerColor color){
		int index = PlayerColor.indexOf(color);
		return players[index];
	}
	
	public int getSize(){
		int i = 0;
		for(Player p : players){
			if(p.isActive()){
				i++;
			}
		}
		return i;
	}
	
	/**
	 * Give a chart of the players that played the game.
	 * 
	 * @return an array of Players, sorted by score.
	 */
	public int[] getScores(){
		int[] scores = new int[size];
		for(int i = 0; i<size; i++){
			scores[i] = players[i].getScore();
		}
		return scores;
	}

}