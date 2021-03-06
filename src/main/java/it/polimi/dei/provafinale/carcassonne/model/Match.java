package it.polimi.dei.provafinale.carcassonne.model;

import it.polimi.dei.provafinale.carcassonne.Coord;
import it.polimi.dei.provafinale.carcassonne.PlayerColor;

import java.util.ArrayList;
import java.util.List;

/**
 * The class Match creates and manages the execution of a match with all its
 * attribute (grid, stack, players, entities, firstTile, playersNumber).
 * 
 */
public class Match {

	private TileGrid grid;
	private TileStack stack;
	private PlayerManager players;
	private List<Entity> entities;
	private Tile firstTile;
	private int playersNumber;

	/**
	 * Match constructor. Creates a new instance of class Match.
	 * 
	 * @param numPlayers
	 *            the number of players.
	 */
	public Match(int numPlayers) {
		this.grid = new TileGrid();
		this.entities = new ArrayList<Entity>();
		this.stack = new TileStack();
		this.players = new PlayerManager(numPlayers);
		this.playersNumber = numPlayers;
		/* Add cards #0 to the grid. */
		firstTile = stack.getInitialTile();
		grid.putTile(firstTile, new Coord(0, 0));
		updateEntities(firstTile);
	}

	/* Tiles methods. */

	/**
	 * 
	 * @return the initial tile.
	 * */
	public Tile getFirstTile() {
		return firstTile;
	}

	/**
	 * Determines if there are still cards in the stack.
	 * 
	 * @return true if there is at least a card in the stack, false instead.
	 */
	public boolean hasMoreCards() {
		return stack.hasMoreTiles();
	}

	/**
	 * Gives a representation of a tile chosen randomly from the stack.
	 * 
	 * @return the chosen tile representation.
	 **/
	public Tile drawTile() {
		Tile drew;
		do {
			drew = stack.drawTile();
		} while (!grid.hasAPlaceFor(drew));

		return drew;
	}

	/**
	 * Tries to put the current tile in the grid at given coordinates.
	 * 
	 * @return true if the tile is correctly added (this means the tile was
	 *         compatible with the position), false otherwise.
	 * */
	public boolean putTile(Tile tile, Coord coord) {
		/* The tile is not compatible with the given coordinates. */
		if (!grid.isTileCompatible(tile, coord)) {
			return false;
		}

		grid.putTile(tile, coord);
		for (SidePosition pos : SidePosition.values()) {
			Side current = tile.getSide(pos);
			Coord offset = SidePosition.getOffsetForPosition(pos);
			Tile neighbor = grid.getTile(coord.add(offset));
			if (neighbor != null) {
				Side opposite = neighbor.getSide(pos.getOpposite());
				current.setOppositeSide(opposite);
				opposite.setOppositeSide(current);
			}
		}
		updateEntities(tile);
		return true;
	}

	/**
	 * Checks if modified entities (the ones tile sides belong to) are now
	 * complete. If an entity is complete finalizes (See Entity.finalize()) it
	 * and give its owners the resulting score.
	 * 
	 * @param tile
	 *            - a Tile that has been added to the grid.
	 */
	public synchronized List<Tile> checkForCompletedEntities(Tile tile) {
		ArrayList<Entity> checkedEntities = new ArrayList<Entity>();
		ArrayList<Tile> updatedTiles = new ArrayList<Tile>();

		for (SidePosition position : SidePosition.values()) {
			Entity entity = tile.getSide(position).getEntity();

			if (entity == null || checkedEntities.contains(entity)) {
				continue;
			}

			checkedEntities.add(entity);
			/* Case an Entity is completed using the current SidePosition. */
			if (entity.isComplete()) {
				List<Tile> currentUpdatedTiles = finalizeEntityAndUpdate(entity);
				updatedTiles.addAll(currentUpdatedTiles);
				entities.remove(entity);
			}
		}
		return updatedTiles;
	}

	/* Players methods. */

	/**
	 * Gives the identificator (color) of the player that has to play the
	 * current turn in the match.
	 * 
	 * @return the PlayerColor of the player that has to play the turn.
	 */
	public PlayerColor getNextPlayer() {
		return players.getNext().getColor();
	}

