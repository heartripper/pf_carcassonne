package it.polimi.dei.provafinale.carcassonne.model.client;

import it.polimi.dei.provafinale.carcassonne.model.Coord;
import it.polimi.dei.provafinale.carcassonne.model.card.Card;
import it.polimi.dei.provafinale.carcassonne.model.card.SidePosition;
import it.polimi.dei.provafinale.carcassonne.model.card.TileGrid;
import it.polimi.dei.provafinale.carcassonne.model.player.PlayerColor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class CarcassonneClient {

	private final int MAX_RECONNECT_ATTEMPTS = 10;

	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;

	private String address;
	private int port;
	private String matchName;

	private PlayerColor color;
	private TileGrid grid;
	private Card currentTile;
	
	private BufferedReader input;

	public CarcassonneClient(String address, int port) {
		this.address = address;
		this.port = port;
		this.grid = new TileGrid();
		this.input = new BufferedReader(new InputStreamReader(System.in));

		connect();
	}

	public void connect() {
		try {
			socket = new Socket(address, port);
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());

			sendToServer("connetti");

		} catch (IOException e) {
			System.out.println("Comunication error.");
		}
	}

	public void start() {
		if(in == null)
			return;
		
		String[] startMsg = readFromServer();

		if (startMsg[0].equals("inizio")) {
			initialize(startMsg[1]);
			System.out.println("Match joined. You are player " + color);
		} else {
			protocolOrderError("inizio", startMsg[0]);
		}

		boolean endGame = false;
		String[] msg = null;
		while (!endGame) {
			if (msg == null)
				msg = readFromServer();

			if (msg[0].equals("turno")) {
				PlayerColor col = PlayerColor.valueOf(msg[1].trim());
				System.out.println("New turn:");
				System.out.println(grid);
				if (col == color) {
					manageMyTurn();
					msg = null;
				} else {
					msg = manageOtherPlayerTurn(col);
				}
			} else if (msg[0].equals("fine")) {
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

		System.out.println(card);

		grid.putTile(new Card(card), new Coord(0, 0));
		matchName = name;
		color = PlayerColor.valueOf(col);
	}

	// Player turn management
	private void manageMyTurn() {
		String[] msg = readFromServer();
		if (!msg[0].equals("prox")) {
			protocolOrderError("prox", msg[0]);
		}

		currentTile = new Card(msg[1].trim());
		System.out.println("It's your turn; your card:");
		System.out.println(currentTile);

		boolean endTurn = false, cardAdded = false;
		while (!endTurn) {
			String command = readFromPlayer();
			if (command.equals("ruota") && !cardAdded) {
				manageCardRotation();
			} else if (command.equals("passo")) {
				managePass();
				endTurn = true;
			} else if (command.matches("[-]??[0-9]+,[-]??[0-9]+")) {
				cardAdded = manageCardPositioning(command);
			} else if (command.length() == 1 && cardAdded) {
				if (manageCoinPositioning(command))
					endTurn = true;
			} else {
				System.out.println("Comando inserito non valido.");
			}
		}
	}

	private void manageCardRotation() {
		sendToServer("ruota");
		String[] response = readFromServer();
		if (!response[0].equals("ruotata")) {
			protocolOrderError("ruotata", response[0]);
		}
		currentTile = new Card(response[1].trim());
		System.out.println("Rotated card:\n" + currentTile);
	}

	private void managePass() {
		sendToServer("passo");
	}

	private boolean manageCardPositioning(String command) {
		String[] split = command.split(",");
		int x = Integer.parseInt(split[0]);
		int y = Integer.parseInt(split[1]);
		Coord coord = new Coord(x,y);
		
		sendToServer(String.format("posiziona: %s, %s", x, y));
		String[] response = readFromServer();

		if (response[0].equals("mossa non valida")) {
			System.out
					.println("Card is not compatible with the position you entered");
			return false;
		} else if (response[0].equals("aggiorna")) {
			grid.putTile(currentTile, coord);
			System.out.printf("Card put into (%s,%s).\n", x, y);
			return true;
		} else {
			protocolOrderError("mossa non valida' or 'aggiorna", response[0]);
			return false;
		}
	}

	private boolean manageCoinPositioning(String command) {
		SidePosition pos;
		try {
			pos = SidePosition.valueOf(command);
		} catch (Exception e) {
			System.out.println("Error: invalid side.");
			return false;
		}

		sendToServer(String.format("pedina: %s", pos));
		String[] response = readFromServer();

		if (response[0].equals("mossa non valida")) {
			System.out.println("You can't add a coin on that side.");
			return false;
		} else if (response[0].equals("aggiorna")) {
			currentTile.addFollower(pos, color);
			return true;
		} else {
			protocolOrderError("mossa non valida' or 'aggiorna", response[0]);
			return false;
		}
	}

	private String[] manageOtherPlayerTurn(PlayerColor col) {
		System.out.printf("Player %s turn.\n", col);
		String[] update1 = readFromServer();

		Card tile;
		
		if (update1[0].equals("aggiorna")) {
			String[] payload = update1[1].split(",");
			tile = new Card(payload[0].trim());
			int x = Integer.parseInt(payload[1].trim());
			int y = Integer.parseInt(payload[2].trim());
			Coord coord = new Coord(x, y);

			grid.putTile(tile, coord);
			System.out.println(String.format("Player %s put card\n%s in %s",
					col, tile, coord));
		}else if(update1[0].equals("turno")){
			return update1;
		}else{
			protocolOrderError("aggiorna' or 'turno", update1[0]);
			return null;
		}

		String[] update2 = readFromServer();
		if (update2[0].equals("turno")) {
			return update2;
		} else if (update2[0].equals("aggiorna")) {
			int index = update2[1].indexOf(",");
			String spos = String.valueOf(update2[1].charAt(index - 3));
			SidePosition pos = SidePosition.valueOf(spos);
			tile.addFollower(pos, col);
			System.out.println("Player " + col + "added coin.");
		} else {
			protocolOrderError("turno' or 'aggiorna", update2[0]);
		}
		return null;
	}

	private void sendToServer(String msg) {
		try {
			out.writeObject(msg);
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
		for (int i = 0; i < MAX_RECONNECT_ATTEMPTS; i++) {
			if (attemptToReconnect())
				return;
		}

		System.out.println("Failed to reconnect.");
		System.exit(1);
	}

	private boolean attemptToReconnect() {
		try {
			socket = new Socket(address, port);
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());

			sendToServer(String.format("riconnetti: %s, %s", color, matchName));
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