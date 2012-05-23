package it.polimi.dei.provafinale.carcassonne.model.gamelogic.player;

import it.polimi.dei.provafinale.carcassonne.model.gamelogic.player.PlayerColor;

public class Player {

	private static int count = 0;
	private PlayerColor color;
	private int score;
	private int id;
	private int followers;
	private boolean active = true;

	/**
	 * Constructor: Create a new instance of Player.
	 */
	public Player() {
		this.score = 0;
		this.id = count++;
		this.color = PlayerColor.valueOf(id);
		this.followers = 7;
	}

	/**
	 * Check if the player has available coins.
	 * 
	 * @return true if the Player has available coins, false instead.
	 */
	public boolean hasFollowers() {
		return followers > 0;
	}

	/**
	 * Add one coin to the player's ones.
	 */
	public void addFollowers(int amount) {
		followers += amount;
		
		if (followers > 7){
			System.out.printf("Error in followers for player %s. It has %s followers.\n", toString(), followers);	
		}
		return;
	}

	/**
	 * Remove one coin from the player's ones.
	 */
	public void removeFollower() {
		if (followers > 0){
			followers--;
		}
	}

	/**
	 * Increment the score of a player.
	 * 
	 * @param increment
	 *            - an int to add to the score of the player.
	 */
	public void addScore(int increment) {
		score += increment;
	}

	/**
	 * Give the score of a player.
	 * 
	 * @return the score of the player.
	 */
	public int getScore() {
		return score;
	}

	/**
	 * Give player's symbol, which is the first letter of his color.
	 * 
	 * @return the color of the player.
	 */
	public String getSymbol() {
		return color.toString();
	}

	/**
	 * Gives the player's color
	 * 
	 * @return the player's color
	 * */
	public PlayerColor getColor() {
		return color;
	}

	/**
	 * Give an overview of the status of a player.
	 * 
	 * @return the color, the score and the available coins of the player.
	 */
	public String getStatus() {
		return String.format("%s: %s , %s", color.getFullName(), score, followers);
	}

	/**
	 * Return the string representation of Player.
	 */
	@Override
	public String toString() {
		return color.getFullName();
	}
	
	public boolean isActive(){
		return active;
	}
	
	public void setInactive(){
		active = false;
	}
}