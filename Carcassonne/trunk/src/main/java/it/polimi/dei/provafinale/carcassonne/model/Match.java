package it.polimi.dei.provafinale.carcassonne.model;

import it.polimi.dei.provafinale.carcassonne.model.card.*;
import it.polimi.dei.provafinale.carcassonne.model.entity.Entity;
import it.polimi.dei.provafinale.carcassonne.model.entity.EntityFactory;
import it.polimi.dei.provafinale.carcassonne.model.entity.EntityType;
import it.polimi.dei.provafinale.carcassonne.model.player.Player;
import it.polimi.dei.provafinale.carcassonne.model.player.PlayerCircularArray;

import java.util.ArrayList;
import java.util.Random;

public class Match {

	private TileGrid grid;
	private TileStack stack;
	private PlayerCircularArray players;
	private ArrayList<Entity> entities;

	// Turn
	private Card firstTile;
	private Card currentCard;
	private Player currentPlayer;
	private int turnCount = 0;

	/**
	 * Constructor: creates a new match for a given number of players.
	 * 
	 * @param numPlayers
	 *            - an int that indicates the number of players.
	 */
	public Match(int numPlayers) {
		this.grid = new TileGrid();
		this.entities = new ArrayList<Entity>();
		this.stack = new TileStack();
		this.players = new PlayerCircularArray(numPlayers);

		// Add cards #0 to the grid.
		firstTile = stack.getInitialTile();
		grid.putTile(firstTile, new Coord(0, 0));
		updateEntities(firstTile);
	}

	/**
	 * Add a player to the game.
	 * 
	 * @param player
	 *            - a Player to add to the game.
	 * @return true if the player has been added correctly, false instead.
	 */
	public boolean addPlayer(Player player) {
		return players.add(player);
	}

	// Turn management

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
	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	/**
	 * @return the current card.
	 **/
	public Card getCurrentCard() {
		return currentCard;
	}

	/**
	 * Gives the initial tile.
	 * 
	 * @return the initial tile
	 * */
	public Card getFirstCard() {
		return firstTile;
	}

	/**
	 * Starts a new turn setting the player and drawing a card.
	 * */
	public void startTurn() {
		turnCount++;
		this.currentCard = stack.drawTile();
		this.currentPlayer = players.next();
	}

	/**
	 * Rotates current card (The one current player has drew).
	 * 
	 * @return the current card
	 * */
	public Card rotateCurrentCard() {
		currentCard.rotate();
		return currentCard;
	}

	/**
	 * Tries to put current card in the grid at given position.
	 * 
	 * @return true if the card was added (this means the card was compatible
	 *         with the position).
	 * */
	public boolean putCurrentCard(Coord coord) {
		boolean added = grid.putTile(currentCard, coord);
		if (!added)
			return false;

		updateEntities(currentCard);
		return true;
	}

	/**
	 * @return a list of the position of sides in the current card that accept
	 *         coin.
	 * */
	public ArrayList<SidePosition> positionsForCoin() {
		return currentCard.sidesAcceptingCoin();
	}

	/**
	 * Tries to add a coin to the side corresponding to the given position in
	 * the current card.
	 * 
	 * @return true if the coin was added, false if it couldn't be added.
	 * */
	public boolean addCoin(SidePosition position) {
		Side destination = currentCard.getSide(position);
		Entity entity = destination.getEntity();

		if (entity == null || !entity.acceptCoin())
			return false;
		else {
			currentPlayer.removeCoin();
			return destination.setPlayerCoin(currentPlayer);
		}
	}

	/**
	 * Ends current turn checking if any entities have been completed during
	 * current turn. If so adds resulting score to owners and finalizes them
	 * 
	 * @return a report containing details of performed actions.
	 * */
	public String endTurn() {
		String report = checkForCompletedEntities(currentCard);
		currentPlayer = null;
		currentCard = null;
		return report;
	}

	/**
	 * When the game is finished, give the score corresponding to each
	 * incomplete entity to his owner/owners.
	 * 
	 * @return a report of performed action
	 */
	public String finalizeMatch() {
		String report = "";
		for (Entity e : entities) {
			for (Player p : e.getOwners()) {
				int score = e.getScore();
				report += String.format(
						" - Player %s gets %s points from %s.\n", p, score, e);
				p.addScore(score);
			}
		}
		return report;
	}

	/**
	 * Gives the game classification, which is an array of Players sorted by
	 * score.
	 * 
	 * @return an Array of Player sorted by score.
	 */
	public Player[] getPlayerChart() {
		return players.getChart();
	}

	// Private Methods

	/**
	 * Check the compatibility of the card with the given position.
	 * 
	 * @param card
	 *            - a Card to collocate in the grid
	 * @param coord
	 *            - the Coord (coordinate) where the player wants to put the
	 *            card.
	 * @return true if the card is correctly added to the grid, false if it is
	 *         impossible to add.
	 */
	// private boolean addCardToGame(Card card, Coord coord) {
	// // If card is not compatible with the passed position, return false
	// if (!grid.isCardCompatible(card, coord)) {
	// return false;
	// }
	// // Add card to grid and update card status.
	// grid.setCard(coord, card);
	// card.setLocationInfo(grid, coord);
	// // Update game entity after the insertion of the new card.
	// updateEntities(card);
	// return true;
	// }

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

	/**
	 * Check if modified entities (the ones card sides belong to) are now
	 * complete. If an entity is complete finalizes (See Entity.finalize()) it
	 * and give its owners the resutling score.
	 * 
	 * @param card
	 *            - a Card that has been added to the grid.
	 */
	private String checkForCompletedEntities(Card card) {
		String report = "";
		ArrayList<Entity> checkedEntities = new ArrayList<Entity>();

		for (SidePosition position : SidePosition.values()) {
			Entity entity = card.getSide(position).getEntity();

			if (entity == null || checkedEntities.contains(entity))
				continue;

			checkedEntities.add(entity);

			if (entity.isComplete()) {
				report += String.format(" - %s is completed.\n", entity);
				int score = entity.getScore();
				for (Player player : entity.getOwners()) {
					player.addScore(score);
					report += String
							.format("   - Player '%s' gets %s points.\n",
									player, score);
				}
				entity.finalizeEntity();
			}
		}
		return report;
	}

	/**
	 * Return the string representation of Match
	 */
	@Override
	public String toString() {
		return String.format("TURN %s - Player: %s\n"
				+ "Remaining cards: %s - %s\n\n%s", turnCount, currentPlayer,
				stack.remainingTilesNumber(), players, grid);
	}
}