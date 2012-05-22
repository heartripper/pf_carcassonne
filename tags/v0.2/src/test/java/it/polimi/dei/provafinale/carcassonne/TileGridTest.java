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
		c0 = new Card("N=N S=C O=S E=S NS=0 NE=0 NO=0 OE=1 SE=0 SO=0");
		c1 = new Card("N=S S=S O=S E=S NS=0 NE=0 NO=1 OE=0 SE=1 SO=0");
		
		tg = new TileGrid();
		tg.putTile(c0, new Coord(0,0));
	}
	
	@Test
	public void testPositionCheck(){
		//Do not add if a position is already taken;
		boolean added = tg.putTile(c1, new Coord(0,0));
		assertFalse(added);
		//Do not add if card is not compatible with position
		added = tg.putTile(c1, new Coord(0,1));
		assertFalse(added);
		//Add card if position is correct
		added = tg.putTile(c1, new Coord(1,0));
		assertTrue(added);
	}
	
	@Test
	public void testPositionAvailable(){
		Card c2 = new Card("N=S S=S O=N E=N NS=1 NE=0 NO=0 OE=0 SE=0 SO=0");
		boolean available = tg.hasAPlaceFor(c2);
		assertFalse(available);
		c2.rotate();
		available = tg.hasAPlaceFor(c2);
		assertTrue(available);
	}
	
}
