package it.polimi.dei.provafinale.carcassonne.controller.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import it.polimi.dei.provafinale.carcassonne.controller.ConnectionLostException;
import it.polimi.dei.provafinale.carcassonne.controller.Message;
import it.polimi.dei.provafinale.carcassonne.controller.RemotePlayer;

/**
 * Class RemoteSocketPlayer implements a RemotePlayer in order to represent a
 * remote player.
 * 
 */
public class RemoteSocketPlayer implements RemotePlayer {

	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private boolean active = true;

	/**
	 * RemoteSocketPlayer constructor. Creates a new instance of class
	 * RemoteSocketPlayer.
	 * 
	 * @param socket
	 *            a Socket.
	 * @param out
	 *            an output stream.
	 * @param in
	 *            an input stream.
	 */
	public RemoteSocketPlayer(Socket socket, ObjectOutputStream out,
			ObjectInputStream in) {
		this.socket = socket;
		this.in = in;
		this.out = out;
	}

	/* Reads the messages from client. */
	@Override
	public Message readMessage() throws ConnectionLostException {
		try {
			String s = (String) in.readObject();
			Message msg = Message.createFromProtocolMsg(s);
			return msg;
		} catch (IOException ioe) {
			throw new ConnectionLostException();
		} catch (ClassNotFoundException cnf) {
			System.out.println("Critical error: " + cnf.toString());
			return null;
		}
	}

	/* Sends message msg to server. */
	@Override
	public void sendMessage(Message msg) throws ConnectionLostException {
		try {
			String protocolMessage = msg.toProtocolMessage();
			out.writeObject(protocolMessage);
			out.flush();
		} catch (IOException ioe) {
			throw new ConnectionLostException();
		}
	}

	/* Closes the connection with the user. */
	@Override
	public void close() throws ConnectionLostException {
		try {
			socket.close();
			in.close();
			out.close();
		} catch (IOException ioe) {
			throw new ConnectionLostException();
		}
	}

	/* Return if a player is active. */
	@Override
	public boolean isActive() {
		return active;
	}

	/* Sets that a player is inactive. */
	@Override
	public void setInactive() {
		this.active = false;
	}

}
