package it.polimi.dei.provafinale.carcassonne;

public abstract class Constants {

	public static final int SIDES_NUMBER = 4;

	public static final int TILE_PIXEL_DIMENSION = 125;

	public static final boolean ASK_ON_CLOSE = false;
	
	public static final boolean DEBUG_MODE = true;
	public static final String DEBUG_ADDR = "localhost";
	public static final int DEBUG_PORT = 12345;

	//Internet game panel.
	public static final String MATCH_IS_STARTING = "Connected to server. Waiting for match to begin.";
	public static final String FIELDS_ERROR = "Please fill in all fields.";
	public static final String PORT_ERROR = "Please insert a valid port value.";
	public static final String CONNECTION_ERROR = "Connection to server failed.";
}
