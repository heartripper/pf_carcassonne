package it.polimi.dei.provafinale.carcassonne;

import it.polimi.dei.provafinale.carcassonne.model.MatchHandler;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.Coord;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.Match;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.card.Card;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.card.SidePosition;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.card.TileGrid;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.entity.Entity;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.player.PlayerColor;
import it.polimi.dei.provafinale.carcassonne.model.textinterface.TextualInterface;

public class MainClass {
	public static void main(String[] args) {
		TextualInterface textIf = new TextualInterface(System.out, System.in);
		MatchHandler mh = new MatchHandler(textIf);
		mh.startGame();
	}
}
