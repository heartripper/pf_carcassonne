package it.polimi.dei.provafinale.carcassonne.view;

import it.polimi.dei.provafinale.carcassonne.model.gamelogic.Coord;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.card.Card;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.card.TileGrid;
import it.polimi.dei.provafinale.carcassonne.view.game.GamePanel;
import it.polimi.dei.provafinale.carcassonne.view.game.TilesPanel;

import javax.swing.JFrame;


public class MainClass {
	public static void main(String[] args) {
//		ViewManager.getInstance();
		
		JFrame frame = new JFrame("test");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(600, 600);

//		GamePanel gm = new GamePanel();
//		frame.add(gm);
//		
//		gm.setPlayers(5);
//		gm.getPlayers()[0].setClientPlayer();
		
		frame.setVisible(true);
		
		TileGrid tg = new TileGrid();
		tg.putTile(new Card("N=N S=C W=S E=S NS=0 NE=0 NW=0 WE=1 SE=0 SW=0"), new Coord(0,0));
		tg.putTile(new Card("N=N S=N W=S E=S NS=0 NE=0 NW=0 WE=1 SE=0 SW=0"), new Coord(0,-1));
		tg.putTile(new Card("N=N S=C W=S E=S NS=0 NE=0 NW=0 WE=1 SE=0 SW=0"), new Coord(-1,0));
		TilesPanel tp = new TilesPanel(tg);
		tp.updateRepresentation();
		frame.add(tp);
		
	}
}
