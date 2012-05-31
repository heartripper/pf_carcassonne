package it.polimi.dei.provafinale.carcassonne.controller;

/**
 * Class ConnectionLostException extends Exception in order to manage exceptions
 * of the type connection lost.
 * 
 */
public class ConnectionLostException extends Exception {

	private static final long serialVersionUID = 7560807455345741619L;

	/**
	 * Manages the exception of type ConnectionLostException.
	 */
	public ConnectionLostException() {
		super();
	}

	/**
	 * Manages the exception of type ConnectionLostException.
	 * 
	 * @param s
	 *            a String that indicates an error.
	 */
	public ConnectionLostException(String s) {
		super(s);
	}

}
