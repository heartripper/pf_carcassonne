package it.polimi.dei.provafinale.carcassonne.model.entity;

import it.polimi.dei.provafinale.carcassonne.model.card.Card;
import it.polimi.dei.provafinale.carcassonne.model.card.Side;
import it.polimi.dei.provafinale.carcassonne.model.player.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;



public class Road extends Entity {

	private static int roadCount=0;
	
	private EntityType type= EntityType.S; 
	private ArrayList<Side> members;
	private boolean completed = false;
	private boolean hasCoin = false;
	private int id;
	
	public Road(){
		id = ++roadCount;
		members = new ArrayList<Side>();
	}
	
	@Override
	public EntityType getType() {
		return type;
	}
	
	@Override
	public boolean isComplete() {
		if(completed)
			return true;
		
		for (Side m : members)
			if(m.getOppositeSide() == null)
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
		for (Side s : members){
			Card card = s.getOwnerCard();
			if(!cards.contains(card))
				cards.add(card);
		}
		return cards.size();
	}

	@Override
	public Entity enclose(Entity otherEntity) {
		if(otherEntity.getMembers().size() > this.members.size())
			return otherEntity.enclose(this);
		else{
			for (Side s : otherEntity.getMembers()) {
				s.setEntity(this);
				this.addMember(s);
			}
		
			if(!otherEntity.acceptCoin())
				hasCoin = true;
			
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
	public boolean acceptCoin() {
		if(hasCoin)
			return false;
		
		for(Side s : members){
			if(s.getPlayerCoin() != null){
				hasCoin = true;
				return false;
			}
		}
		
		return true;
	}

	@Override
	public ArrayList<Player> getOwners() {
		Map<Player, Counter> coinCount = new HashMap<Player, Counter>();
		
		for(Side side : members){
			Player player = side.getPlayerCoin();
			if(player == null)
				continue;
			
			Counter counter;
			if(coinCount.containsKey(player))
				counter = coinCount.get(player);
			else{
				counter = new Counter();
				coinCount.put(player, counter);
			}
			
			counter.increment();
		}
		
		ArrayList<Player> owners = new ArrayList<Player>();
		int max = 0;
		
		for(Counter counter : coinCount.values()){
			int value = counter.getValue();
			if( value > max)
				max = value;
		}
			
		for(Player player : coinCount.keySet()){
			Counter counter = coinCount.get(player);
			if(counter.getValue() == max)
				owners.add(player);
		}

		return owners;
	}

	@Override
	public void finalizeEntity() {
		for(Side s : members){
			Player p = s.getPlayerCoin();
			if(p != null){
				p.addCoin();
				s.setPlayerCoin(null);
			}
		}
	}
}
