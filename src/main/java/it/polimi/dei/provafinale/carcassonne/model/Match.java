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
	private PlayerCircularArray players;
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
		this.players = new PlayerCircularArray(numPlayers);
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
		/* The tile is compatible with the given coordinates. */
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
	public List<Tile> checkForCompletedEntities(Tile tile) {
		ArrayList<Entity> checkedEntities = new ArrayList<Entity>();
		ArrayList<Tile> updatedTiles = new ArrayList<Tile>();

		for (SidePosition position : SidePosition.values()) {
			Entity entity = tile.getSide(position).getEntity();
			/*
			 * There are no Entity on the current SidePosition or the Entity of
			 * the side is already included in checkedEntities.
			 */
			if (entity == null || checkedEntities.contains(entity)) {
				continue;
			}
			/* The Entity of the current SidePosition is a new one. */
			checkedEntities.add(entity);
			/* Case an Entity is completed using the current SidePosition. */
			if (entity.isComplete()) {
				List<Tile> currentUpdatedTiles = finalizeEntityAndUpdate(entity);
				updatedTiles.addAll(currentUpdatedTiles);
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
	public void finalizeMatch() {
		for (Entity e : entities) {
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
	 *            - the color of the player we want to remove.
	 */
	public List<Tile> removePlayer(PlayerColor color)
			throws NotEnoughPlayersException {
		Player p = players.getByColor(color);
		p.setInactive();
		List<Tile> updates = new ArrayList<Tile>();
		for(Entity e : entities){
			updates.addAll(e.removeFollowers(color));
		}
		
		/* There are no enought players to play the game. */
		if (players.getSize() < 2) {
			throw new NotEnoughPlayersException();
		}
		return updates;
	}

	/* Private Methods. */

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
					sideEntity = EntityFactory.createByType(type);
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

	private void addSideToEntity(Side side, Entity entity) {
		side.setEntity(entity);
		if (entity != null) {
			entity.addMember(side);
		}
	}

	private List<Tile> finalizeEntityAndUpdate(Entity entity) {
		/* Give corresponding score to entity owners. */
		int score = entity.getScore();
		int[] followers = entity.countFollowers(playersNumber);
		giveScoreToOwners(followers, score);
		/* Return followers to owners. */
		returnFollowers(followers);
		/* Remove followers from entity and return a list of updated cards. */
		entities.remove(entity);
		return entity.removeFollowers(null);
	}

	private void giveScoreToOwners(int[] followers, int score) {

		int max = 0;

		for (int f : followers) {
			/* Maximum number of followers. */
			if (f > max) {
				max = f;
			}
		}

		/* No follower to assign a score. */
		if (max == 0) {
			return;
		}

		/* Score assignment. */
		for (int i = 0; i < followers.length; i++) {
			if (followers[i] == max) {
				PlayerColor color = PlayerColor.valueOf(i);
				Player player = players.getByColor(color);
				player.addScore(score);
			}
		}
	}

	/* Returns followers to their owners. */
	private void returnFollowers(int[] followers) {
		for (int i = 0; i < playersNumber; i++) {
			PlayerColor color = PlayerColor.valueOf(i);
			Player player = players.getByColor(color);
			player.addFollowers(followers[i]);
		}
	}
}