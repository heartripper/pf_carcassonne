package it.polimi.dei.provafinale.carcassonne.model.entity;

import it.polimi.dei.provafinale.carcassonne.model.card.Side;
import it.polimi.dei.provafinale.carcassonne.model.player.Player;

import java.util.ArrayList;

public abstract class Entity {
	public abstract EntityType getType();
	public abstract void addMember(Side side);
	public abstract ArrayList<Side> getMembers();
	public abstract boolean isComplete();
	public abstract int getScore();
	public abstract void finalizeEntity();
	public abstract ArrayList<Player> getOwners();
	public abstract Entity enclose(Entity otherEntity);
	public abstract boolean acceptCoin();
}
