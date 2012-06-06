package it.polimi.dei.provafinale.carcassonne.controller;

import java.io.Serializable;

/**
 * This class represents messages that are exchanged between MatchHandler and
 * player clients.
 * */
public class Message implements Serializable {

	private static final long serialVersionUID = -5892768452421790257L;

	/**
	 * A MessageType.
	 */
	public final MessageType type;
	
	/**
	 * A String containing the message payload.
	 */
	public final String payload;

	/**
	 * Creates a new Message with give type and payload.
	 * 
	 * @param type
	 *            - a MessageType.
	 * @param payload
	 *            - a String containing the message payload.
	 * */
	public Message(MessageType type, String payload) {
		this.type = type;
		this.payload = payload;
	}

	/**
	 * Gives the protocol message represented by this Message.
	 * 
	 * @return a String containing the protocol message.
	 * */
	public String toProtocolMessage() {
		String msg = type.toString();
		if (payload != null) {
			msg += (": " + payload);
		}
		return msg;
	}

	/**
	 * Given a protocol message, creates a Message instance representing that
	 * message.
	 * 
	 * @param message
	 *            - a string containing the protocol message.
	 * @return a Message representation of protocol message.
	 */
	public static Message createFromProtocolMsg(String message) {
		String[] split = message.split(":");
		MessageType type = MessageType.getTypeFor(split[0].trim());
		String payload = null;
		if (split.length == 2) {
			payload = split[1].trim();
		}
		return new Message(type, payload);
	}

	@Override
	public String toString() {
		return toProtocolMessage();
	}

}