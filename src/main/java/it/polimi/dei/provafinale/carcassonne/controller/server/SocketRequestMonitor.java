package it.polimi.dei.provafinale.carcassonne.controller.server;

import it.polimi.dei.provafinale.carcassonne.controller.Message;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;  
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Class to monitor players' requests to play made via sockets. It accepts
 * players requests, than enqueues them in Match Manager. *
 */
public class SocketRequestMonitor implements Runnable {

	private int port;
	private ServerSocket serverSocket;
	private MatchesManager matchesManager;

	/**
	 * Constructs a new Socket Requests Monitor.
	 * 
	 * @param matchesManager
	 *            - the Matches Manager to send requests to.
	 * @param port
	 *            - the port to listen for requests to.
	 * */
	public SocketRequestMonitor(MatchesManager matchesManager, int port) {
		this.port = port;
		this.matchesManager = matchesManager;
	}

	@Override
	public void run() {
		try {
			/* Start listening to given port */
			serverSocket = new ServerSocket(port);
		} catch (BindException be) {
			System.out.println("Given port is not free.");
			return;
		} catch (IOException e) {
			System.out.println("Server error: " + e);
			return;
		}

		System.out.println("Request monitor started.");

		while (true) {
			handleRequests();
		}
	}

	/**
	 * Handles a player's request to play, creating a RemotePlayer instance and
	 * enqueueing it in Matches Manager.
	 * */
	private void handleRequests() {
		try {
			/* Accept request */
			Socket socket = serverSocket.accept();

			/* Create streams */
			InputStream in = socket.getInputStream();
			BufferedReader input = new BufferedReader(new InputStreamReader(in));
			
			OutputStream out = socket.getOutputStream();
			BufferedWriter output = new BufferedWriter(new OutputStreamWriter(out));

			RemotePlayer player = new RemoteSocketPlayer(socket, output, input);

			/* Parse request */
			String message = input.readLine();
			if(message == null){
				throw new RuntimeException("Read null value from socket");
			}
			Message request = Message.createFromProtocolMsg(message);

			matchesManager.enqueuePlayer(player, request);

		} catch (IOException e) {
			System.out.println("Lost connection with uninitialized player.");
		}
	}
}
