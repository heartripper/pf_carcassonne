package it.polimi.dei.provafinale.carcassonne.model.entity;

import it.polimi.dei.provafinale.carcassonne.model.card.Card;
import it.polimi.dei.provafinale.carcassonne.model.card.Side;
import it.polimi.dei.provafinale.carcassonne.model.player.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class City extends Entity {

	private static int cityCount = 0;

	private EntityType type = EntityType.C;
	private ArrayList<Side> members;
	private boolean completed = false;
	private boolean hasCoin = false;
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
	public ArrayList<Side> getMembers() {
		return members;
	}

	/**
	 * Add a city to a city.
	 */
	@Override
	public void addMember(Side side) {
		if (!members.contains(side))
			members.add(side);
	}

	/**
	 * Check if a coin can be put on a city.
	 */
	public boolean acceptCoin() {
		if (hasCoin) {
			return false;
		}
		for (Side s : members) {
			if (s.getPlayerCoin() != null) {
				hasCoin = true;
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
		if (otherEntity.getMembers().size() > this.members.size())
			return otherEntity.enclose(this);
		else {
			for (Side s : otherEntity.getMembers()) {
				s.setEntity(this);
				this.addMember(s);
			}
			if (!otherEntity.acceptCoin()) {
				hasCoin = true;
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
	 * Give the owner/owners of a city.
	 */
	@Override
	public ArrayList<Player> getOwners() {
		Map<Player, Counter> coinCount = new HashMap<Player, Counter>();
		for (Side side : members) {
			Player player = side.getPlayerCoin();
			if (player == null) {
				continue;
			}
			Counter counter;
			if (coinCount.containsKey(player)) {
				counter = coinCount.get(player);
			} else {
				counter = new Counter();
				coinCount.put(player, counter);
			}
			counter.increment();
		}
		ArrayList<Player> owners = new ArrayList<Player>();
		int max = 0;
		for (Counter counter : coinCount.values()) {
			int value = counter.getValue();
			if (value > max) {
				max = value;
			}
		}
		for (Player player : coinCount.keySet()) {
			Counter counter = coinCount.get(player);
			if (counter.getValue() == max) {
				owners.add(player);
			}
		}
		return owners;
	}

	/**
	 * Give the coin/coins of a city to its owner/owners
	 */
	@Override
	public void finalizeEntity() {
		for (Side s : members) {
			Player p = s.getPlayerCoin();
			if (p != null) {
				p.addCoin();
				s.setPlayerCoin(null);
			}
		}
	}

	/**
	 * Give the score given by a city.
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