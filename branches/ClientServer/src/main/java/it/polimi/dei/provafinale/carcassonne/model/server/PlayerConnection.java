package it.polimi.dei.provafinale.carcassonne.model.server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class PlayerConnection {
	public final Socket socket;
	public final ObjectOutputStream out;
	public final ObjectInputStream in;
	
	private boolean active;

	public PlayerConnection(Socket socket, ObjectOutputStream out,
			ObjectInputStream in) {
		this.socket = socket;
		this.out = out;
		this.in = in;
		this.active = true;
	}
	
	public void setActive(boolean active){
		this.active = active;
	}
	
	public boolean isActive(){
		return active;
	}
}
