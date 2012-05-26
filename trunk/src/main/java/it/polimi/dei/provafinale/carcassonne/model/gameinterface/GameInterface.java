package it.polimi.dei.provafinale.carcassonne.model.gameinterface;

import it.polimi.dei.provafinale.carcassonne.model.gamelogic.player.PlayerColor;
import it.polimi.dei.provafinale.carcassonne.model.server.PlayersDisconnectedException;

/**
 * Interface to let Game Controller communicate with Client Controller.
 */
public interface GameInterface {

	/**
	 * Gives the number of players set by interface creator (server or user in
	 * case of local game).
	 * 
	 * @return the number of player
	 * */
	public int askPlayerNumber();

	/**
	 * Reads a message from a player.
	 * 
	 * @param color
	 *            - the PlayerColor of player to read from
	 * @return the read Message
	 * @throws PlayersDisconnectedException
	 *             when given player disconnects
	 * */
	public Message readFromPlayer(PlayerColor color)
			throws PlayersDisconnectedException;

	/**
	 * Sends a message to a player.
	 * 
	 * @param color
	 *            - the color of the player to send the message to
	 * @param msd
	 *            - the Message to send
	 * @throws PlayersDisconnectedException
	 *             when given player disconnects
	 * */
	public void sendPlayer(PlayerColor color, Message msg)
			throws PlayersDisconnectedException;

	/**
	 * Sends a message to all players.
	 * 
	 * @param msg
	 *            - the Message to send
	 * @throws PlayersDisconnectedException
	 *             when one or more player discconnects;
	 * */
	public void sendAllPlayer(Message msg) throws PlayersDisconnectedException;
}
