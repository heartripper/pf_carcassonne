package it.polimi.dei.provafinale.carcassonne.model.gameinterface;

public enum MessageType {
	START("start"), TURN("turn"), NEXT("next"), ROTATE("rotate"), ROTATED(
			"rotated"), PLACE("update"), FOLLOWER("follower"), PASS("pass"), UPDATE(
			"update"), INVALID_MOVE("move not valid"), SCORE("score"), END(
			"end"), LOCK("lock"), UNLOCK("unlock"), LEAVE("leave");

	private String protocolMessage;

	MessageType(String s) {
		this.protocolMessage = s;
	}

	@Override
	public String toString() {
		return protocolMessage;
	}

	public static MessageType getTypeFor(String type) {
		for (MessageType t : MessageType.values())
			if (t.protocolMessage.equals(type))
				return t;
		return null;
	}
}
