package it.polimi.dei.provafinale.carcassonne.model;

import it.polimi.dei.provafinale.carcassonne.Constants;
import it.polimi.dei.provafinale.carcassonne.Coord;
import it.polimi.dei.provafinale.carcassonne.PlayerColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class representing cards. Sides are represented using the convention North =
 * 0, East = 1, South = 2, West = 3.
 * */
public final class Tile {

	/* Reference to container grid. */
	private Coord tileCoord;
	private Side[] sides;
	private List<SideConnection> connections;
	private Map<String, String> linksCache;
	private boolean linksCacheValid = false;

	private final SidePosition[] representationOrder = { SidePosition.N,
			SidePosition.S, SidePosition.W, SidePosition.E };

	/**
	 * Tile constructor. Creates a new entity of class Tile.
	 * 
	 * @param representation
	 *            - a String representing the Tile according to the project
	 *            specification.
	 */
	public Tile(String representation) {

		sides = new Side[Constants.SIDES_NUMBER];
		connections = new ArrayList<SideConnection>();

		String[] descriptors = representation.split(" ");
		for (String desc : descriptors) {
			String split[] = desc.split("=");
			String name = split[0], value = split[1];
			/* This part of representation represents one of the Side. */
			if (name.length() == 1) {
				setSide(name, value);
			}
			/* This part of representation represents one of the SideConnection */
			else {
				setConnection(name, value);
			}
		}
	}

	/**
	 * Sets Side options.
	 * 
	 * @param name
	 *            a String that represents a name of a parameter.
	 * @param value
	 *            a String that represents the value of a parameter.
	 */
	private void setSide(String name, String value) {
		int posIndex = SidePosition.valueOf(name).getIndex();
		String sideType = null;
		PlayerColor follower = null;
		/* A follower is NOT present on this Side. */
		if (value.indexOf('+') == -1) {
			sideType = value;
		}
		/* A follower is present on this Side. */
		else {
			String[] split = value.split("\\+");
			sideType = split[0];
			follower = PlayerColor.valueOf(split[1]);
		}
		/* Setting Side parameters. */
		EntityType type = EntityType.valueOf(sideType);
		Side side = new Side(this, type);
		sides[posIndex] = side;
		if (follower != null) {
			side.setFollower(follower);
		}
	}

	/**
	 * Sets connection options.
	 * 
	 * @param name
	 *            a String that represents the name of an option.
	 * @param value
	 *            a String that represents the value of an option.
	 */
	private void setConnection(String name, String value) {
		/*
		 * If value is equals to zero there isn't a connection.
		 */
		if (Integer.parseInt(value) == 0) {
			return;
		}

		String start = String.valueOf(name.charAt(0));
		String end = String.valueOf(name.charAt(1));
		Side startSide = getSide(SidePosition.valueOf(start));
		Side endSide = getSide(SidePosition.valueOf(end));
		connections.add(new SideConnection(startSide, endSide));
	}

	/**
	 * Sets the tile on a grid in a specific position.
	 * 
	 * @param grid
	 *            - a Grid where we want to put the tile on.
	 * @param coord
	 *            - a Coord where we want to put the tile.
	 */
	public void setCoords(Coord coord) {
		this.tileCoord = coord;
	}

	/**
	 * Find a side (with its entity type, entity, owner and follower) of a tile.
	 * 
	 * @param side
	 *            - a SidePosition of the Tile.
	 * @return a Side of the tile.
	 */
	public Side getSide(SidePosition position) {
		return sides[position.getIndex()];
	}

	/**
	 * 
	 * @return the Tile coordinates.
	 */
	public Coord getCoords() {
		return tileCoord;
	}

	/**
	 * Provides a list of the Side linked to a given one (side1) in a tile
	 * because of the presence of an entity.
	 * 
	 * @param side
	 *            - a Side we want to know the related ones.
	 * @return an ArrayList of Side linked to the given one in a Tile.
	 */
	public List<Side> sidesLinkedTo(Side side) {
		List<Side> linkedSides = new ArrayList<Side>();
		for (Side side2 : this.sides) {
			if (side != side2) {
				/*
				 * Create the connection between side1 and side2 and than checks
				 * if it contained in the connections.
				 */
				SideConnection connection = new SideConnection(side, side2);
				if (connections.contains(connection)) {
					linkedSides.add(side2);
				}
			}
		}
		return linkedSides;
	}

	/**
	 * Checks the presence of a link between two sides of a given card.
	 * 
	 * @param start
	 *            a SidePosition.
	 * @param end
	 *            a SidePosition.
	 * @return true if there is an entity that links start and end in the given
	 *         Tile.
	 */
	public boolean sideLinked(SidePosition start, SidePosition end) {
		Side side1 = getSide(start);
		Side side2 = getSide(end);
		return connections.contains(new SideConnection(side1, side2));
	}

	/**
	 * Rotate the Tile of 90 degrees clock wise.
	 */
	public void rotate() {
		/* Tile already placed can't be rotated. */
		if (tileCoord != null) {
			return;
		}

		int i = sides.length - 1;
		Side backup = sides[i];
		for (; i > 0; i--) {
			sides[i] = sides[i - 1];
		}
		sides[0] = backup;
	}

	@Override
	public String toString() {
		StringBuilder representation = new StringBuilder();
		/* Sides representation. */
		for (SidePosition pos : representationOrder) {
			String rep = String.format("%s=%s ", pos, getSide(pos).toString());
			representation.append(rep);
		}
		/* Connections representation. */
		/* Case the tile has been rotated. */
		if (!linksCacheValid) {
			linksCache = new HashMap<String, String>();
			for (int i = 0; i < representationOrder.length; i++) {
				for (int j = 0; j < representationOrder.length; j++) {
					/* Not necessary to confront two times. */
					if (i >= j) {
						continue;
					}
					/* New SidePosition assignment. */
					SidePosition pos1 = representationOrder[i];
					SidePosition pos2 = representationOrder[j];
					SideConnection con = new SideConnection(getSide(pos1),
							getSide(pos2));
					/* Setting connections. */
					boolean connected = connections.contains(con);
					int val = (connected ? 1 : 0);
					String conn = String.format("%s%s", pos1, pos2);
					String rep = String.format("%s=%s ", conn, val);
					linksCache.put(conn, rep);
				}
			}

		}
		/* Complete representation creation. */
		representation.append(linksCache.get("NS"));
		representation.append(linksCache.get("NE"));
		representation.append(linksCache.get("NW"));
		representation.append(linksCache.get("WE"));
		representation.append(linksCache.get("SE"));
		representation.append(linksCache.get("SW"));
		return representation.toString().trim();
	}
}