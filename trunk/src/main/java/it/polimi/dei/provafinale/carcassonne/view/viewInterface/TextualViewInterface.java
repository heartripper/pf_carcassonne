package it.polimi.dei.provafinale.carcassonne.view.viewInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import it.polimi.dei.provafinale.carcassonne.controller.client.ClientController;
import it.polimi.dei.provafinale.carcassonne.model.gameinterface.Message;
import it.polimi.dei.provafinale.carcassonne.model.gameinterface.MessageType;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.player.PlayerColor;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.tile.Tile;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.tile.TileGrid;
import it.polimi.dei.provafinale.carcassonne.model.textinterface.TileGridRepresenter;

public class TextualViewInterface implements ViewInterface {

	private TileGridRepresenter representer;
	private BufferedReader input;
	private PlayerColor currentPlayerColor;
	private boolean handleSinglePlayer = false;
	
	public TextualViewInterface() {
		InputStreamReader reader = new InputStreamReader(System.in);
		input = new BufferedReader(reader);
	}

	@Override
	public void initialize(TileGrid grid, int numPlayers,
			PlayerColor clientColor) {
		this.representer = new TileGridRepresenter(grid);
		String format = "Match starts: %s players%s.\n";
		String clientColorStr = "";
		if(clientColor != null){
			handleSinglePlayer = true;
			clientColorStr = ", you are player " + clientColor.getFullName();
		}
		printMessage(String.format(format, numPlayers, clientColorStr));
	}

	@Override
	public void updateGridRepresentation() {
		printMessage("Grid:");
		printMessage(representer.getRepresentation());
	}

	@Override
	public void updateCurrentTile(String rep) {
		if(rep == null){
			return;
		}
		printMessage("Your card: ");
		Tile c = new Tile(rep);
		printMessage(TileGridRepresenter.getTileRepresentation(c));
	}

	@Override
	public void updateScore(String msg) {
		printMessage("Score: " + msg);
	}

	@Override
	public void showNotify(String msg) {
		printMessage(msg);
	}

	@Override
	public void setUIActive(boolean enabled) {
		if(!enabled){
			return;
		}

		while(true){
			String pre = "";
			if(!handleSinglePlayer){
				pre = String.format("(%s) ", currentPlayerColor);
			}
			printMessage(pre + "Please insert command");
			String line;
			try{
			line = input.readLine();
			}catch(IOException e){
				printMessage("Error reading input");
				continue;
			}
			
			if(line == null){
				continue;
			}
			
			Message request;
			if(line.equals("rotate")){
				request = new Message(MessageType.ROTATE, null);
			}else if(line.matches("[-]??[0-9]+,[-]??[0-9]+")){
				request = new Message(MessageType.PLACE, line);					
			}else if(line.matches("[NESW]")){
				request = new Message(MessageType.FOLLOWER, line);
			}else if(line.equals("pass")){
				request = new Message(MessageType.PASS, null);
			}else{
				printMessage("Command not understood.");
				continue;
			}
			
			ClientController.getCurrentMatchController().sendMessage(request);
			return;
		}
	}

	@Override
	public void setCurrentPlayer(PlayerColor color) {
		this.currentPlayerColor = color;
		printMessage(String.format("Player %s turn.\n", color));
	}
	
	// Helper
	private void printMessage(String msg) {
		System.out.println(msg);
	}
}
