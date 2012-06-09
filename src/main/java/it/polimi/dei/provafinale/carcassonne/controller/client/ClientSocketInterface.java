package it.polimi.dei.provafinale.carcassonne.controller.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

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
	private BufferedReader in;
	private BufferedWriter out;

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
		createSocket();
		sendToServer("connect");
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
	public void reconnect(String matchName, String color)
			throws ConnectionLostException {
		createSocket();
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
			out.write(msg);
			out.newLine();
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
			String msg = in.readLine();
			System.out.println("SOCKET|READ: " + msg);
			return msg;
		} catch (IOException e) {
			throw new ConnectionLostException();
		}
	}

	/**
	 * Creates the socket.
	 * 
	 * @throws ConnectionLostException
	 *             if there's no connection with server
	 * */
	private void createSocket() throws ConnectionLostException {
		try {
			socket = new Socket(addr, port);
			InputStream input = socket.getInputStream();
			in = new BufferedReader(new InputStreamReader(input));

			OutputStream output = socket.getOutputStream();
			out = new BufferedWriter(new OutputStreamWriter(output));
		} catch (IOException ioe) {
			throw new ConnectionLostException();
		}
	}
}