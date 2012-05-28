package it.polimi.dei.provafinale.carcassonne.controller;

import it.polimi.dei.provafinale.carcassonne.model.Entity;
import it.polimi.dei.provafinale.carcassonne.model.EntityType;
import it.polimi.dei.provafinale.carcassonne.model.Side;
import it.polimi.dei.provafinale.carcassonne.model.Tile;

import java.util.HashSet;
import java.util.Set;

public class Road extends Entity {

	private static int roadCount = 0;

	private EntityType type = EntityType.S;
	private int id;

	public Road() {
		id = ++roadCount;
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
			if (!tiles.contains(card)){
				tiles.add(card);
			}
		}
		return tiles.size();
	}
	
	@Override
	public String toString() {
		return type.getRepresentation() + id;
	}
}