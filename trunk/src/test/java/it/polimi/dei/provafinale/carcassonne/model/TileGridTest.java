package it.polimi.dei.provafinale.carcassonne.model;

import it.polimi.dei.provafinale.carcassonne.Coord;
import it.polimi.dei.provafinale.carcassonne.model.Tile;
import it.polimi.dei.provafinale.carcassonne.model.TileGrid;

import org.junit.*;
import static org.junit.Assert.*;

public class TileGridTest {

	private Tile t0;
	private Tile t1;
	private TileGrid tg;

	@Before
	public void setUp() {
		t0 = new Tile("N=N S=C W=S E=S NS=0 NE=0 NW=0 WE=1 SE=0 SW=0");
		t1 = new Tile("N=S S=S W=S E=S NS=0 NE=0 NW=1 WE=0 SE=1 SW=0");

		tg = new TileGrid();
		tg.putTile(t0, new Coord(0, 0));
	}

	@Test
	public void getTileTest() {
		Tile t = tg.getTile(new Coord(0, 0));
		assertTrue(t0 == t);
	}

	@Test
	public void getTileNeighborTest() {
		tg.putTile(t1, new Coord(0, 1));
		Tile neighbor = tg.getTileNeighbor(t0, SidePosition.N);
		assertTrue(neighbor == t1);
	}

	@Test
	public void testPositionAvailable() {
		Tile c2 = new Tile("N=S S=S W=N E=N NS=1 NE=0 NW=0 WE=0 SE=0 SW=0");
		boolean available = tg.hasAPlaceFor(c2);
		assertFalse(available);
		c2.rotate();
		available = tg.hasAPlaceFor(c2);
		assertTrue(available);
	}

	@Test
	public void neighborPresenceTest() {
		assertTrue(tg.hasNeighborForCoord(new Coord(1, 0)));
		assertFalse(tg.hasNeighborForCoord(new Coord(0, 2)));
		tg.putTile(t1, new Coord(0, 1));
		assertTrue(tg.hasNeighborForCoord(new Coord(0, 2)));
	}

	@Test
	public void tileCheckTest() {
		// Do not add if a position is already taken;
		boolean compatible = tg.isTileCompatible(t1, new Coord(0, 0));
		assertFalse(compatible);
		// Do not add if card is not compatible with position
		compatible = tg.isTileCompatible(t1, new Coord(0, 1));
		assertFalse(compatible);
		// Add card if position is correct
		compatible = tg.isTileCompatible(t1, new Coord(1, 0));
		assertTrue(compatible);
	}

	@Test
	public void putTileTest() {
		Coord c = new Coord(0, 1);
		tg.putTile(t1, c);
		assertTrue(tg.getTile(c) == t1);
	}

}
