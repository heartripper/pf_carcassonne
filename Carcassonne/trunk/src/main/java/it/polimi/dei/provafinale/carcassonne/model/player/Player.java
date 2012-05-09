package it.polimi.dei.provafinale.carcassonne.model.player;

public class Player implements Comparable<Player> {

	private static int count = 0;
	private PlayerColor color;
	private int score;
	private int id;
	private int coins;

	/**
	 * Constructor: Create a new instance of Player.
	 */
	public Player() {
		this.score = 0;
		this.id = count++;
		this.color = PlayerColor.valueOf(id);
		this.coins = 7;
	}

	/**
	 * Check if the player has available coins.
	 * 
	 * @return true if the Player has available coins, false instead.
	 */
	public boolean hasCoins() {
		return coins > 0;
	}

	/**
	 * Add one coin to the player's ones.
	 */
	public void addCoin() {
		if (coins < 7)
			coins++;
		return;
	}

	/**
	 * Remove one coin from the player's ones.
	 */
	public void removeCoin() {
		if (coins > 0)
			coins--;
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
		return String.format("%s: %s , %s", color.getFullName(), score, coins);
	}

	/**
	 * Compare the scores of two players.
	 */
	public int compareTo(Player otherPlayer) {
		return (otherPlayer.score - score);
	}

	/**
	 * Return the string representation of Player.
	 */
	@Override
	public String toString() {
		return color.getFullName();
	}
}
