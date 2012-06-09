package it.polimi.dei.provafinale.carcassonne.controller;

import it.polimi.dei.provafinale.carcassonne.PlayerColor;

/**
 * Class ClientLocalInterface implements GameInterface and ClientInterface in
 * order to give an interface of a client that plays in the local mode.
 * 
 */
public class ClientLocalInterface implements GameInterface, ClientInterface {

	private MessageBuffer modelBuffer;
	private MessageBuffer clientBuffer;
	private int playerNumber;

	/**
	 * ClientLocalInterface constructor. Creates a new instance of class
	 * ClientLocalInterface.
	 * 
	 * @param playerNumber
	 *            the number of players that want to play in the current game.
	 */
	public ClientLocalInterface(int playerNumber) {
		this.playerNumber = playerNumber;
		this.modelBuffer = new MessageBuffer();
		this.clientBuffer = new MessageBuffer();
	}

	/* Client interface. */

	@Override
	public void connect() throws ConnectionLostException {
		/* Nothing to do here in local game. */
		return;
	}

	@Override
	public void sendMessage(Message message) {
		clientBuffer.write(message);
	}

	@Override
	public Message readMessage() {
		return modelBuffer.read();
	}

	@Override
	public void reconnect(String matchName, String color) {
		/* Nothing to do here in local game. */
		return;
	}

	/* Game interface */

	@Override
	public int getPlayerNumber() {
		return playerNumber;
	}

	@Override
	public Message readFromPlayer(PlayerColor player) {
		return clientBuffer.read();
	}

	@Override
	public void sendAllPlayer(Message message) {
		Message toSend;
		/* Case start message. */
		if (message.type == MessageType.START) {
			String tile = message.payload;
			String payload = String.format("%s, %s, %s, %s", tile, "localgame",
					"null", playerNumber);
			toSend = new Message(MessageType.START, payload);
		} else {
			toSend = message;
		}

		modelBuffer.write(toSend);
	}
}