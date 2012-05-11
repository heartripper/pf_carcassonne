package it.polimi.dei.provafinale.carcassonne;

import java.util.ArrayList;

import it.polimi.dei.provafinale.carcassonne.model.card.Card;
import it.polimi.dei.provafinale.carcassonne.model.card.Side;
import it.polimi.dei.provafinale.carcassonne.model.card.SidePosition;
import it.polimi.dei.provafinale.carcassonne.model.entity.EntityType;
import it.polimi.dei.provafinale.carcassonne.model.player.PlayerColor;

import org.junit.*;
import static org.junit.Assert.*;

public class TileTest {

	private Card tile;
	private final String REPRESENTATION = "N=N S=C O=S,B E=S NS=0 NE=0 NO=0 OE=1 SE=0 SO=0";
	
	@Before
	public void setUp(){
		tile = new Card(REPRESENTATION);
	}
	
	@Test
	public void testSides(){
		Side north = tile.getSide(SidePosition.N);
		assertTrue(north.getType() == EntityType.N);
		
		Side south = tile.getSide(SidePosition.S);
		assertTrue(south.getType() == EntityType.C);
		
		Side east = tile.getSide(SidePosition.E);
		assertTrue(east.getType() == EntityType.S);
		
		Side west = tile.getSide(SidePosition.O);
		assertTrue(west.getType() == EntityType.S);
	}
	
	@Test
	public void testLinks(){
		Side north = tile.getSide(SidePosition.N);
		ArrayList<Side> ns = tile.sidesLinkedTo(north);
		assertTrue(ns.size() == 0);
		
		Side south = tile.getSide(SidePosition.S);
		ArrayList<Side> ss = tile.sidesLinkedTo(south);
		assertTrue(ss.size() == 0);
		
		Side east = tile.getSide(SidePosition.E);
		ArrayList<Side> es = tile.sidesLinkedTo(east);
		assertTrue(es.size() == 1);
		
		Side west = tile.getSide(SidePosition.O);
		ArrayList<Side> ws = tile.sidesLinkedTo(west);
		assertTrue(ws.size() == 1);
	}
	
	@Test
	public void testFollowers(){
		PlayerColor follower = tile.getSide(SidePosition.O).getFollower();
		assertTrue(follower == PlayerColor.B);
	}
	
	@Test
	public void testRepresentation(){
		assertTrue(tile.getRepresentation().equals(REPRESENTATION));
	}
}
