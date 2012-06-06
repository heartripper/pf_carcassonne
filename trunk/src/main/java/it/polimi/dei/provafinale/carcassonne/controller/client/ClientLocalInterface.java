package it.polimi.dei.provafinale.carcassonne.controller.client;

import it.polimi.dei.provafinale.carcassonne.PlayerColor;
import it.polimi.dei.provafinale.carcassonne.controller.ConnectionLostException;
import it.polimi.dei.provafinale.carcassonne.controller.Message;
import it.polimi.dei.provafinale.carcassonne.controller.MessageType;
import it.polimi.dei.provafinale.carcassonne.controller.server.GameInterface;

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
	public void sendMessage(Message msg) {
		clientBuffer.write(msg);
	}

	@Override
	public Message readMessage() {
		return modelBuffer.read();
	}

	@Override
	public void reconnect(String matchName, PlayerColor color) {
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
	public void sendPlayer(PlayerColor color, Message msg) {
		if (msg.type == MessageType.UPDATE_SINGLE) {
			msg = new Message(MessageType.UPDATE, msg.payload);
		}
		modelBuffer.write(msg);
	}

	@Override
	public void sendAllPlayer(Message msg) {
		/* Case start message. */
		if (msg.type == MessageType.START) {
			String tile = msg.payload;
			String payload = String.format("%s, %s, %s, %s", tile, "localgame",
					"null", playerNumber);
			msg = new Message(MessageType.START, payload);
		}

		modelBuffer.write(msg);
	}

}