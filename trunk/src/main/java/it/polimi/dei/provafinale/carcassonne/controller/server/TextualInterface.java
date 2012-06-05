package it.polimi.dei.provafinale.carcassonne.controller.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

import it.polimi.dei.provafinale.carcassonne.PlayerColor;
import it.polimi.dei.provafinale.carcassonne.controller.Message;
import it.polimi.dei.provafinale.carcassonne.controller.MessageType;
import it.polimi.dei.provafinale.carcassonne.model.Tile;
import it.polimi.dei.provafinale.carcassonne.view.game.TileGridRepresenter;
import it.polimi.dei.provafinale.carcassonne.view.game.TileRepresentationGrid;

public class TextualInterface implements GameInterface {

	private TileRepresentationGrid grid;
	private TileGridRepresenter gridRepresenter;
	private PlayerColor currentPlayer;

	private PrintStream out;
	private BufferedReader in;

	public TextualInterface(PrintStream out, InputStream in) {
		this.out = out;
		this.in = new BufferedReader(new InputStreamReader(in));
		this.grid = new TileRepresentationGrid();
		this.gridRepresenter = new TileGridRepresenter(grid);
	}

	@Override
	public int askPlayerNumber() {
		int num = 0;

		while (true) {
			out.println("Please insert a number between 2 and 5:");
			try {
				String line = in.readLine();
				if (line == null) {
					continue;
				}
				num = Integer.parseInt(line);
				if (num >= 2 && num <= 5) {
					return num;
				}
			} catch (Exception e) {
			}
		}
	}

	@Override
	public Message readFromPlayer(PlayerColor player) {

		while (true) {
			try {
				out.printf("(%s) Please insert command:\n", player);
				String line = in.readLine();
				if (line == null) {
					continue;
				}
				MessageType type;
				String payload;
				if (line.equals("rotate")) {
					type = MessageType.ROTATE;
					payload = null;
				} else if (line.equals("pass")) {
					type = MessageType.PASS;
					payload = null;
				} else if (line.matches("[-]??[0-9]+,[-]??[0-9]+")) {
					type = MessageType.PLACE;
					payload = line;
				} else if (line.matches("[NESW]")) {
					type = MessageType.FOLLOWER;
					payload = line;
				} else {
					out.println("Command not understood.\n");
					continue;
				}

				// Return response
				return new Message(type, payload);
			} catch (IOException e) {
				out.println("Error reading input.\n");
			}
		}
	}

	@Override
	public void sendPlayer(PlayerColor color, Message msg) {
		String message;
		switch (msg.type) {
		case NEXT:
			message = manageNext(msg.payload);
			break;
		case ROTATED:
			message = manageTileRotation(color, msg);
			break;
		case UPDATE_SINGLE:
			message = manageCardPlacing(msg.payload);
			break;
		case INVALID_MOVE:
			message = "Invalid move.\n";
			break;
		default:
			message = String.format(
					"Message '%s' from match handler was not understood",
					msg.type);
		}
		out.printf("(%s) %s", color, message);
	}

	@Override
	public void sendAllPlayer(Message msg) {
		switch (msg.type) {
		case START:
			grid.execUpdate(msg.payload + ", 0, 0");
			return;

		case TURN:
			out.println("\n\nNEW TURN. Grid:");
			out.println(gridRepresenter.getRepresentation());
			currentPlayer = PlayerColor.valueOf(msg.payload);
			out.printf("It's player %s turn.\n", currentPlayer);
			return;

		case UPDATE:
			manageTileUpdate(msg.payload);
			return;

		case SCORE:
			out.println("Turn end. Updated scores: " + msg.payload);
			break;

		case END:
			out.println("End game. Score: " + msg.payload);
			break;
			
		default:
			break;
		}
	}

	// Helpers
	private String manageCardPlacing(String payload) {
		return "Tile added to game.\n";
	}

	private String manageTileRotation(PlayerColor color, Message msg) {
		Tile tile = new Tile(msg.payload);
		String tileRep = TileGridRepresenter.getTileRepresentation(tile);
		return String.format("Tile rotated:\n%s\n", tileRep);
	}

	private String manageNext(String payload) {
		Tile tile = new Tile(payload);
		String tileRep = TileGridRepresenter.getTileRepresentation(tile);
		return String.format("Your card:\n%s\n", tileRep);
	}

	private void manageTileUpdate(String msg) {
		grid.execUpdate(msg);
	}
}