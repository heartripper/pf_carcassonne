package it.polimi.dei.provafinale.carcassonne.controller.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import it.polimi.dei.provafinale.carcassonne.model.gameinterface.Message;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.player.PlayerColor;

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
	public void connect() throws ConnectionLostException{
		try{
			socket = new Socket(addr, port);
			in = new ObjectInputStream(socket.getInputStream());
			out = new ObjectOutputStream(socket.getOutputStream());

			sendToServer("connect");
		}catch(IOException ioe){
			//TODO: we got disconnected from the server.
		}
	}

	@Override
	public void sendMessage(Message msg) throws ConnectionLostException{
		String protocolMsg = msg.toProtocolMessage();
		sendToServer(protocolMsg);
	}

	@Override
	public Message readMessage() throws ConnectionLostException{
			String protocolMsg = readFromServer();
			Message msg = Message.createFromProtocolMsg(protocolMsg);
			return msg;
	}

	@Override
	public void reconnect(String matchName, PlayerColor color) throws ConnectionLostException {
		String message = String.format("reconnect: %s, %s", color, matchName);
		sendToServer(message);
	}
	
	// Helper methods
	private void sendToServer(String msg) throws ConnectionLostException{
		try{
			out.writeObject(msg);
			out.flush();
		}catch(IOException ioe){
			throw new ConnectionLostException();
		}
	}

	private String readFromServer() throws ConnectionLostException {
		try {
			return (String) in.readObject();
		} catch (ClassNotFoundException e) {
			System.out.println("ClassNotFoundExcepion");
			System.exit(1); // TODO: do something better
			return null;
		} catch (IOException e) {
			throw new ConnectionLostException();
		}
	}
}