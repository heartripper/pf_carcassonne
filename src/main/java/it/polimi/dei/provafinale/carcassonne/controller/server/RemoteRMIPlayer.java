package it.polimi.dei.provafinale.carcassonne.controller.server;

import java.rmi.RemoteException;

import it.polimi.dei.provafinale.carcassonne.controller.RMIClient;
import it.polimi.dei.provafinale.carcassonne.controller.ConnectionLostException;
import it.polimi.dei.provafinale.carcassonne.controller.Message;

/**
 * Class RemoteRMIPlayer implements a RemotePlayer in order to represent a
 * remote player.
 * 
 */
public class RemoteRMIPlayer implements RemotePlayer {

	private RMIClient client;
	private boolean active;

	/**
	 * RemoteRMIPlayer constructor. Creates a new instance of class
	 * RemoteRMIPlayer.
	 * 
	 * @param client
	 *            a CarcassonneRMIClient we want to add to the game.
	 */
	public RemoteRMIPlayer(RMIClient client) {
		this.client = client;
		this.active = true;
	}

	/* Reads the messages from client. */
	@Override
	public Message readMessage() throws ConnectionLostException {
		try {
			return client.readMessageFromPlayer();
		} catch (RemoteException re) {
			throw new ConnectionLostException();
		}
	}

	/* Sends message msg to server. */
	@Override
	public void sendMessage(Message msg) throws ConnectionLostException {
		try {
			client.sendMessageToPlayer(msg);
		} catch (RemoteException re) {
			throw new ConnectionLostException();
		}
	}

	/* Closes the connection with the user. */
	@Override
	public void close() throws ConnectionLostException {
		// TODO
	}

	/* Return if a player is active. */
	@Override
	public boolean isActive() {
		return active;
	}

	/* Sets that a player is inactive. */
	@Override
	public void setInactive() {
		active = false;
	}

}
