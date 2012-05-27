package it.polimi.dei.provafinale.carcassonne.controller.server;

import it.polimi.dei.provafinale.carcassonne.controller.client.ConnectionLostException;
import it.polimi.dei.provafinale.carcassonne.model.gameinterface.Message;

public interface RemotePlayer {
	public Message readMessage() throws ConnectionLostException;

	public void sendMessage(Message msg) throws ConnectionLostException;

	public void close() throws ConnectionLostException;

	public boolean isActive();

	public void setInactive();
}
