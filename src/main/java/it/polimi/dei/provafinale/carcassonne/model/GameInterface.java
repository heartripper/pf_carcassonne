package it.polimi.dei.provafinale.carcassonne.model;

import it.polimi.dei.provafinale.carcassonne.Message;
import it.polimi.dei.provafinale.carcassonne.PlayerColor;
import it.polimi.dei.provafinale.carcassonne.controller.server.PlayersDisconnectedException;

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
	int askPlayerNumber();

	/**
	 * Reads a message from a player.
	 * 
	 * @param color
	 *            - the PlayerColor of player to read from
	 * @return the read Message
	 * @throws PlayersDisconnectedException
	 *             when given player disconnects
	 * */
	Message readFromPlayer(PlayerColor color)
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
	void sendPlayer(PlayerColor color, Message msg)
			throws PlayersDisconnectedException;

	/**
	 * Sends a message to all players.
	 * 
	 * @param msg
	 *            - the Message to send
	 * @throws PlayersDisconnectedException
	 *             when one or more player discconnects;
	 * */
	void sendAllPlayer(Message msg) throws PlayersDisconnectedException;
}
