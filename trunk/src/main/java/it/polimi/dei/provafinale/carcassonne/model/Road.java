package it.polimi.dei.provafinale.carcassonne.model;

import java.util.HashSet;
import java.util.Set;

/**
 * The class Road extends Entity and represents one of the possible part the
 * tiles in Carcassonne game.
 * 
 */
public class Road extends Entity {        

	private EntityType type = EntityType.S;

	/**
	 * Road constructor. Creates a new instance of class Road.
	 */
	public Road() {
		super();
	}

	@Override
	public EntityType getType() {
		return type;
	}

	@Override
	public int getScore() {
		Set<Tile> tiles = new HashSet<Tile>();
		for (Side s : getMembers()) {
			Tile card = s.getOwnerCard();
			if (!tiles.contains(card)) {
				tiles.add(card);
			}
		}
		return tiles.size();
	}

}