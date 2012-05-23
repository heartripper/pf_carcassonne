package it.polimi.dei.provafinale.carcassonne;

public abstract class Constants {

	public final static int SIDES_NUMBER = 4;

	public final static int TILE_PIXEL_DIMENSION = 125;

	public final static boolean DEBUG_MODE = true;
	public final static String DEBUG_ADDR = "localhost";
	public final static int DEBUG_PORT = 12345;

	//Internet game panel.
	public final static String MATCH_IS_STARTING = "Connected to server. Waiting for match to begin.";
	public final static String FIELDS_ERROR = "Please fill in all fields.";
	public final static String PORT_ERROR = "Please insert a valid port value.";
	public final static String CONNECTION_ERROR = "Connection to server failed.";
}
