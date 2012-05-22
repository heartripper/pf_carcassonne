package it.polimi.dei.provafinale.carcassonne.model.gamelogic.entity;

import it.polimi.dei.provafinale.carcassonne.model.gamelogic.card.Card;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.card.Side;

import java.util.ArrayList;

public abstract class Entity {
	public abstract EntityType getType();
	public abstract void addMember(Side side);
	public abstract ArrayList<Side> getMembers();
	public abstract boolean isComplete();
	public abstract int getScore();
	public abstract ArrayList<Card> removeFollowers();
	public abstract boolean acceptFollowers();
	public abstract int[] countFollowers(int numPlayers);
	public abstract Entity enclose(Entity otherEntity);
}
