package it.polimi.dei.provafinale.carcassonne.model.gamelogic.card;

import it.polimi.dei.provafinale.carcassonne.model.gamelogic.entity.Entity;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.entity.EntityType;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.player.PlayerColor;

public class Side {

	private EntityType type = null;
	private Entity entity = null;
	private Tile ownerCard = null;
	private PlayerColor follower = null;

	/**
	 * Constructor: set the owner of a card which the side belongs to, set the
	 * type of an entity which the side belongs to.
	 * 
	 * @param ownerCard
	 *            - an owner which the Side of the Card belongs to.
	 * @param type
	 *            - an EntityType which the Side belong to.
	 */
	public Side(Tile ownerCard, EntityType type) {
		this.type = type;
		this.entity = null;
		this.ownerCard = ownerCard;
		this.follower = null;
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
	 * Give the entity which the side belongs to.
	 * 
	 * @return an entity.
	 */
	public Entity getEntity() {
		return entity;
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
	 * Give the owner of a side of a card.
	 * 
	 * @return an ownerCard.
	 */
	public Tile getOwnerCard() {
		return ownerCard;
	}

	/**
	 * Give the side opposite to the current one.
	 * 
	 * @return a Side.
	 */
	public Side getOppositeSide() {
		if (ownerCard == null) {
			return null;
		}
		SidePosition position = ownerCard.getSidePosition(this);
		SidePosition oppositePosition = position.getOpposite();
		Tile oppositeCard = ownerCard.getNeighbor(position);
		if (oppositeCard == null) {
			return null;
		}
		return oppositeCard.getSide(oppositePosition);
	}

	/**
	 * Add a follower to a side.
	 * 
	 * @param player
	 *            - a Player which the Card containing the Side belongs to.
	 * @return true if the Player puts a follower on the Side, false instead.
	 */
	public void setFollower(PlayerColor follower) {
		this.follower = follower;
	}

	/**
	 * Give the color of the player who put a follower on this side.
	 * 
	 * @return the PlayerColor of the player.
	 */
	public PlayerColor getFollower() {
		return follower;
	}

	/**
	 * Return the string representation of Side.
	 */
	@Override
	public String toString() {
		String rep = (type == EntityType.N ? " " : type.toString());
		if (follower == null) {
			return String.format(" %s ", rep);
		} else {
			return String.format("%s:%s", rep, follower.toString());
		}
	}

}
