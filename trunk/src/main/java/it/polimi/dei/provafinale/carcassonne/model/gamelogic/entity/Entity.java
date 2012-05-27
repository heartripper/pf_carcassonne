package it.polimi.dei.provafinale.carcassonne.model.gamelogic.entity;

import it.polimi.dei.provafinale.carcassonne.model.gamelogic.tile.Side;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.tile.Tile;

import java.util.List;

public abstract class Entity {
	public abstract EntityType getType();
	public abstract void addMember(Side side);
	public abstract List<Side> getMembers();
	public abstract boolean isComplete();
	public abstract int getScore();
	public abstract List<Tile> removeFollowers();
	public abstract boolean acceptFollowers();
	public abstract int[] countFollowers(int numPlayers);
	public abstract Entity enclose(Entity otherEntity);
}
