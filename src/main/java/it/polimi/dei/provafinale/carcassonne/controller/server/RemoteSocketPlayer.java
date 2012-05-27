package it.polimi.dei.provafinale.carcassonne.controller.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import it.polimi.dei.provafinale.carcassonne.controller.client.ConnectionLostException;
import it.polimi.dei.provafinale.carcassonne.model.gameinterface.Message;

public class RemoteSocketPlayer implements RemotePlayer {

	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private boolean active = true;

	public RemoteSocketPlayer(Socket socket, ObjectOutputStream out,
			ObjectInputStream in) {
		this.socket = socket;
		this.in = in;
		this.out = out;
	}

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

	@Override
	public boolean isActive() {
		return active;
	}

	@Override
	public void setInactive() {
		this.active = false;
	}

}
