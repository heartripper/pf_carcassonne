package it.polimi.dei.provafinale.carcassonne.model.gameinterface;

/**
 * Enum containing all possible types of messages used in communications by
 * match handler and players
 * */
public enum MessageType {
	START("start"), TURN("turn"), NEXT("next"), ROTATE("rotate"), ROTATED(
			"rotated"), PLACE("place"), FOLLOWER("tile"), PASS("pass"), UPDATE(
			"update"), UPDATE_SINGLE("update"), INVALID_MOVE("move not valid"), SCORE(
			"score"), END("end"), LOCK("lock"), UNLOCK("unlock"), LEAVE("leave");

	private String protocolMessage;

	MessageType(String s) {
		this.protocolMessage = s;
	}

	@Override
	public String toString() {
		return protocolMessage;
	}

	/**
	 * Gives the MessageType corresponding to the given protocol message header.
	 * 
	 * @param type
	 *            - a String containing the protocol message header
	 * @return the MessageType corresponding to given message header if exists,
	 *         null otherwise.
	 * */
	public static MessageType getTypeFor(String type) {
		for (MessageType t : MessageType.values()){
			if (t.protocolMessage.equals(type)) {
				return t;
			}
		}
		return null;
	}
}
