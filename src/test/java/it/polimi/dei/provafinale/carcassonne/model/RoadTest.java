package it.polimi.dei.provafinale.carcassonne.model;

import it.polimi.dei.provafinale.carcassonne.PlayerColor;

import org.junit.*;
import static org.junit.Assert.*;

public class RoadTest {

	private Road road;
	private EntityType type = EntityType.S;
	@Before
	public void setUp(){
		road = new Road();
	}
	
	@Test
	public void getTypeTest(){
		assertEquals(road.getType(), type);
	}
	
	@Test
	public void getScoreTest(){
		int numTiles = 5;
		for(int i = 0; i < numTiles; i++){
			Tile t = new Tile("N=N S=C W=S E=S NS=0 NE=0 NW=0 WE=1 SE=0 SW=0");
			road.addMember(t.getSide(SidePosition.E));
			road.addMember(t.getSide(SidePosition.W));
		}
		
		assertEquals(road.getScore(), numTiles);
	}
}
