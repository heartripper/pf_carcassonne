package it.polimi.dei.provafinale.carcassonne.model;

import java.util.ArrayList;
import java.util.List;

import org.junit.*;
import static org.junit.Assert.*;

public class TileStackTest {

	private TileStack stack;
	
	@Before
	public void setUp(){
		this.stack = new TileStack();
	}

	@Test
	public void hasMoretilesTest(){
		int numTiles = 55;
		for(int i = 0; i < numTiles; i++){
			assertTrue(stack.hasMoreTiles());
			stack.drawTile();
		}
		
		assertFalse(stack.hasMoreTiles());
	}
	
	@Test
	public void testInitialTile(){
		Tile c = stack.getInitialTile();
		assertEquals(c.toString(), "N=N S=C W=S E=S NS=0 NE=0 NW=0 WE=1 SE=0 SW=0");	
	}
	
	@Test
	public void drawCardTest(){
		List<Tile> drewTiles = new ArrayList<Tile>();
		while(stack.hasMoreTiles()){
			Tile c = stack.drawTile();
			assertFalse(drewTiles.contains(c));
			drewTiles.add(c);
		}
	}
}
