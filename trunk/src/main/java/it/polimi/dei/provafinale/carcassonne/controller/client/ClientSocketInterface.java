package it.polimi.dei.provafinale.carcassonne.controller.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import it.polimi.dei.provafinale.carcassonne.logger.Logger;
import it.polimi.dei.provafinale.carcassonne.logger.LoggerService;
import it.polimi.dei.provafinale.carcassonne.model.gameinterface.Message;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.player.PlayerColor;

public class ClientSocketInterface implements ClientInterface {

	private final String addr;
	private final int port;

	private Logger logger;
	
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
		logger = LoggerService.getService().register("Socket");
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
		logger.log("SOCKET|WRITE: " + msg);
	}

	@Override
	public Message readMessage() throws ConnectionLostException {
		String protocolMsg = readFromServer();
		Message msg = Message.createFromProtocolMsg(protocolMsg);
		logger.log("SOCKET|READ: " + msg);
		return msg;
	}

	@Override
	public void reconnect(String matchName, PlayerColor color)
			throws ConnectionLostException {
		String message = String.format("reconnect: %s, %s", color, matchName);
		sendToServer(message);
	}

	/* Helper methods. */

	private void sendToServer(String msg) throws ConnectionLostException {
		try {
			out.writeObject(msg);
			out.flush();
		} catch (IOException ioe) {
			throw new ConnectionLostException();
		}
	}

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