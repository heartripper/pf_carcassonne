package it.polimi.dei.provafinale.carcassonne;

import it.polimi.dei.provafinale.carcassonne.model.MatchHandler;
import it.polimi.dei.provafinale.carcassonne.model.gameinterface.TextualInterface;

public class MainClass {
	public static void main(String[] args) {
//		GameEngine.getInstance().startGame();
		
		TextualInterface textIf = new TextualInterface(System.out, System.in);
		MatchHandler mh = new MatchHandler(textIf);
		mh.startGame();
	}
}
