package it.polimi.dei.provafinale.carcassonne.model.entity;

import it.polimi.dei.provafinale.carcassonne.model.card.Card;
import it.polimi.dei.provafinale.carcassonne.model.card.Side;
import it.polimi.dei.provafinale.carcassonne.model.player.PlayerColor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Road extends Entity {

	private static int roadCount = 0;

	private EntityType type = EntityType.S;
	private ArrayList<Side> members;
	private boolean completed = false;
	private boolean hasFollowers = false;
	private int id;

	public Road() {
		id = ++roadCount;
		members = new ArrayList<Side>();
	}

	@Override
	public EntityType getType() {
		return type;
	}

	@Override
	public boolean isComplete() {
		if (completed)
			return true;

		for (Side m : members)
			if (m.getOppositeSide() == null)
				return false;

		completed = true;
		return true;
	}

	@Override
	public void addMember(Side side) {
		if (!members.contains(side))
			members.add(side);
	}

	@Override
	public int getScore() {
		Set<Card> cards = new HashSet<Card>();
		for (Side s : members) {
			Card card = s.getOwnerCard();
			if (!cards.contains(card))
				cards.add(card);
		}
		return cards.size();
	}

	@Override
	public Entity enclose(Entity otherEntity) {
		if (otherEntity.getMembers().size() > this.members.size())
			return otherEntity.enclose(this);
		else {
			for (Side s : otherEntity.getMembers()) {
				s.setEntity(this);
				this.addMember(s);
			}

			if (!otherEntity.acceptFollowers())
				hasFollowers = true;

			return this;
		}
	}

	@Override
	public ArrayList<Side> getMembers() {
		return members;
	}

	@Override
	public String toString() {
		return type.getRepresentation() + id;
	}

	@Override
	public boolean acceptFollowers() {
		if (hasFollowers)
			return false;

		for (Side s : members) {
			if (s.getFollower() != null) {
				hasFollowers = true;
				return false;
			}
		}

		return true;
	}

	@Override
	public ArrayList<Card> removeFollowers() {
		ArrayList<Card> updatedCards = new ArrayList<Card>();
		for (Side s : members) {
			if(s.getFollower() != null){
				s.setFollower(null);
				Card c = s.getOwnerCard();
				if(!updatedCards.contains(c))
					updatedCards.add(c);
			}
		}
		return updatedCards;
	}

	@Override
	public int[] countFollowers(int numPlayers) {
		int[] counter = new int[numPlayers];

		for (int i = 0; i < numPlayers; i++) {
			counter[i] = 0;
		}
		
		for(Side s : members){
			PlayerColor follower = s.getFollower();
			if(follower != null){
				int index = PlayerColor.indexOf(follower);
				counter[index]++;
			}
		}
		return counter;
	}
}
