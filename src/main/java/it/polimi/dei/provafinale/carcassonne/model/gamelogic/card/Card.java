package it.polimi.dei.provafinale.carcassonne.model.gamelogic.card;

import it.polimi.dei.provafinale.carcassonne.Constants;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.Coord;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.entity.EntityType;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.player.PlayerColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class representing cards. Sides are represented using the convention North =
 * 0, East = 1, South = 2, <west = 3.
 * */
public class Card {
	// Reference to container grid
	private TileGrid grid;
	private Coord cardCoord;

	private Side[] sides;
	private List<SideConnection> connections;
	private Map<String, String> linksCache;
	private boolean linksCacheValid = false;

	private final SidePosition[] representationOrder = { SidePosition.N,
			SidePosition.S, SidePosition.W, SidePosition.E };

	/**
	 * Constructor: create a new Card entity.
	 * 
	 * @param rep
	 *            - a String representing the card according to the project
	 *            specification
	 */
	public Card(String rep) {
		sides = new Side[Constants.SIDES_NUMBER];
		connections = new ArrayList<SideConnection>();

		String[] descriptors = rep.split(" ");
		for (String desc : descriptors) {
			String split[] = desc.split("=");
			String name = split[0], value = split[1];
			if (name.length() == 1) {
				setSide(name, value);
			} else {
				setConnection(name, value);
			}
		}
	}

	private void setSide(String name, String value) {
		int posIndex = SidePosition.valueOf(name).getIndex();
		String sideType = null;
		PlayerColor follower = null;
		if (value.indexOf('-') == -1) {
			sideType = value;
		} else {
			String[] split = value.split("-");
			sideType = split[0];
			follower = PlayerColor.valueOf(split[1]);
		}

		EntityType type = EntityType.valueOf(sideType);
		Side side = new Side(this, type);
		sides[posIndex] = side;
		if (follower != null) {
			side.setFollower(follower);
		}
	}

	private void setConnection(String name, String value) {
		if (Integer.parseInt(value) == 0) {
			return;
		}
		String start = String.valueOf(name.charAt(0));
		String end = String.valueOf(name.charAt(1));

		Side side1 = getSide(SidePosition.valueOf(start));
		Side side2 = getSide(SidePosition.valueOf(end));
		connections.add(new SideConnection(side1, side2));
	}

	/**
	 * Set the card on a grid in a specific position.
	 * 
	 * @param grid
	 *            - a Grid where the card is put on.
	 * @param coord
	 *            - a Coord where the card is collocate.
	 */
	public void setPositionInfo(TileGrid grid, Coord coord) {
		this.grid = grid;
		this.cardCoord = coord;
	}

	/**
	 * Find a side (with its entity type, entity, owner and follower) of a card.
	 * 
	 * @param side
	 *            - a SidePosition of a card.
	 * @return a Side of the card.
	 */
	public Side getSide(SidePosition position) {
		return sides[position.getIndex()];
	}

	/**
	 * Give the side position of a given side of a card.
	 * 
	 * @param side
	 *            - a Side of a Card.
	 * @return the SidePosition of the given Side of the card.
	 */
	public SidePosition getSidePosition(Side side) {
		for (SidePosition position : SidePosition.values()) {
			if (sides[position.getIndex()] == side) {
				return position;
			}
		}
		return null;
	}

	/**
	 * Tries to add a follower to the side corresponding to the given position in
	 * the current card.
	 * 
	 * @return true if the follower was added, false if it couldn't be added.
	 * */
	public void addFollower(SidePosition position, PlayerColor color) {
		Side side = getSide(position);
		side.setFollower(color);
	}

	/**
	 * Gives this card's coordinates.
	 * 
	 * @return this card coordinates.
	 */
	public Coord getCoordinates() {
		return cardCoord;
	}

	/**
	 * Give the card opposite to a given side of a the card.
	 * 
	 * @param position
	 *            - a SidePosition of this card
	 * @return the neighbor Card.
	 */
	public Card getNeighbor(SidePosition position) {
		Coord offset;
		if (grid == null || cardCoord == null) {
			return null;
		}
		offset = SidePosition.getOffsetForPosition(position);

		Coord neighborCoord = cardCoord.add(offset);
		return grid.getTile(neighborCoord);
	}

	/**
	 * Give a list of the sides linked to a given one in a card because of the
	 * presence of an entity.
	 * 
	 * @param side
	 *            - a Side.
	 * @return an ArrayList of Side linked to a given one in the card.
	 */
	public List<Side> sidesLinkedTo(Side side1) {
		List<Side> sides = new ArrayList<Side>();
		for (Side side2 : this.sides) {
			if (side1 != side2) {
				SideConnection connection = new SideConnection(side1, side2);
				if (connections.contains(connection)) {
					sides.add(side2);
				}
			}
		}
		return sides;
	}

	/**
	 * Check the presence of a link between two sides of a given card.
	 * 
	 * @param side1
	 *            - a Side.
	 * @param side2
	 *            - a Side.
	 * @return true if there is an entity that links side1 and side2 of the
	 *         card, false instead.
	 */
	public boolean sideLinked(SidePosition start, SidePosition end) {
		Side side1 = getSide(start);
		Side side2 = getSide(end);
		return connections.contains(new SideConnection(side1, side2));
	}

	/**
	 * Rotate the card of 90ï¿½ clock wise.
	 */
	public void rotate() {
		// If tile has already been placed, it can't be rotated
		if (cardCoord != null) {
			return;
		}

		int i = sides.length - 1;
		Side backup = sides[i];
		for (; i > 0; i--) {
			sides[i] = sides[i - 1];
		}
		sides[0] = backup;
	}

	/**
	 * Gives the string representing this card, according to project
	 * specification.
	 * 
	 * @return the String representing this card.
	 * */
	@Override
	public String toString() {
		StringBuilder representation = new StringBuilder();
		// Sides representation
		for (SidePosition pos : representationOrder) {
			String rep = String.format("%s=%s ", pos, getSideRep(pos));
			representation.append(rep);
		}
		// Connections representation
		if (!linksCacheValid) {
			linksCache = new HashMap<String, String>();
			for (int i = 0; i < representationOrder.length; i++) {
				for (int j = 0; j < representationOrder.length; j++) {
					if (i >= j) {
						continue;
					}
					SidePosition pos1 = representationOrder[i];
					SidePosition pos2 = representationOrder[j];
					SideConnection con = new SideConnection(getSide(pos1),
							getSide(pos2));
					boolean connected = connections.contains(con);
					int val = (connected ? 1 : 0);
					String conn = String.format("%s%s", pos1, pos2);
					String rep = String.format("%s=%s ", conn, val);
					linksCache.put(conn, rep);
				}
			}

		}

		representation.append(linksCache.get("NS"));
		representation.append(linksCache.get("NE"));
		representation.append(linksCache.get("NW"));
		representation.append(linksCache.get("WE"));
		representation.append(linksCache.get("SE"));
		representation.append(linksCache.get("SW"));
		return representation.toString().trim();
	}

	private String getSideRep(SidePosition pos) {
		String rep = getSide(pos).getType().toString();
		PlayerColor follower = getSide(pos).getFollower();
		String col = (follower == null ? null : follower.toString());
		if (col != null) {
			rep += ("-" + col);
		}
		return rep;
	}
}