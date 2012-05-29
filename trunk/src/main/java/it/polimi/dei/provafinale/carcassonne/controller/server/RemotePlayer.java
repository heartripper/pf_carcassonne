package it.polimi.dei.provafinale.carcassonne.controller.server;

import it.polimi.dei.provafinale.carcassonne.controller.Message;
import it.polimi.dei.provafinale.carcassonne.controller.client.ConnectionLostException;

/**
 * Interface to represent a remote player.
 * */
public interface RemotePlayer {

	/**
	 * Reads a Message from remote player.
	 * 
	 * @return the Message read from player
	 * @throws ConnectionLostException
	 *             if the player has disconnected.
	 * */
	Message readMessage() throws ConnectionLostException;

	/**
	 * Sends a Message to a remote player.
	 * 
	 * @param msg
	 *            - the Message to send to remote player
	 * @throws ConnectionLostException
	 *             if the player has disconnected.
	 * */
	void sendMessage(Message msg) throws ConnectionLostException;

	/**
	 * Closes the communication with remote player
	 * 
	 * @throws ConnectionLostException
	 *             if the player has disconnected.
	 * */
	void close() throws ConnectionLostException;

	/**
	 * Returns remote player's status. A remote player is considered active if
	 * he is connected.
	 * 
	 * @return true if remote player is active
	 * */
	boolean isActive();

	/**
	 * Sets the player as inactive after he has disconnected.
	 * */
	void setInactive();
}
