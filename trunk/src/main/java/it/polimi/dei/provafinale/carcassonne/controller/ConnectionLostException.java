package it.polimi.dei.provafinale.carcassonne.controller;

/**
 * Class ConnectionLostException extends Exception in order to manage exceptions
 * of the type connection lost.
 * 
 */
public class ConnectionLostException extends Exception {

	private static final long serialVersionUID = 7560807455345741619L;

	/**
	 * Class ConnectionLostException constructor. Creates a new instance of
	 * class ConnectionLostConstructor in order to manage an exception of this
	 * type.
	 */
	public ConnectionLostException() {
		super();
	}

	/**
	 * Class ConnectionLostException constructor. Creates a new instance of
	 * class ConnectionLostConstructor in order to manage an exception of this
	 * type.
	 * 
	 * @param s
	 *            a String that indicates an error.
	 */
	public ConnectionLostException(String s) {
		super(s);
	}

}
