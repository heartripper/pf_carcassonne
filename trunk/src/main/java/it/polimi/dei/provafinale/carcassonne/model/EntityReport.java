package it.polimi.dei.provafinale.carcassonne.model;

/**
 * Creates, when an entity is completed, a report for it calculating the
 * followers to return and the scores for each player.
 * */
public class EntityReport {

	private int[] followers;
	private int[] scores;

	/**
	 * Constructs a new EntityReport for given entity and for a given number of
	 * players.
	 * 
	 * @param entity
	 *            - the entity to create the report from.
	 * @param numPlayers
	 *            - the number of players to generate the report for.
	 * */
	public EntityReport(Entity entity, int numPlayers) {
		int score = entity.getScore();
		this.followers = entity.countFollowers(numPlayers);
		this.scores = new int[numPlayers];

		int max = 0;
		for (int i = 0; i < numPlayers; i++) {
			if (followers[i] > max) {
				max = followers[i];
			}
		}

		for (int i = 0; i < numPlayers; i++) {
			if (max > 0 && followers[i] == max) {
				scores[i] = score;
			} else {
				scores[i] = 0;
			}
		}
	}

	/**
	 * Gives the score per player given by passed entity.
	 * 
	 * @return an array containing score per player sort by player color order.
	 * */
	public int[] getScores() {
		return scores;
	}

	/**
	 * Gives the number of followers per player put on passed entity.
	 * 
	 * @return an array containing followers per player sort by player color.
	 *         order
	 * */
	public int[] getFollowers() {
		return followers;
	}

}
