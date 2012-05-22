package it.polimi.dei.provafinale.carcassonne.model.gameinterface;

public class Message {
	public final MessageType type;
	public final String payload;
	
	public Message(MessageType type, String payload){
		this.type = type;
		this.payload = payload;
	}
}
