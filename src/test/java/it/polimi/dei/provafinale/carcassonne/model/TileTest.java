package it.polimi.dei.provafinale.carcassonne.model;

import java.util.ArrayList;
import java.util.List;

import it.polimi.dei.provafinale.carcassonne.PlayerColor;
import it.polimi.dei.provafinale.carcassonne.model.EntityType;
import it.polimi.dei.provafinale.carcassonne.model.Side;
import it.polimi.dei.provafinale.carcassonne.model.SidePosition;
import it.polimi.dei.provafinale.carcassonne.model.Tile;

import org.junit.*;
import static org.junit.Assert.*;

public class TileTest {

	private Tile tile;
	private final String REPRESENTATION = "N=N S=C W=S-B E=S NS=0 NE=0 NW=0 WE=1 SE=0 SW=0";
	
	@Before
	public void setUp(){
		tile = new Tile(REPRESENTATION);
	}
	
	@Test
	public void testSides(){
		Side north = tile.getSide(SidePosition.N);
		assertTrue(north.getType() == EntityType.N);
		
		Side south = tile.getSide(SidePosition.S);
		assertTrue(south.getType() == EntityType.C);
		
		Side east = tile.getSide(SidePosition.E);
		assertTrue(east.getType() == EntityType.S);
		
		Side west = tile.getSide(SidePosition.W);
		assertTrue(west.getType() == EntityType.S);
	}
	
	@Test
	public void testLinks(){
		Side north = tile.getSide(SidePosition.N);
		List<Side> ns = tile.sidesLinkedTo(north);
		assertTrue(ns.size() == 0);
		
		Side south = tile.getSide(SidePosition.S);
		List<Side> ss = tile.sidesLinkedTo(south);
		assertTrue(ss.size() == 0);
		
		Side east = tile.getSide(SidePosition.E);
		List<Side> es = tile.sidesLinkedTo(east);
		assertTrue(es.size() == 1);
		
		Side west = tile.getSide(SidePosition.W);
		List<Side> ws = tile.sidesLinkedTo(west);
		assertTrue(ws.size() == 1);
	}
	
	@Test
	public void testFollowers(){
		PlayerColor follower = tile.getSide(SidePosition.W).getFollower();
		assertTrue(follower == PlayerColor.B);
	}
	
	@Test
	public void testRepresentation(){
		assertTrue(tile.toString().equals(REPRESENTATION));
	}
}
