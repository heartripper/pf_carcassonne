package it.polimi.dei.provafinale.carcassonne;

import it.polimi.dei.provafinale.carcassonne.model.Coord;
import it.polimi.dei.provafinale.carcassonne.model.card.Card;
import it.polimi.dei.provafinale.carcassonne.model.card.TileGrid;

import org.junit.*;
import static org.junit.Assert.*;

public class TileGridTest {

	private Card c0;
	private Card c1;
	private TileGrid tg;
	
	@Before
	public void setUp(){
		c0 = new Card("N=N S=C W=S E=S NS=0 NE=0 NW=0 WE=1 SE=0 SW=0");
		c1 = new Card("N=S S=S W=S E=S NS=0 NE=0 NW=1 WE=0 SE=1 SW=0");
		
		tg = new TileGrid();
		tg.putTile(c0, new Coord(0,0));
	}
	
	@Test
	public void testPositionCheck(){
		//Do not add if a position is already taken;
		boolean compatible = tg.isTileCompatible(c1, new Coord(0,0));
		assertFalse(compatible);
		//Do not add if card is not compatible with position
		compatible = tg.isTileCompatible(c1, new Coord(0,1));
		assertFalse(compatible);
		//Add card if position is correct
		compatible = tg.isTileCompatible(c1, new Coord(1,0));
		assertTrue(compatible);
	}
	
	@Test
	public void testPositionAvailable(){
		Card c2 = new Card("N=S S=S W=N E=N NS=1 NE=0 NW=0 WE=0 SE=0 SW=0");
		boolean available = tg.hasAPlaceFor(c2);
		assertFalse(available);
		c2.rotate();
		available = tg.hasAPlaceFor(c2);
		assertTrue(available);
	}
	
}
