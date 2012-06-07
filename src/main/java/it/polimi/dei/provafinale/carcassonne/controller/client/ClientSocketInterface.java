package it.polimi.dei.provafinale.carcassonne.controller.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import it.polimi.dei.provafinale.carcassonne.PlayerColor;
import it.polimi.dei.provafinale.carcassonne.controller.ClientInterface;
import it.polimi.dei.provafinale.carcassonne.controller.ConnectionLostException;
import it.polimi.dei.provafinale.carcassonne.controller.Message;

/**
 * Class ClientSocketInterface implements ClientInterface in order to give an
 * interface of a client that plays in Socket mode.
 * 
 */
public class ClientSocketInterface implements ClientInterface {

	private final String addr;
	private final int port;

	private Socket socket;
	private ObjectInputStream in;
	private ObjectOutputStream out;

	/**
	 * ClientSocketInterface constructor. Creates a new instance of class
	 * ClientSocketInterface.
	 * 
	 * @param addr
	 *            a String representing an address.
	 * @param port
	 *            a port number.
	 */
	public ClientSocketInterface(String addr, int port) {
		this.addr = addr;
		this.port = port;
	}

	@Override
	public void connect() throws ConnectionLostException {
		try {
			socket = new Socket(addr, port);
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			sendToServer("connect");
		} catch (IOException ioe) {
			throw new ConnectionLostException();
		}
	}

	@Override
	public void sendMessage(Message msg) throws ConnectionLostException {
		String protocolMsg = msg.toProtocolMessage();
		sendToServer(protocolMsg);
	}

	@Override
	public Message readMessage() throws ConnectionLostException {
		String protocolMsg = readFromServer();
		return Message.createFromProtocolMsg(protocolMsg);
	}

	@Override
	public void reconnect(String matchName, PlayerColor color)
			throws ConnectionLostException {
		String message = String.format("reconnect: %s, %s", color, matchName);
		sendToServer(message);
	}

	/* Helper methods. */

	/**
	 * Sends a message to the server.
	 * 
	 * @param msg
	 *            the message to be sent.
	 * @throws ConnectionLostException
	 */
	private void sendToServer(String msg) throws ConnectionLostException {
		try {
			out.writeObject(msg);
			out.flush();
		} catch (IOException ioe) {
			throw new ConnectionLostException();
		}
	}

	/**
	 * Reads a message from the server.
	 * 
	 * @return the String representation of the read message.
	 * @throws ConnectionLostException
	 */
	private String readFromServer() throws ConnectionLostException {
		try {
			String msg = (String) in.readObject();
			System.out.println("SOCKET|READ: " + msg);
			return msg;
		} catch (ClassNotFoundException cnf) {
			throw new RuntimeException(cnf);
		} catch (IOException e) {
			throw new ConnectionLostException();
		}
	}

}