package it.polimi.dei.provafinale.carcassonne.controller.client;

import it.polimi.dei.provafinale.carcassonne.model.gamelogic.Coord;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.card.Card;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.card.Side;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.card.SidePosition;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.card.TileGrid;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.player.PlayerColor;
import it.polimi.dei.provafinale.carcassonne.model.textinterface.TileGridRepresenter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class CarcassonneClient {

	private final int maxReconnectAttempts = 10;

	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;

	private String address;
	private int port;
	private String matchName;

	private PlayerColor clientColor;
	private PlayerColor currentPlayer;
	private TileGrid grid;
	private TileGridRepresenter gridRepresenter;

	private BufferedReader input;

	public CarcassonneClient(String address, int port) {
		this.address = address;
		this.port = port;

		this.grid = new TileGrid();
		this.gridRepresenter = new TileGridRepresenter(grid);
		this.input = new BufferedReader(new InputStreamReader(System.in));

		connect();
	}

	public void connect() {
		try {
			socket = new Socket(address, port);
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());

			sendToServer("connect");

		} catch (IOException e) {
			System.out.println("Comunication error.");
		}
	}

	public void start() {
		if (in == null){
			return;
		}

		String[] startMsg = readFromServer();

		if (startMsg[0].equals("start")) {
			initialize(startMsg[1]);
			System.out.println("Match joined. You are player " + clientColor);
		} else {
			protocolOrderError("start", startMsg[0]);
		}

		boolean endGame = false;
		while (!endGame) {
			String[] msg = readFromServer();

			if (msg[0].equals("turn")) {
				currentPlayer = PlayerColor.valueOf(msg[1].trim());
				System.out.println("New turn:");
				System.out.println(gridRepresenter.getRepresentation());
				if (currentPlayer == clientColor) {
					manageMyTurn();
				} else {
					manageOtherPlayerTurn();
				}
			} else if (msg[0].equals("end")) {
				System.out.println("End game;\nScore: " + msg[1]);
				endGame = true;
			} else {
				protocolOrderError("turno' or 'fine", msg[0]);
			}
		}
	}

	private void initialize(String msg) {
		String values[] = msg.split(",");
		String card = values[0].trim();
		String name = values[1].trim();
		String col = values[2].trim();

		grid.putTile(new Card(card), new Coord(0, 0));
		matchName = name;
		clientColor = PlayerColor.valueOf(col);
	}

	// Player turn management
	private void manageMyTurn() {
		String[] msg = readFromServer();
		if (!msg[0].equals("next")) {
			protocolOrderError("next", msg[0]);
		}

		Card tile = new Card(msg[1].trim());

		System.out.println("It's your turn; your card:");
		System.out.println(gridRepresenter.getTileRepresentation(tile));

		boolean endTurn = false, currentTileAdded = false;
		while (!endTurn) {
			String command = readFromPlayer();
			if (command.equals("rotate")) {
				manageCardRotation();
			} else if (command.equals("pass") && currentTileAdded) {
				managePass();
				endTurn = true;
			} else if (command.matches("[-]??[0-9]+,[-]??[0-9]+")) {
				currentTileAdded = manageTilePositioning(command);
			} else if (command.matches("[NESW]")) {
				endTurn = manageFollower(command);
			} else {
				System.out.println("You inserted an invalid command.");
			}
		}
		
		String[] command = readFromServer();
		while(command[0].equals("update")){
			handleUpdate(command[1]);
			command = readFromServer();
		}
		
		if(command[0].equals("score")){
			updateScore(command[1]);
		}else{
			protocolOrderError("update' or 'score", command[0]);
		}
	}

	private void manageCardRotation() {
		sendToServer("rotate");
		String[] response = readFromServer();
		if (!response[0].equals("rotated")) {
			protocolOrderError("rotated", response[0]);
		}
		Card tile = new Card(response[1].trim());
		System.out.println("Rotated card:");
		System.out.println(gridRepresenter.getTileRepresentation(tile));
	}

	private void managePass() {
		sendToServer("pass");
	}

	private boolean manageTilePositioning(String command) {
		String[] split = command.split(",");
		int x = Integer.parseInt(split[0]);
		int y = Integer.parseInt(split[1]);

		sendToServer(String.format("place: %s, %s", x, y));
		String[] response = readFromServer();

		if (response[0].equals("move not valid")) {
			System.out.println("You entered and invalid position for the card.");
			return false;
		} else if (response[0].equals("update")) {
			System.out.println("Tile put at given position.");
			handleUpdate(response[1]);
			return true;
		} else {
			protocolOrderError("move not valid' or 'update", response[0]);
			return false;
		}
	}

	private boolean manageFollower(String command) {
		SidePosition pos = SidePosition.valueOf(command.trim());
		sendToServer(String.format("tile: %s", pos));
		String[] response = readFromServer();

		if (response[0].equals("move not valid")) {
			System.out.println("You can't add a coin on that side.");
			return false;
		} else if (response[0].equals("update")) {
			handleUpdate(response[1]);
			return true;
		} else {
			protocolOrderError("move not valid' or 'update", response[0]);
			return false;
		}
	}

	private void manageOtherPlayerTurn() {
		System.out.printf("Player %s turn.\n", currentPlayer);
		// During other player turn we will receive only updates;
		//The first is the tile added by player.
		//The others are changes caused by player moves.
		String message[] = readFromServer();
		while (message[0].equals("update")) {
			handleUpdate(message[1]);
			message = readFromServer();
		}

		// When score messages arrives, turn has ended.
		if (message[0].equals("score")) {
			updateScore(message[1]);
		} else {
			protocolOrderError("score' or 'update", message[0]);
		}

		return;
	}

	private void updateScore(String string) {
		System.out.println("Score: " + string);
	}

	// Handles update
	private void handleUpdate(String payload) {
		String[] split = payload.split(",");
		Card tile = new Card(split[0].trim());
		int x = Integer.parseInt(split[1].trim());
		int y = Integer.parseInt(split[2].trim());
		Coord coord = new Coord(x, y);
		Card oldTile = grid.getTile(coord);
		if (oldTile == null) {
			grid.putTile(tile, coord);
			System.out.printf("Player %s put tile into %s.\n", currentPlayer,
					coord);
		} else {
			for (SidePosition pos : SidePosition.values()) {
				Side oldSide = oldTile.getSide(pos);
				PlayerColor newFollower = tile.getSide(pos).getFollower();
				oldSide.setFollower(newFollower);
			}
		}
	}

	private void sendToServer(String msg) {
		try {
			out.writeObject(msg);
			out.flush();
		} catch (IOException ioe) {
			System.out.println("Lost connection with server.");
			reconnectOrDie();
		}
	}

	private String[] readFromServer() {
		try {
			String msg = (String) in.readObject();
			return msg.split(":");
		} catch (IOException ioe) {
			System.out.println("Lost connection with server.");
			reconnectOrDie();
			return null;
		} catch (ClassNotFoundException e) {
			System.out.println("Comunication error.");
			return null;
		}
	}

	private String readFromPlayer() {
		try {
			System.out.println("Insert command:");
			String c = input.readLine();
			return c;
		} catch (IOException ioe) {
			System.out.println("Error while reading input.");
			return null;
		}
	}

	private void protocolOrderError(String expected, String received) {
		System.out.println("Comunication error: expecting '" + expected
				+ "' but '" + received + "' arrived.");
		close();
		System.exit(1);
	}

	private void reconnectOrDie() {
		for (int i = 0; i < maxReconnectAttempts; i++) {
			if (attemptToReconnect()){
				return;
			}	
		}

		System.out.println("Failed to reconnect.");
		System.exit(1);
	}

	private boolean attemptToReconnect() {
		try {
			socket = new Socket(address, port);
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());

			sendToServer(String.format("riconnetti: %s, %s", clientColor,
					matchName));
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	private void close() {
		try {
			socket.close();
			out.close();
			in.close();
		} catch (IOException ioe) {
			System.out.println("Lost connection with server.");
		}
	}
}