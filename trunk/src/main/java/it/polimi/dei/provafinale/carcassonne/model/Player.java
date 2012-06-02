package it.polimi.dei.provafinale.carcassonne.model;

import it.polimi.dei.provafinale.carcassonne.PlayerColor;

/**
 * The class Player deals with the management of a player (color, score,
 * followers, active).
 * 
 */
public class Player {

	private static final int MAXIMUM_FOLLOWERS_NUMBER = 7;
	
	private PlayerColor color;
	private int score;
	private int followers;
	private boolean active = true;

	/**
	 * Player constructor. Creates a new instance of class Player.
	 */
	public Player() {
		this.score = 0;
		this.followers = MAXIMUM_FOLLOWERS_NUMBER;
	}

	/**
	 * Checks if the Player has available followers.
	 * 
	 * @return true if the Player has available followers, false instead.
	 */
	public boolean hasFollowers() {
		return followers > 0;
	}

	/**
	 * Adds one follower to the player's ones.
	 */
	public void addFollowers(int amount) {
		followers += amount;
		/* Error in followers number. */
		if (followers > MAXIMUM_FOLLOWERS_NUMBER) {
			System.out.printf(
					"Error in followers for player %s. It has %s followers.\n",
					toString(), followers);
		}
		return;
	}

	/**
	 * Removes one follower from the player's ones.
	 */
	public void removeFollower() {
		if (followers > 0) {
			followers--;
		}
	}

	/**
	 * Increments the score of a Player of a given value.
	 * 
	 * @param increment
	 *            - a number to add to the current score of the player.
	 */
	public void addScore(int increment) {
		score += increment;
	}

	/**
	 * Gives the score of a player.
	 * 
	 * @return the score of the player.
	 */
	public int getScore() {
		return score;
	}

	/**
	 * Sets player's color.
	 * 
	 * @param color
	 *            - a player's color
	 * */
	public void setColor(PlayerColor color) {
		this.color = color;
	}

	/**
	 * 
	 * @return player color.
	 * */
	public PlayerColor getColor() {
		return color;
	}

	/**
	 * Give player's symbol, which is the first letter of his color.
	 * 
	 * @return the String representation of the player color.
	 */
	public String getSymbol() {
		return color.toString();
	}

	@Override
	public String toString() {
		return color.getFullName();
	}

	/**
	 * 
	 * @return true if the Player is active, false instead.
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Sets the Player status inactive.
	 */
	public void setInactive() {
		active = false;
	}
}