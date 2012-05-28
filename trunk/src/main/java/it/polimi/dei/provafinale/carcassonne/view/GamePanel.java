package it.polimi.dei.provafinale.carcassonne.view;

import it.polimi.dei.provafinale.carcassonne.model.PlayerColor;
import it.polimi.dei.provafinale.carcassonne.model.TileGrid;

import javax.swing.JPanel;

/**
 * Class to let Client Controller control an user interface.
 * */
public abstract class GamePanel extends JPanel {

	private static final long serialVersionUID = -2458261363554188485L;

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
	abstract void initialize(TileGrid grid, int numPlayers,
			PlayerColor clientColor);

	/**
	 * Makes the user interface update the representation of the grid.
	 * */
	abstract void updateGridRepresentation();

	/**
	 * Updates the current tile in the user interface.
	 * 
	 * @param - the string representation of a tile
	 * */
	abstract void updateCurrentTile(String rep);

	/**
	 * Updates the score in the user interface
	 * */
	abstract void updateScore(String msg);

	/**
	 * Enables or disables the user interface.
	 * 
	 * @param enabled
	 *            - true if the user interface should be enabled, false
	 *            otherwise
	 * */
	abstract void setUIActive(boolean enabled);

	/**
	 * Sets current player in the user interface.
	 * 
	 * @param color
	 *            - the PlayerColor of current player
	 * */
	abstract void setCurrentPlayer(PlayerColor color);

	/**
	 * Shows a notice on the user interface
	 * 
	 * @param msg
	 *            - the notice to show
	 * */
	abstract void showNotify(String msg);
}
