package it.polimi.dei.provafinale.carcassonne.controller;

/**
 * This is an interface used by Client Controller to talk to Match Controller.
 * */
public interface ClientInterface {

	/**
	 * Connects the interface to Match Controller interface.
	 * */
	void connect() throws ConnectionLostException;

	/**
	 * Sends a message to Match Controller interface.
	 * 
	 * @param message
	 *            - a Message to server.
	 * */
	void sendMessage(Message message) throws ConnectionLostException;

	/**
	 * Reads a message from Match Controller interface.
	 * 
	 * @return the Message read from Match Controller.
	 * */
	Message readMessage() throws ConnectionLostException;

	/**
	 * Tries to reconnect to Match controller after a disconnection.
	 * */
	void reconnect(String matchName, String color)
			throws ConnectionLostException;

}
