package it.polimi.dei.provafinale.carcassonne.model.gameinterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

import it.polimi.dei.provafinale.carcassonne.model.Coord;
import it.polimi.dei.provafinale.carcassonne.model.card.Card;
import it.polimi.dei.provafinale.carcassonne.model.card.SidePosition;
import it.polimi.dei.provafinale.carcassonne.model.card.TileGrid;
import it.polimi.dei.provafinale.carcassonne.model.player.PlayerColor;

public class TextualInterface implements GameInterface {

	private boolean cardAdded = false;

	private TileGrid grid;
	private PlayerColor currentPlayer;
	private Card currentCard;

	private PrintStream out;
	private BufferedReader in;

	public TextualInterface(PrintStream out, InputStream in) {
		this.out = out;
		this.in = new BufferedReader(new InputStreamReader(in));
		this.grid = new TileGrid();
	}

	@Override
	public int askPlayerNumber() {
		int num = 0;

		while (true) {
			out.println("Please insert a number between 2 and 5:");
			try {
				num = Integer.parseInt(in.readLine());
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
				MessageType type;
				String payload;
				if (line.equals("ruota") && !cardAdded) {
					type = MessageType.ROTATION;
					payload = null;
				} else if (line.equals("passo") && cardAdded) {
					type = MessageType.PASS;
					payload = null;
				} else if (!cardAdded
						&& line.matches("[-]??[0-9]+,[-]??[0-9]+")) {
					type = MessageType.PUT_TILE;
					payload = line;
				} else if (line.matches("[NESO]")) {
					type = MessageType.PUT_FOLLOWER;
					payload = line;
				} else {
					out.println("Invalid command.");
					continue;
				}

				// Return response
				return new Message(type, payload);
			} catch (IOException e) {
				out.println("Error reading input.");
			}
		}
	}

	@Override
	public void sendPlayer(PlayerColor color, Message msg) {
		switch (msg.type) {
		case ROTATION:
			currentCard = new Card(msg.payload);
			out.printf("(%s) Tile rotated:\n%s\n", color, currentCard);
			return;

		case PROX:
			currentCard = new Card(msg.payload);
			cardAdded = false;
			out.printf("(%s) Your card:\n%s", color, currentCard);
			return;

		case INVALID_MOVE:
			if (cardAdded)
				out.printf("(%s) Invalid position for follower.\n", color);
			else
				out.printf("(%s) Invalid position for tile.\n", color);
			return;
		}
	}

	@Override
	public void sendAllPlayer(Message msg) {
		switch (msg.type) {
		case INIT:
			Card c0 = new Card(msg.payload);
			grid.putTile(c0, new Coord(0, 0));
			return;

		case TURN:
			out.println(grid);
			currentPlayer = PlayerColor.valueOf(msg.payload);
			out.printf("New turn: Player %s.\n", currentPlayer);
			return;

		case UPDATE:
			if (cardAdded) {
				out.printf("Player %s added follower.\n", currentPlayer);
				
				int index = msg.payload.indexOf(",");
				String spos = String.valueOf(msg.payload.charAt(index-3));
				String scol = String.valueOf(msg.payload.charAt(index+1));
				
				SidePosition pos = SidePosition.valueOf(spos);
				PlayerColor col = PlayerColor.valueOf(scol);
				
				currentCard.addFollower(pos, col);
				
			} else {
				String[] split = msg.payload.split(",");
				//TODO ask to fix protocol
				String sx;
				String sy;
				
				if(split.length == 4){
					sx = split[2];
					sy = split[3];
				}else{
					sx = split[1];
					sy = split[2];
				}
				
				int x = Integer.parseInt(sx.trim());
				int y = Integer.parseInt(sy.trim());
				grid.putTile(currentCard, new Coord(x, y));
				System.out.printf("Player %s added tile.\n", currentPlayer);
				cardAdded = true;
			}
			return;
		}
	}
}
