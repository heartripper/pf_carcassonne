package it.polimi.dei.provafinale.carcassonne.model.gamelogic;

import it.polimi.dei.provafinale.carcassonne.model.NotEnoughPlayersException;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.card.*;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.entity.Entity;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.entity.EntityFactory;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.entity.EntityType;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.player.Player;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.player.PlayerCircularArray;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.player.PlayerColor;

import java.util.ArrayList;

public class Match {

	private TileGrid grid;
	private TileStack stack;
	private PlayerCircularArray players;
	private ArrayList<Entity> entities;

	private Card firstTile;
	private int playersNumber;

	/**
	 * Constructor: creates a new match for a given number of players.
	 * 
	 * @param playersNumber
	 *            - an int that indicates the number of players.
	 */
	public Match(int numPlayers) {
		this.grid = new TileGrid();
		this.entities = new ArrayList<Entity>();
		this.stack = new TileStack();
		this.players = new PlayerCircularArray(numPlayers);

		this.playersNumber = numPlayers;

		// Add cards #0 to the grid.
		firstTile = stack.getInitialTile();
		grid.putTile(firstTile, new Coord(0, 0));
		updateEntities(firstTile);
	}

	/**
	 * Gives the initial tile.
	 * 
	 * @return the initial tile
	 * */
	public Card getFirstTile() {
		return firstTile;
	}

	/**
	 * Used to determine if there are still cards in the stack.
	 * 
	 * @return true if there is at least a card in the stack, false instead.
	 */
	public boolean hasMoreCards() {
		return stack.hasMoreTiles();
	}

	/**
	 * 
	 * @return current player.
	 */
	public PlayerColor getNextPlayer() {
		return players.getNext().getColor();
	}

	/**
	 * @return the current card.
	 **/
	public Card drawCard() {
		return stack.drawTile();
	}

	/**
	 * Tries to put current card in the grid at given position.
	 * 
	 * @return true if the card was added (this means the card was compatible
	 *         with the position).
	 * */
	public boolean putTile(Card tile, Coord coord) {
		if(!grid.isTileCompatible(tile, coord))
			return false;

		grid.putTile(tile, coord);
		updateEntities(tile);
		return true;
	}

	/**
	 * Adds a follower of a color on a position of a card. If that operation
	 * succeeds removes a follower from player represented by that color.
	 * 
	 * @param tile
	 *            - the card to add the follower on
	 * @param position
	 *            - the position to put the follower at
	 * @param color
	 *            - the color of the player owning the follower
	 * 
	 * */
	public boolean putFollower(Card tile, SidePosition position,
			PlayerColor color) {
		Entity e = tile.getSide(position).getEntity();
		if (e == null || !e.acceptFollowers())
			return false;

		tile.addFollower(position, color);
		Player p = players.getByColor(color);
		p.removeFollower();
		return true;
	}

	/**
	 * Check if modified entities (the ones card sides belong to) are now
	 * complete. If an entity is complete finalizes (See Entity.finalize()) it
	 * and give its owners the resutling score.
	 * 
	 * @param card
	 *            - a Card that has been added to the grid.
	 */
	public ArrayList<Card> checkForCompletedEntities(Card card) {
		ArrayList<Entity> checkedEntities = new ArrayList<Entity>();
		ArrayList<Card> updatedTiles = new ArrayList<Card>();
		
		for (SidePosition position : SidePosition.values()) {
			Entity entity = card.getSide(position).getEntity();

			if (entity == null || checkedEntities.contains(entity))
				continue;

			checkedEntities.add(entity);
			if (entity.isComplete()) {
				updatedTiles.addAll(finalizeEntityAndUpdate(entity));
			}
		}
		return updatedTiles;
	}

	/**
	 * When the game is finished, give the score corresponding to each
	 * incomplete entity to his owner/owners.
	 * 
	 * @return a report of performed action
	 */
	public void finalizeMatch() {
		for (Entity e : entities) {
			finalizeEntityAndUpdate(e);
		}
	}

