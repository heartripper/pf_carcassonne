package it.polimi.dei.provafinale.carcassonne.model.gamelogic.card;

import it.polimi.dei.provafinale.carcassonne.model.gamelogic.Coord;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.entity.Entity;
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

	private Map<SidePosition, Side> sides;
	private boolean[][] links;

	/**
	 * Constructor: create a new Card entity.
	 * 
	 * @param rep
	 *            - a String representing the card according to the project
	 *            specification
	 */
	public Card(String rep) {
		sides = new HashMap<SidePosition, Side>();
		links = new boolean[4][4];

		String[] descriptors = rep.split(" ");
		for (String desc : descriptors) {
			String split[] = desc.split("=");
			String name = split[0], value = split[1];
			if (name.length() == 1) {
				setSide(name, value);
			} else {
				setLink(name, value);
			}
		}
	}

	private void setSide(String name, String value) {
		SidePosition position = SidePosition.valueOf(name);
		String sideType = null;
		PlayerColor follower = null;
		if (value.indexOf("-") == -1) {
			sideType = value;
		} else {
			String[] split = value.split("-");
			sideType = split[0];
			follower = PlayerColor.valueOf(split[1]);
		}

		EntityType type = EntityType.valueOf(sideType);
		Side side = new Side(this, type);
		sides.put(position, side);
		if (follower != null) {
			side.setFollower(follower);
		}
	}

	private void setLink(String name, String value) {
		if (Integer.parseInt(value) == 0) {
			return;
		}
		String start = String.valueOf(name.charAt(0));
		String end = String.valueOf(name.charAt(1));

		SidePosition startPos = SidePosition.valueOf(start);
		SidePosition endPos = SidePosition.valueOf(end);

		int s = startPos.getIndex();
		int e = endPos.getIndex();

		links[s][e] = links[e][s] = true;
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
	 * Find a side (with its entity type, entity, owner and coin) of a card.
	 * 
	 * @param side
	 *            - a SidePosition of a card.
	 * @return a Side of the card.
	 */
	public Side getSide(SidePosition side) {
		return sides.get(side);
	}

	/**
	 * Give the side position of a given side of a card.
	 * 
	 * @param side
	 *            - a Side of a Card.
	 * @return the SidePosition of the given Side of the card.
	 */
	public SidePosition getSidePosition(Side side) {
		for (SidePosition position : sides.keySet()) {
			if (sides.get(position) == side) {
				return position;
			}
		}
		return null;
	}

	/**
	 * Tries to add a coin to the side corresponding to the given position in
	 * the current card.
	 * 
	 * @return true if the coin was added, false if it couldn't be added.
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

	// TODO: Remove this.
	/**
	 * Give a list of the sides of a card that are available to accept coins.
	 * 
	 * @return an ArrayList of SidePosition where is possible to set coins in
	 *         the card.
	 */
	public List<SidePosition> sidesAcceptingCoin() {
		ArrayList<SidePosition> sides = new ArrayList<SidePosition>();
		for (SidePosition position : SidePosition.values()) {
			Entity entity = getSide(position).getEntity();
			if (entity != null && entity.acceptFollowers()) {
				sides.add(position);
			}
		}
		return sides;
	}

	/**
	 * Give a list of the sides linked to a given one in a card because of the
	 * presence of an entity.
	 * 
	 * @param side
	 *            - a Side.
	 * @return an ArrayList of Side linked to a given one in the card.
	 */
	public List<Side> sidesLinkedTo(Side side) {
		ArrayList<Side> positions = new ArrayList<Side>();
		SidePosition startPos = getSidePosition(side);
		for (SidePosition endPos : SidePosition.values()) {
			if (startPos != endPos && sideLinked(startPos, endPos)) {
				positions.add(getSide(endPos));
			}
		}
		return positions;
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
	public boolean sideLinked(SidePosition side1, SidePosition side2) {
		int index1 = side1.getIndex();
		int index2 = side2.getIndex();
		return links[index1][index2];
	}

	/**
	 * Rotate the card of 90ï¿½ clock wise.
	 */
	public void rotate() {
		if (cardCoord != null) {
			return;
		}
		Side tmp1 = sides.get(SidePosition.W), tmp2;
		for (int i = 0; i < 4; i++) {
			SidePosition currentPos = SidePosition.valueOf(i);
			tmp2 = sides.get(currentPos);
			sides.remove(currentPos);
			sides.put(currentPos, tmp1);
			tmp1 = tmp2;
		}
		boolean[] tmp3 = links[3], tmp4;
		boolean tmp5, tmp6;
		for (int i = 0; i < 4; i++) {
			tmp4 = links[i];
			links[i] = tmp3;
			tmp3 = tmp4;
			tmp5 = links[i][3];
			for (int j = 0; j < 4; j++) {
				tmp6 = links[i][j];
				links[i][j] = tmp5;
				tmp5 = tmp6;
			}
		}
	}

	/**
	 * Gives the string representing this card, according to project
	 * specification.
	 * 
	 * @return the String representing this card.
	 * */
	@Override
	public String toString() {
		String representation = "";
		String sideFormat = "N=%s%s S=%s%s W=%s%s E=%s%s ";

		String northSide = getSideRep(SidePosition.N);
		String northPlayer = getFollowerRep(SidePosition.N);
		String southSide = getSideRep(SidePosition.S);
		String southPlayer = getFollowerRep(SidePosition.S);
		String westSide = getSideRep(SidePosition.W);
		String westPlayer = getFollowerRep(SidePosition.W);
		String eastSide = getSideRep(SidePosition.E);
		String eastPlayer = getFollowerRep(SidePosition.E);

		representation += String.format(sideFormat, northSide, northPlayer,
				southSide, southPlayer, westSide, westPlayer, eastSide,
				eastPlayer);

		String linkFormat = "NS=%s NE=%s NW=%s WE=%s SE=%s SW=%s";
		String linkNE = (links[0][1] ? "1" : "0");
		String linkNS = (links[0][2] ? "1" : "0");
		String linkNO = (links[0][3] ? "1" : "0");
		String linkES = (links[1][2] ? "1" : "0");
		String linkEO = (links[1][3] ? "1" : "0");
		String linkSO = (links[2][3] ? "1" : "0");

		representation += String.format(linkFormat, linkNS, linkNE, linkNO,
				linkEO, linkES, linkSO);

		return representation;
	}

	private String getSideRep(SidePosition pos) {
		return getSide(pos).getType().toString();
	}

	private String getFollowerRep(SidePosition pos) {
		PlayerColor col = getSide(pos).getFollower();
		if (col == null) {
			return "";
		} else {
			return "-" + col.toString();
		}
	}
}