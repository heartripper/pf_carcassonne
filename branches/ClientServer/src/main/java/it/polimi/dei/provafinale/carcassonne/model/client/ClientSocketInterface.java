package it.polimi.dei.provafinale.carcassonne.model.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import it.polimi.dei.provafinale.carcassonne.model.gameinterface.Message;
import it.polimi.dei.provafinale.carcassonne.model.gameinterface.MessageType;

public class ClientSocketInterface implements ClientInterface {

	private final String addr;
	private final int port;

	private Socket socket;
	private ObjectInputStream in;
	private ObjectOutputStream out;

	public ClientSocketInterface(String addr, int port) {
		this.addr = addr;
		this.port = port;
	}

	@Override
	public void connect() throws IOException {
		socket = new Socket(addr, port);
		in = new ObjectInputStream(socket.getInputStream());
		out = new ObjectOutputStream(socket.getOutputStream());

		sendToServer("connect");
	}

	@Override
	public void sendMessage(Message msg) throws IOException {
		String protocolMsg = msg.toProtocolMessage();
		sendToServer(protocolMsg);
	}

	@Override
	public Message readMessage() throws IOException {
		String protocolMsg = readFromServer();
		Message msg = Message.createFromProtocolMsg(protocolMsg);
		return msg;
	}

	// Helper methods
	private void sendToServer(String msg) throws IOException {
		out.writeObject(msg);
		out.flush();
	}

	private String readFromServer() throws IOException {
		try {
			return (String) in.readObject();
		} catch (ClassNotFoundException e) {
			System.out.println("ClassNotFoundExcepion");
			System.exit(1); // TODO: do something better
			return null;
		}
	}
}