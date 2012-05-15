package it.polimi.dei.provafinale.carcassonne.model.server;

import it.polimi.dei.provafinale.carcassonne.model.MatchHandler;
import it.polimi.dei.provafinale.carcassonne.model.gameinterface.ServerGameInterface;
import it.polimi.dei.provafinale.carcassonne.model.player.PlayerColor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class CarcassonneServer {

	private final int MAX_PLAYER_NUMBER = 5;
	private final int TIMEOUT = 10 * 1000;

	private ServerSocket serverSocket;
	private Vector<PlayerConnection> currentPlayerConn;
	private Thread timer;
	private Map<String, ServerGameInterface> interfaces;

	public CarcassonneServer(int port) {
		try {
			serverSocket = new ServerSocket(port);
			showMessage("Server started.");
		} catch (IOException ioe) {
			showMessage("Error while initializing server:\n" + ioe);
		}
		interfaces = new HashMap<String, ServerGameInterface>();
	}

	public void startRequestMonitor() {
		// Monitor incoming requests
		showMessage("Waiting for requests.");
		while (true) {

			try {
				Socket playerSocket = serverSocket.accept();

				ObjectInputStream input = new ObjectInputStream(
						playerSocket.getInputStream());
				ObjectOutputStream output = new ObjectOutputStream(
						playerSocket.getOutputStream());

				PlayerConnection pc = new PlayerConnection(playerSocket,
						output, input);

				String request[] = ((String) input.readObject()).split(":");

				if (request[0].equals("connetti")) {
					connectPlayer(pc);
				} else if (request[0].equals("riconnetti")) {
					reconnectPlayer(pc, request[1]);
				}

			} catch (IOException ioe) {
				showMessage("Error while monitoring request:\n" + ioe);
			} catch (ClassNotFoundException cnfe) {
				showMessage("Error while monitoring requests:\n" + cnfe);
			}
		}
	}

	private void connectPlayer(PlayerConnection pc) {
		if (currentPlayerConn == null) {
			currentPlayerConn = new Vector<PlayerConnection>();

			// Start timer
			timer = new Thread(new Timer());
			timer.start();
		}

		currentPlayerConn.add(pc);
		int size = currentPlayerConn.size();
		showMessage(String.format("Player added to match: %s/%s", size,
				MAX_PLAYER_NUMBER));

		if (size == MAX_PLAYER_NUMBER) {
			showMessage("Creating match...");
			startMatch();
			timer.interrupt();
		}
	}

	private void reconnectPlayer(PlayerConnection pc, String request) {
		String[] split = request.split(",");
		PlayerColor color = PlayerColor.valueOf(split[0].trim());
		String matchName = split[1].trim();
		
		ServerGameInterface sgIf = interfaces.get(matchName);
		sgIf.reconnectPlayer(color, pc);
	}

	private synchronized void startMatch() {
		ServerGameInterface sgInterface = new ServerGameInterface(
				currentPlayerConn);
		currentPlayerConn = null;

		// Create a match handler
		MatchHandler matchHandler = new MatchHandler(sgInterface);
		Thread runner = new Thread(matchHandler);
		runner.start();

		// Add the connection list to connections map
		interfaces.put(sgInterface.getName(), sgInterface);
	}

	private void showMessage(String s) {
		System.out.println(s);
	}

	private class Timer implements Runnable {

		@Override
		public void run() {
			while (true) {
				try {
					Thread.sleep(TIMEOUT);
					if (currentPlayerConn.size() >= 2) {
						showMessage(String.format(
								"Timeout: starting match with %s players",
								currentPlayerConn.size()));
						startMatch();
						break;
					}
				} catch (InterruptedException e) {
					return;
				}
			}
		}
	}
}
