package it.polimi.dei.provafinale.carcassonne.view.viewInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import it.polimi.dei.provafinale.carcassonne.controller.client.MatchController;
import it.polimi.dei.provafinale.carcassonne.model.gameinterface.Message;
import it.polimi.dei.provafinale.carcassonne.model.gameinterface.MessageType;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.card.Card;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.card.TileGrid;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.player.PlayerColor;
import it.polimi.dei.provafinale.carcassonne.model.textinterface.TileGridRepresenter;

public class TextualViewInterface implements ViewInterface {

	private TileGridRepresenter representer;
	private BufferedReader input;

	public TextualViewInterface() {
		InputStreamReader reader = new InputStreamReader(System.in);
		input = new BufferedReader(reader);
	}

	@Override
	public void initialize(TileGrid grid, int numPlayers,
			PlayerColor clientColor) {
		this.representer = new TileGridRepresenter(grid);
		String format = "Match starts: %s players, you are player %s.\n";
		String playerFullName = clientColor.getFullName();
		printMessage(String.format(format, numPlayers, playerFullName));
	}

	@Override
	public void updateGridRepresentation() {
		printMessage("Grid:");
		printMessage(representer.getRepresentation());
	}

	@Override
	public void updateCurrentTile(String rep) {
		printMessage("Your card: ");
		Card c = new Card(rep);
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
			printMessage("Please insert command");
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
			
			MatchController.getCurrentMatchController().sendMessage(request);
			return;
		}
	}

	// Helper
	private void printMessage(String msg) {
		System.out.println(msg);
	}

}
