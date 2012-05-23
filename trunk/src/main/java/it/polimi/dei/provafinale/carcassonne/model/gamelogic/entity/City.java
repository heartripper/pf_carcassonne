package it.polimi.dei.provafinale.carcassonne.model.gamelogic.entity;

import it.polimi.dei.provafinale.carcassonne.model.gamelogic.card.Card;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.card.Side;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.player.PlayerColor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class City extends Entity {

	private static int cityCount = 0;

	private EntityType type = EntityType.C;
	private List<Side> members;
	private boolean completed = false;
	private boolean hasFollower = false;
	private int id;

	/**
	 * Constructor: Create a new instance of City.
	 */
	public City() {
		id = ++cityCount;
		members = new ArrayList<Side>();
	}

	/**
	 * Return that the entity in consideration is a city.
	 */
	@Override
	public EntityType getType() {
		return type;
	}

	/**
	 * Obtain the side/sides of a city.
	 */
	@Override
	public List<Side> getMembers() {
		return members;
	}

	/**
	 * Add a city to a city.
	 */
	@Override
	public void addMember(Side side) {
		if (!members.contains(side)){
			members.add(side);
		}
	}

	/**
	 * Check if a coin can be put on a city.
	 */
	public boolean acceptFollowers() {
		if (hasFollower) {
			return false;
		}
		for (Side s : members) {
			if (s.getFollower() != null) {
				hasFollower = true;
				return false;
			}
		}
		return true;
	}

	/**
	 * Merge the given city into the previous one.
	 */
	@Override
	public Entity enclose(Entity otherEntity) {
		if (otherEntity.getMembers().size() > this.members.size()){
			return otherEntity.enclose(this);
		}else{
			for (Side s : otherEntity.getMembers()) {
				s.setEntity(this);
				this.addMember(s);
			}
			if (!otherEntity.acceptFollowers()) {
				hasFollower = true;
			}
			return this;
		}
	}

	/**
	 * Check if a city is completed.
	 */
	@Override
	public boolean isComplete() {
		if (completed) {
			return true;
		}
		Set<Side> dependencies = new HashSet<Side>();
		for (Side s : members) {
			if (dependencies.contains(s)) {
				dependencies.remove(s);
			} else {
				Side oppositeSide = s.getOppositeSide();
				if (oppositeSide == null) {
					return false;
				} else {
					dependencies.add(oppositeSide);
				}
			}
		}
		if (dependencies.size() == 0) {
			completed = true;
			return true;
		}
		return false;
	}
	
	/**
	 * Counts followers placed on an entity.
	 * @return an array in which counters are placed in players' order
	 * */
	public int[] countFollowers(int numPlayers){
		int[] counter = new int[numPlayers];
		
		for(int i = 0; i < numPlayers; i++){
			counter[i] = 0;
		}
		for(Side s : members){
			PlayerColor follower = s.getFollower();
			if(follower == null){
				continue;
			}
			int index = PlayerColor.indexOf(follower);
			counter[index]++;
		}
		
		return counter;
	}

	/**
	 * Removes followers from entity;
	 */
	@Override
	public List<Card> removeFollowers() {
		ArrayList<Card> updatedCards = new ArrayList<Card>();
		for (Side s : members) {
			if(s.getFollower() != null){
				s.setFollower(null);
				Card c = s.getOwnerCard();
				if(!updatedCards.contains(c)){
					updatedCards.add(c);
				}
			}
		}
		return updatedCards;
	}

	/**
	 * Calculates the score given by this city.
	 * 
	 * @return the score
	 */
	@Override
	public int getScore() {
		Set<Card> cards = new HashSet<Card>();
		for (Side s : members) {
			Card c = s.getOwnerCard();
			if (!cards.contains(c)) {
				cards.add(c);
			}
		}
		int scorePerCard = (completed ? 2 : 1);
		return scorePerCard * cards.size();
	}

	/**
	 * Return the string representation of City.
	 */
	@Override
	public String toString() {
		return type.getRepresentation() + id;
	}
}