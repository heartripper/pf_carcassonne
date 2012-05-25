package it.polimi.dei.provafinale.carcassonne.controller.client;

import it.polimi.dei.provafinale.carcassonne.model.gameinterface.GameInterface;
import it.polimi.dei.provafinale.carcassonne.model.gameinterface.Message;
import it.polimi.dei.provafinale.carcassonne.model.gameinterface.MessageBuffer;
import it.polimi.dei.provafinale.carcassonne.model.gameinterface.MessageType;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.player.PlayerColor;
import it.polimi.dei.provafinale.carcassonne.model.server.PlayersDisconnectedException;

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
	public int askPlayerNumber() {
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
	public void sendAllPlayer(Message msg) throws PlayersDisconnectedException {
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