package it.polimi.dei.provafinale.carcassonne.controller.client;

import it.polimi.dei.provafinale.carcassonne.PlayerColor;

/**
 * Interface to let Client Controller control an user interface.
 * */
public interface ViewInterface {
	/**
	 * Initializes the user interface.
	 * 
	 * @param numPlayers
	 *            - a int representing the number of players
	 * @param clientColor
	 *            - the color of player handled by controller, null if
	 *            controller handles all players (local game)
	 * */
	void initialize(String initializeString);

	/**
	 * Makes the user interface update the representation of the grid.
	 * */
	void updateGridRepresentation(String msg);

	/**
	 * Updates the current tile in the user interface.
	 * 
	 * @param - the string representation of a tile
	 * */
	void updateCurrentTile(String rep);

	/**
	 * Updates the score in the user interface
	 * */
	void updateScore(String msg);

	/**
	 * Enables or disables the user interface.
	 * 
	 * @param enabled
	 *            - true if the user interface should be enabled, false
	 *            otherwise
	 * */
	void setUIActive(boolean enabled);

	/**
	 * Sets current player in the user interface.
	 * 
	 * @param color
	 *            - the PlayerColor of current player
	 * */
	void setCurrentPlayer(PlayerColor color);

	/**
	 * Shows a notice on the user interface
	 * 
	 * @param msg
	 *            - the notice to show
	 * */
	void showNotify(String msg);

}
