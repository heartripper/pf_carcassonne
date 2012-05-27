package it.polimi.dei.provafinale.carcassonne.view.viewInterface;

import it.polimi.dei.provafinale.carcassonne.model.gamelogic.player.PlayerColor;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.tile.TileGrid;

/**
 * Interface to let Client Controller control an user interface.
 * */

public interface ViewInterface {
	/**
	 * Initializes the user interface.
	 * 
	 * @param grid
	 *            - a tileGrid to be represented on the user interface
	 * @param numPlayers
	 *            - a int representing the number of players
	 * @param clientColor
	 *            - the color of player handled by controller, null if
	 *            controller handles all players (local game)
	 * */
	public abstract void initialize(TileGrid grid, int numPlayers,
			PlayerColor clientColor);

	/**
	 * Makes the user interface update the representation of the grid.
	 * */
	public abstract void updateGridRepresentation();

	/**
	 * Updates the current tile in the user interface.
	 * 
	 * @param - the string representation of a tile
	 * */
	public abstract void updateCurrentTile(String rep);

	/**
	 * Updates the score in the user interface
	 * */
	public abstract void updateScore(String msg);

	/**
	 * Enables or disables the user interface.
	 * 
	 * @param enabled
	 *            - true if the user interface should be enabled, false
	 *            otherwise
	 * */
	public abstract void setUIActive(boolean enabled);

	/**
	 * Sets current player in the user interface.
	 * 
	 * @param color
	 *            - the PlayerColor of current player
	 * */
	public abstract void setCurrentPlayer(PlayerColor color);

	/**
	 * Shows a notice on the user interface
	 * 
	 * @param msg
	 *            - the notice to show
	 * */
	public abstract void showNotify(String msg);

}
