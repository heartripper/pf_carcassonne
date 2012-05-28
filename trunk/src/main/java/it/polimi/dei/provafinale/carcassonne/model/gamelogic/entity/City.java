package it.polimi.dei.provafinale.carcassonne.model.gamelogic.entity;

import it.polimi.dei.provafinale.carcassonne.model.gamelogic.tile.Side;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.tile.Tile;

import java.util.HashSet;
import java.util.Set;

public class City extends Entity {

	private EntityType type = EntityType.C;

	/**
	 * Constructor: Create a new instance of City.
	 */
	public City() {
		super();
	}

	/**
	 * Return that the entity in consideration is a city.
	 */
	@Override
	public EntityType getType() {
		return type;
	}

	/**
	 * Calculates the score given by this city.
	 * 
	 * @return the score
	 */
	@Override
	public int getScore() {
		Set<Tile> cards = new HashSet<Tile>();
		for (Side s : getMembers()) {
			Tile c = s.getOwnerCard();
			if (!cards.contains(c)) {
				cards.add(c);
			}
		}
		int scorePerCard = (isComplete() ? 2 : 1);
		return scorePerCard * cards.size();
	}
}