package it.polimi.dei.provafinale.carcassonne.model.gamelogic.entity;

import it.polimi.dei.provafinale.carcassonne.model.gamelogic.card.Card;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.card.Side;

import java.util.ArrayList;
import java.util.List;

public abstract class Entity {
	public abstract EntityType getType();
	public abstract void addMember(Side side);
	public abstract List<Side> getMembers();
	public abstract boolean isComplete();
	public abstract int getScore();
	public abstract List<Card> removeFollowers();
	public abstract boolean acceptFollowers();
	public abstract int[] countFollowers(int numPlayers);
	public abstract Entity enclose(Entity otherEntity);
}