package it.polimi.dei.provafinale.carcassonne.model;

import it.polimi.dei.provafinale.carcassonne.Coord;

import org.junit.*;
import static org.junit.Assert.*;

public class MatchTest {

	private Match match;
	private int playersNumber;
	private String[] tileRep = {
			"N=N S=C W=S E=S NS=0 NE=0 NW=0 WE=1 SE=0 SW=0",
			"N=S S=S W=S E=S NS=0 NE=0 NW=0 WE=0 SE=0 SW=0",
			"N=C S=S W=S E=N NS=0 NE=0 NW=0 WE=0 SE=0 SW=1",
			"N=C S=N W=N E=C NS=0 NE=0 NW=0 WE=0 SE=0 SW=0",
			"N=C S=N W=N E=C NS=0 NE=0 NW=0 WE=0 SE=0 SW=0",
			"N=C S=S W=S E=N NS=0 NE=0 NW=0 WE=0 SE=0 SW=1" };

	private Tile[] tiles;

	@Before
	public void setUp() {
		match = new Match(playersNumber);
		tiles = new Tile[tileRep.length];
		for (int i = 0; i < tileRep.length; i++) {
			tiles[i] = new Tile(tileRep[i]);
		}
	}

	@Test
	public void testPutTile() {
		/* Checks that we cannot add a tile in a incompatible position. */
		assertFalse(match.putTile(tiles[5], new Coord(0, 1)));
		/* Checks that we can add a tile in a compatible position. */
		assertTrue(match.putTile(tiles[2], new Coord(0, -1)));
		assertTrue(match.putTile(tiles[1], new Coord(-1, -1)));
		/* Check opposite sides */
		Side s1 = tiles[1].getSide(SidePosition.E);
		Side s2 = tiles[2].getSide(SidePosition.W);
		assertEquals(s1, s2.getOppositeSide());
		assertEquals(s1.getOppositeSide(), s2);
		/* Check entities */
		Entity e1 = s1.getEntity();
		Entity e2 = s2.getEntity();
		assertEquals(e1, e2);
	}
}
