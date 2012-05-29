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
 * 0, East = 1, South = 2, <west = 3.
 * */
public final class Tile {
	// Reference to container grid
	private TileGrid grid;
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
	 * @param rep
	 *            - a String representing the Tile according to the project
	 *            specification.
	 */
	public Tile(String rep) {
		/* Find the Sides and the SideConnection of the given Tile. */
		sides = new Side[Constants.SIDES_NUMBER];
		connections = new ArrayList<SideConnection>();
		/* rep analysis. */
		String[] descriptors = rep.split(" ");
		for (String desc : descriptors) {
			String split[] = desc.split("=");
			String name = split[0], value = split[1];
			/* This part of rep represents one of the Side. */
			if (name.length() == 1) {
				setSide(name, value);
			}
			/* This part of rep represents one of the SideConnection */
			else {
				setConnection(name, value);
			}
		}
	}

	/* Setting Side options. */
	private void setSide(String name, String value) {
		int posIndex = SidePosition.valueOf(name).getIndex();
		String sideType = null;
		PlayerColor follower = null;
		/* We are not analyzing the Side in presence of the follower. */
		if (value.indexOf('-') == -1) {
			sideType = value;
		}
		/* We are analyzing the Side in presence of the follower. */
		else {
			String[] split = value.split("-");
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

	/* Setting connection options. */
	private void setConnection(String name, String value) {
		/*
		 * Conversion of value in number: there is no connection between two
		 * sides.
		 */
		if (Integer.parseInt(value) == 0) {
			return;
		}
		/*
		 * There is a connection between two sides: setting start and end of
		 * connection.
		 */
		String start = String.valueOf(name.charAt(0));
		String end = String.valueOf(name.charAt(1));
		Side side1 = getSide(SidePosition.valueOf(start));
		Side side2 = getSide(SidePosition.valueOf(end));
		connections.add(new SideConnection(side1, side2));
	}

	/**
	 * Sets the tile on a grid in a specific position.
	 * 
	 * @param grid
	 *            - a Grid where we want to put the tile on.
	 * @param coord
	 *            - a Coord where we want to put the tile.
	 */
	public void setPositionInfo(TileGrid grid, Coord coord) {
		this.grid = grid;
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
	 * Give the SidePosition corresponding to a given Side of a tile.
	 * 
	 * @param side
	 *            - a Side of the Tile.
	 * @return the SidePosition of the given Side of the Tile.
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
	 * Adds a follower of the given PlayerColor to the given SidePosition in
	 * Tile.
	 * 
	 * */
	public void addFollower(SidePosition position, PlayerColor color) {
		Side side = getSide(position);
		side.setFollower(color);
	}

	/**
	 * 
	 * @return the Tile coordinates.
	 */
	public Coord getCoordinates() {
		return tileCoord;
	}

	/**
	 * Gives the Tile opposite to a given SidePosition of the current Tile.
	 * 
	 * @param position
	 *            - a SidePosition of the current Tile.
	 * @return the neighbor Tile.
	 */
	public Tile getNeighbor(SidePosition position) {
		Coord offset;
		/* There is no neighbor. */
		if (grid == null || tileCoord == null) {
			return null;
		}
		/* Finds the neighbor. */
		offset = SidePosition.getOffsetForPosition(position);
		Coord neighborCoord = tileCoord.add(offset);
		return grid.getTile(neighborCoord);
	}

	/**
	 * Provides a list of the Side linked to a given one (side1) in a tile
	 * because of the presence of an entity.
	 * 
	 * @param side1
	 *            - a Side we want to know the related ones.
	 * @return an ArrayList of Side linked to the given one in a Tile.
	 */
	public List<Side> sidesLinkedTo(Side side1) {
		List<Side> linkedSides = new ArrayList<Side>();
		for (Side side2 : this.sides) {
			/* Examination of a couple of sides of the same Tile. */
			if (side1 != side2) {
				/* Create the connection between side1 and side2. */
				SideConnection connection = new SideConnection(side1, side2);
				/* Check if the connection really exists. */
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
	 * @return true if there is an entity thar links start and end in the given
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
		/* If tile has already been placed, it can't be rotated. */
		if (tileCoord != null) {
			return;
		}
		/* Rotation implementation. */
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
		if (linksCacheValid == false) {
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
		/*
		 * Adds the current String to the string builder to create the complete
		 * representation.
		 */
		representation.append(linksCache.get("NS"));
		representation.append(linksCache.get("NE"));
		representation.append(linksCache.get("NW"));
		representation.append(linksCache.get("WE"));
		representation.append(linksCache.get("SE"));
		representation.append(linksCache.get("SW"));
		return representation.toString().trim();
	}
}