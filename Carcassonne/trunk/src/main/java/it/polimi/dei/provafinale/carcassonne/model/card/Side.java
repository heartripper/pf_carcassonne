package it.polimi.dei.provafinale.carcassonne.model.card;

import it.polimi.dei.provafinale.carcassonne.model.entity.Entity;
import it.polimi.dei.provafinale.carcassonne.model.entity.EntityType;
import it.polimi.dei.provafinale.carcassonne.model.player.Player;

public class Side {

	private EntityType type;
	private Entity entity;
	private Card ownerCard;
	private Player playerCoin;

	/**
	 * Constructor: set the owner of a card which the side belongs to, set the
	 * type of an entity which the side belongs to.
	 * 
	 * @param ownerCard
	 *            - an owner which the Side of the Card belongs to.
	 * @param type
	 *            - an EntityType which the Side belong to.
	 */
	public Side(Card ownerCard, EntityType type) {
		this.type = type;
		this.entity = null;
		this.ownerCard = ownerCard;
		this.playerCoin = null;
	}

	/**
	 * Give the entity type of a side.
	 * 
	 * @return an EntityType
	 */
	public EntityType getType() {
		return type;
	}

	/**
	 * Give the position of a side of a card.
	 * 
	 * @return the SidePosition of a Card which belongs to ownerCard.
	 */
	public SidePosition getPosition() {
		return ownerCard.getSidePosition(this);
	}

	/**
	 * Give the entity which the side belongs to.
	 * 
	 * @return an entity.
	 */
	public Entity getEntity() {
		return entity;
	}

	/**
	 * Give the owner of a side of a card.
	 * 
	 * @return an ownerCard.
	 */
	public Card getOwnerCard() {
		return ownerCard;
	}

	/**
	 * Set an entity on a side of a card.
	 * 
	 * @param entity
	 *            - an Entity.
	 */
	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	/**
	 * Give the side opposite to the current one.
	 * 
	 * @return a Side.
	 */
	public Side getOppositeSide() {
		if (ownerCard == null)
			return null;
		SidePosition position = getPosition();
		SidePosition oppositePosition = position.getOpposite();
		Card oppositeCard = ownerCard.getNeighbor(position);
		if (oppositeCard == null)
			return null;
		return oppositeCard.getSide(oppositePosition);
	}

	/**
	 * Add a coin to a side.
	 * 
	 * @param player
	 *            - a Player which the Card containing the Side belongs to.
	 * @return true if the Player puts a Coin on the Side, false instead.
	 */
	public boolean setPlayerCoin(Player player) {
		if (entity == null)
			return false;
		this.playerCoin = player;
		return true;
	}

	/**
	 * Give the coin of a player put on a side.
	 * 
	 * @return the playerCoin on a Side.
	 */
	public Player getPlayerCoin() {
		return playerCoin;
	}

	/**
	 * Return the string representation of Side.
	 */
	@Override
	public String toString() {
		String base;
		if (entity == null)
			base = type.getRepresentation();
		else {
			base = entity.toString();
			if (playerCoin != null)
				base += (":" + playerCoin.getSymbol());
		}
		// TODO?: make function pad
		int spaceToFill = 5 - base.length();
		while (spaceToFill > 0) {
			if (spaceToFill % 2 == 0)
				base = base + " ";
			else
				base = " " + base;
			spaceToFill--;
		}
		return base;
	}

}
