package it.polimi.dei.provafinale.carcassonne.controller.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;

import it.polimi.dei.provafinale.carcassonne.controller.ConnectionLostException;
import it.polimi.dei.provafinale.carcassonne.controller.Message;

/**
 * Class RemoteSocketPlayer implements a RemotePlayer in order to represent a
 * remote player.
 * 
 */
public class RemoteSocketPlayer implements RemotePlayer {

	private Socket socket;
	private BufferedReader in;
	private BufferedWriter out;
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
	public RemoteSocketPlayer(Socket socket, BufferedWriter out,
			BufferedReader in) {
		this.socket = socket;
		this.in = in;
		this.out = out;
	}

	/* Reads the messages from client. */
	@Override
	public Message readMessage() throws ConnectionLostException {
		try {
			String s = in.readLine();
			if(s == null){
				throw new RuntimeException("Read null value from socket");
			}
			return Message.createFromProtocolMsg(s);
		} catch (IOException ioe) {
			throw new ConnectionLostException();
		}
	}

	/* Sends message msg to server. */
	@Override
	public void sendMessage(Message message) throws ConnectionLostException {
		try {
			String protocolMessage = message.toProtocolMessage();
			out.write(protocolMessage);
			out.newLine();
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
