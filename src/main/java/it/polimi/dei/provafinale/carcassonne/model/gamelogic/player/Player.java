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
	 * Player constructor. Creates a new instance of class Player.
	 */
	public Player() {
		this.score = 0;
		this.id = count++;
		this.color = PlayerColor.valueOf(id);
		this.followers = 7;
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
		if (followers > 7) {
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
	 * Give player's symbol, which is the first letter of his color.
	 * 
	 * @return the String representation of the player color.
	 */
	public String getSymbol() {
		return color.toString();
	}

	/**
	 * 
	 * @return player color.
	 * */
	public PlayerColor getColor() {
		return color;
	}

	/**
	 * Gives an overview of the status of a Player (color, score, number of
	 * followers).
	 * 
	 * @return the String representation of color, score and available followers
	 *         of the Player.
	 */
	public String getStatus() {
		return String.format("%s: %s , %s", color.getFullName(), score,
				followers);
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