package it.polimi.dei.provafinale.carcassonne.controller;

import java.util.ArrayList;
import java.util.List;

import it.polimi.dei.provafinale.carcassonne.PlayerColor;

/**
 * Exception to communicate that one or more players has disconnected. It holds
 * the colors of disconnected players.
 * */
public class PlayersDisconnectedException extends Exception {

	private static final long serialVersionUID = 1L;

	private List<PlayerColor> colors;

	/**
	 * Constructs an exception with no players
	 * */
	public PlayersDisconnectedException(){
		colors = new ArrayList<PlayerColor>();
	}
	
	
	/**
	 * Constructs a new instance for a single disconnected player.
	 * 
	 * @param color
	 *            - the color of the player who disconnected
	 * */
	public PlayersDisconnectedException(PlayerColor color) {
		super();
		colors = new ArrayList<PlayerColor>();
		colors.add(color);
	}

	/**
	 * Constructs a new instance for a list of disconnected players.
	 * 
	 * @param colors
	 *            - a list of players who disconnected
	 * */
	public PlayersDisconnectedException(List<PlayerColor> colors) {
		super();
		this.colors = colors;
	}

	/**
	 * Adds a list of disconnected players to this instance's list.
	 * 
	 * @param newColors
	 *            - a list containing the colors of disconnected players
	 * 
	 * */
	public void add(List<PlayerColor> newColors) {
		colors.addAll(newColors);
	}

	/**
	 * Adds a disconnected player to this instance's list.
	 * 
	 * @param color
	 *            - the color of disconnected player
	 * */
	public void add(PlayerColor color) {
		colors.add(color);
	}

	/**
	 * Gets the list of disconnected players.
	 * 
	 * @return the list of disconnected players
	 * */
	public List<PlayerColor> getDisconnectedPlayers() {
		return colors;
	}
}
