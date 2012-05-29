package it.polimi.dei.provafinale.carcassonne.model;

import it.polimi.dei.provafinale.carcassonne.Constants;
import it.polimi.dei.provafinale.carcassonne.PlayerColor;

public class Side {

	private EntityType type = null;
	private Entity entity = null;
	private Tile ownerTile = null;
	private PlayerColor follower = null;

	/**
	 * Side constructor. Creates a new instance of class Side (set the owner of
	 * a card which the side belongs to, set the type of an entity which the
	 * side belongs to).
	 * 
	 * @param ownerTile
	 *            - an owner which the Side of Tile belongs to.
	 * @param type
	 *            - an EntityType which the Side belong to.
	 */
	public Side(Tile ownerTile, EntityType type) {
		this.type = type;
		this.entity = null;
		this.ownerTile = ownerTile;
		this.follower = null;
	}

	/**
	 * 
	 * @return the EntityType of Side.
	 */
	public EntityType getType() {
		return type;
	}

	/**
	 * 
	 * @return the Entity of Side.
	 */
	public Entity getEntity() {
		return entity;
	}

	/**
	 * 
	 * @param entity
	 *            - an Entity we want to set on Side.
	 */
	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	/**
	 * 
	 * @return a Tile which the Side belongs to.
	 */
	public Tile getOwnerCard() {
		return ownerTile;
	}

	/**
	 * Gives the side opposite to the current one.
	 * 
	 * @return a Side opposite to the current one.
	 */
	public Side getOppositeSide() {
		/* The Side doesn't belong to a Tile. */
		if (ownerTile == null) {
			return null;
		}
		/* The Side belongs to a Tile. */
		/* Obtains the SidePosition of the current Side. */
		SidePosition position = ownerTile.getSidePosition(this);
		/* Obtains the opposite SidePosition. */
		SidePosition oppositePosition = position.getOpposite();
		/* Obtains the opposite Tile. */
		Tile oppositeTile = ownerTile.getNeighbor(position);
		/* Case there isn't an opposite Tile. */
		if (oppositeTile == null) {
			return null;
		}
		return oppositeTile.getSide(oppositePosition);
	}

	/**
	 * 
	 * @param follower
	 *            the follower we want to put on the current Side
	 */
	public void setFollower(PlayerColor follower) {
		this.follower = follower;
	}

	/**
	 * 
	 * @return the PlayerColor of the follower that is located on Side.
	 */
	public PlayerColor getFollower() {
		return follower;
	}

	@Override
	public String toString() {
		String rep = type.toString();
		if (follower != null) {
			rep += (Constants.FOLLOWER_SEPARATOR + follower.toString());
		}
		return rep;
	}
}
