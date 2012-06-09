package it.polimi.dei.provafinale.carcassonne;

/**
 * Class to hold constants used widely across the project.
 * */
public abstract class Constants {

	/* Basics */
	public static final int MIN_PLAYER_NUMBER = 2;
	public static final int MAX_PLAYER_NUMBER = 5;

	public static final int SIDES_NUMBER = 4;

	public static final int FOLLOWER_PIXEL_DIMENSION = 25;
	public static final int TILE_PIXEL_DIMENSION = 125;

	public static final boolean ASK_ON_CLOSE = false;

	public static final String TILES_PATH = "/carcassonne.dat";
	public static final String LESS_TILES_PATH = "/carcassonne_mini.dat";

	public static final String FOLLOWER_SEPARATOR = "+";

	/* Debug constants */
	public static final boolean DEBUG_MODE = true;
	public static final String DEBUG_ADDR = "localhost";
	public static final int DEBUG_PORT = 12345;
	public static final boolean USE_FEW_TILES = false;

	/* Internet game panel */
	public static final String MATCH_IS_STARTING = "Connected to server. Waiting for match to begin.";
	public static final String FIELDS_ERROR = "Please fill in all fields.";
	public static final String PORT_ERROR = "Please insert a valid port value.";
	public static final String CONNECTION_ERROR = "Connection to server failed.";

	/* View Type */
	public static final int VIEW_TYPE_GUI = 0;
	public static final int VIEW_TYPE_TEXTUAL = 1;

	/* Tile painter */
	public static final String BASE_TILE_PATH = "/tiles.txt";
	public static final String PLACEHOLDER_PATH = "/placeholder.png";
	public static final String TILE_PATH_FORMAT = "/tiles/%s.png";
	public static final String FOLLOWERS_IMG_PATH_FORMAT = "/followers/%s.png";

	/* Server */
	public static final int PLAYER_LIST_CHECK_TIME = 10 * 1000;
	public static final String RMI_SERVER_NAME = "CarcassonneRMIServer";
}
