package it.polimi.dei.provafinale.carcassonne.controller.client;

import it.polimi.dei.provafinale.carcassonne.PlayerColor;
import it.polimi.dei.provafinale.carcassonne.controller.Message;

/**
 * Interface that is used by Client Controller to talk to Match Controller
 * */
public interface ClientInterface {

	/**
	 * Connects the interface to Match Controller interface.
	 * */
	void connect() throws ConnectionLostException;

	/**
	 * Sends a message to Match Controller interface
	 * 
	 * @param msg
	 *            - a Message to server
	 * */
	void sendMessage(Message msg) throws ConnectionLostException;

	/**
	 * Reads a message from Match Controller interface.
	 * 
	 * @return the Message read from Match Controller
	 * */
	Message readMessage() throws ConnectionLostException;

	/**
	 * Tries to reconnect to Match controller after a disconnection
	 * */
	void reconnect(String matchName, PlayerColor color)
			throws ConnectionLostException;
}