	/**
	 * Adds a follower of a color on a coordinate of a tile. If this operation
	 * succeeds removes a follower from the list of followers belonging to
	 * player represented by color.
	 * 
	 * @param tile
	 *            the tile to add the follower on.
	 * @param position
	 *            the position to put the follower at.
	 * @param color
	 *            the tile to add the follower on.
	 * @return true if the follower is correctly put on the specified position,
	 *         false otherwise.
	 */
	public boolean putFollower(Tile tile, SidePosition position,
			PlayerColor color) {
		/* Check that we can put the follower in given position. */
		Entity e = tile.getSide(position).getEntity();
		if (e == null || !e.acceptFollowers()) {
			return false;
		}
		/* Check that given player has available followers. */
		Player p = players.getByColor(color);
		if (!p.hasFollowers()) {
			return false;
		}
		/* Add follower on tile and remove it from player. */
		tile.getSide(position).setFollower(color);
		p.removeFollower();
		return true;
	}

	/**
	 * When the game is finished, gives the score corresponding to each
	 * incomplete entity to his owner/owners.
	 */
	public synchronized void finalizeMatch() {
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			finalizeEntityAndUpdate(e);
		}
	}

	/**
	 * Gives the game classification.
	 * 
	 * @return an array of scores.
	 */
	public int[] getScores() {
		return players.getScores();
	}

	/**
	 * Removes a player from the match (removes him from player list and removes
	 * all his followers).
	 * 
	 * @param color
	 *            the color of the player we want to remove.
	 * @return the list of the updated tile on the grid.
	 * @throws NotEnoughPlayersException
	 */
	public synchronized List<Tile> removePlayer(PlayerColor color)
			throws NotEnoughPlayersException {
		Player p = players.getByColor(color);
		p.setInactive();
		/* There are no enough players to play the game. */
		if (players.getPlayerNumber() < 2) {
			throw new NotEnoughPlayersException();
		}
		
		List<Tile> updates = new ArrayList<Tile>();
		for (Entity e : entities) {
			updates.addAll(e.removeFollowers(color));
		}
		
		return updates;
	}

	/* Private Methods. */

	/**
	 * Updates entities.
	 * 
	 * @param tile
	 *            the tile we have just added on the grid.
	 */
	private void updateEntities(Tile tile) {
		for (SidePosition position : SidePosition.values()) {
			Side currentSide = tile.getSide(position);
			Side oppositeSide = currentSide.getOppositeSide();
			Entity currentEntity = currentSide.getEntity();
			EntityType type = currentSide.getType();
			Entity sideEntity;
			/* The current side doesn't belong to an entity. */
			if (currentEntity == null) {
				/* There is no a side opposite to the current one. */
				if (oppositeSide == null) {
					/* Creation of a new entity. */
					sideEntity = Entity.createByType(type);
				}
				/* There is a side opposite to the current one. */
				else {
					/*
					 * The current side entity belongs to the same entity of the
					 * opposite side.
					 */
					sideEntity = oppositeSide.getEntity();
				}
				/* The current side is added to the entity. */
				addSideToEntity(currentSide, sideEntity);
				for (Side side : tile.sidesLinkedTo(currentSide)) {
					addSideToEntity(side, sideEntity);
				}
			}
			/* The current side belongs to an entity. */
			else {
				if (oppositeSide == null) {
					continue;
				}
				Entity oppositeEntity = oppositeSide.getEntity();
				if (oppositeEntity == null) {
					continue;
				}
				/* Current side inclusion to an entity. */
				sideEntity = oppositeEntity.enclose(currentEntity);
				if (entities.contains(currentEntity)
						&& !currentEntity.equals(sideEntity)) {
					entities.remove(currentEntity);
				}
				if (entities.contains(oppositeEntity)
						&& !oppositeEntity.equals(sideEntity)) {
					entities.remove(oppositeEntity);
				}
			}
			/* Addition of current side entity. */
			if (sideEntity != null && !entities.contains(sideEntity)) {
				entities.add(sideEntity);
			}
		}
	}

	/**
	 * Adds a side to an entity.
	 * 
	 * @param side
	 *            a side we want to add to an entity.
	 * @param entity
	 *            an entity we want to set a side belongs to.
	 */
	private void addSideToEntity(Side side, Entity entity) {
		side.setEntity(entity);
		if (entity != null) {
			entity.addMember(side);
		}
	}

	/**
	 * Handles a complete entity.
	 * 
	 * @param entity
	 *            the complete entity.
	 * @return the list of the grid updated tiles.
	 */
	private List<Tile> finalizeEntityAndUpdate(Entity entity) {
		EntityReport er = new EntityReport(entity, playersNumber);

		/* Give corresponding score to entity owners. */
		players.addScores(er.getScores());

		/* Return followers to owners. */
		players.addFollowers(er.getFollowers());

		return entity.removeFollowers(null);
	}
}