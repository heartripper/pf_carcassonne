package it.polimi.dei.provafinale.carcassonne.model.gameinterface;

public class Message {
	public final MessageType type;
	public final String payload;

	public Message(MessageType type, String payload) {
		this.type = type;
		this.payload = payload;
	}

	public String toProtocolMessage() {
		String msg = type.toString();
		if (payload != null){
			msg += (": " + payload);
		}
		return msg;
	}

	public static Message createFromProtocolMsg(String msg) {
		String[] split = msg.split(",");
		MessageType type = MessageType.getTypeFor(split[0]);
		return new Message(type, split[1].trim());
	}
}