	/**
	 * Gives the game classification, which is an array of Players sorted by
	 * score.
	 * 
	 * @return an Array of Player sorted by score.
	 */
	public int[] getScores() {
		return players.getScores();
	}

	/**
	 * Removes a player from the match (removes him from player list and removes
	 * all his followers).
	 * 
	 * @param color
	 *            - the color of the player to remove.
	 */
	public void removePlayer(PlayerColor color)
			throws NotEnoughPlayersException {
		Player p = players.getByColor(color);
		p.setInactive();
		// TODO remove followers.

		if (players.getSize() < 2)
			throw new NotEnoughPlayersException();
	}

	// Private Methods

	/**
	 * Update the entities (cities, roads, fields) present on the grid after a
	 * card has been added.
	 * 
	 * @param card
	 *            - a card that has been added to the grid.
	 */
	private void updateEntities(Card card) {
		for (SidePosition position : SidePosition.values()) {
			Side currentSide = card.getSide(position);
			Side oppositeSide = currentSide.getOppositeSide();
			Entity currentEntity = currentSide.getEntity();
			EntityType type = currentSide.getType();
			Entity sideEntity;
			if (currentEntity == null) {
				if (oppositeSide == null) {
					sideEntity = EntityFactory.createByType(type);
				} else {
					sideEntity = oppositeSide.getEntity();
				}
				addSideToEntity(currentSide, sideEntity);
				for (Side s : card.sidesLinkedTo(currentSide)) {
					addSideToEntity(s, sideEntity);
				}
			} else {
				if (oppositeSide == null) {
					continue;
				}
				Entity oppositeEntity = oppositeSide.getEntity();
				if (oppositeEntity == null) {
					continue;
				}
				sideEntity = oppositeEntity.enclose(currentEntity);
				if (entities.contains(currentEntity)
						&& currentEntity != sideEntity) {
					entities.remove(currentEntity);
				}
				if (entities.contains(oppositeEntity)
						&& oppositeEntity != sideEntity) {
					entities.remove(oppositeEntity);
				}
			}
			if (sideEntity != null && !entities.contains(sideEntity)) {
				entities.add(sideEntity);
			}
		}
	}

	/**
	 * Add a side to a entity.
	 * 
	 * @param side
	 *            - a Side of a given card.
	 * @param entity
	 *            - an Entity which the side belongs to.
	 */
	private void addSideToEntity(Side side, Entity entity) {
		side.setEntity(entity);
		if (entity != null)
			entity.addMember(side);
	}

	private ArrayList<Card> finalizeEntityAndUpdate(Entity entity) {
		// Give corresponding score to entity owners
		int score = entity.getScore();
		int[] followers = entity.countFollowers(playersNumber);
		giveScoreToOwners(followers, score);
		// return followers to owners
		returnFollowers(followers);
		// Remove followers from entity and return a list of updated cards.
		return entity.removeFollowers();
	}

	private void giveScoreToOwners(int[] followers, int score) {
		int max = 0;

		for (int f : followers) {
			if (f > max)
				max = f;
		}

		if (max == 0)
			return;

		for (int i = 0; i < followers.length; i++) {
			if (followers[i] == max) {
				PlayerColor color = PlayerColor.valueOf(i);
				Player p = players.getByColor(color);
				p.addScore(score);
			}
		}
	}

	private void returnFollowers(int[] followers) {
		for (int i = 0; i < playersNumber; i++) {
			PlayerColor color = PlayerColor.valueOf(i);
			Player p = players.getByColor(color);
			p.addFollowers(followers[i]);
		}
	}

	/**
	 * Return the string representation of Match
	 */
	@Override
	public String toString() {
		return String.format("Remaining cards: %s - %s\n\n%s",
				stack.remainingTilesNumber(), players, grid);
	}
}