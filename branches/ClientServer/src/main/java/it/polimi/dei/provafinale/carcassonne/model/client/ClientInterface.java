package it.polimi.dei.provafinale.carcassonne.model.client;

import it.polimi.dei.provafinale.carcassonne.model.gameinterface.Message;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.player.PlayerColor;

public interface ClientInterface {
	public void connect() throws ConnectionLostException;

	public void sendMessage(Message msg) throws ConnectionLostException;

	public Message readMessage() throws ConnectionLostException;
	
	public void reconnect(String matchName, PlayerColor color) throws ConnectionLostException;
}
