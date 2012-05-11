package it.polimi.dei.provafinale.carcassonne.model.gameinterface;

import it.polimi.dei.provafinale.carcassonne.model.player.PlayerColor;

/**
 * Interface between the game handler and game utilizers
 */
public interface GameInterface {
	public int askPlayerNumber();
	
	public Message readFromPlayer(PlayerColor player);
	
	public void sendPlayer(PlayerColor color, Message msg);

	public void sendAllPlayer(Message msg);
}
