package it.polimi.dei.provafinale.carcassonne.model.gameinterface;

import it.polimi.dei.provafinale.carcassonne.model.player.PlayerColor;
import it.polimi.dei.provafinale.carcassonne.model.server.PlayersDisconnectedException;

/**
 * Interface between the game handler and game utilizers
 */
public interface GameInterface {
	public int askPlayerNumber();

	public Message readFromPlayer(PlayerColor player) throws PlayersDisconnectedException;

	public void sendPlayer(PlayerColor color, Message msg) throws PlayersDisconnectedException;

	public void sendAllPlayer(Message msg) throws PlayersDisconnectedException;
}
