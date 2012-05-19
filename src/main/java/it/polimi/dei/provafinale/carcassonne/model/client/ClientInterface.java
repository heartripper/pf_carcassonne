package it.polimi.dei.provafinale.carcassonne.model.client;

import java.io.IOException;

import it.polimi.dei.provafinale.carcassonne.model.gameinterface.Message;

public interface ClientInterface {
	public void connect() throws IOException;
	public void sendMessage(Message msg) throws IOException;
	public Message readMessage() throws IOException;
}
