package it.polimi.dei.provafinale.carcassonne.model;

import org.junit.*;
import static org.junit.Assert.*;

public class CityTest {

	private City city;
	private EntityType type = EntityType.C;
	
	@Before
	public void setUp(){
		city = new City();
	}
	
	@Test
	public void getTypeTest(){
		assertEquals(city.getType(), type);
	}
	
	@Test
	public void getNonCompleteScoreTest(){
		int numTiles = 5;
		for(int i = 0; i < numTiles; i++){
			Tile t = new Tile("N=N S=N W=C E=C NS=0 NE=0 NW=0 WE=1 SE=0 SW=0");
			city.addMember(t.getSide(SidePosition.E));
			city.addMember(t.getSide(SidePosition.W));
		}
		
		assertEquals(city.getScore(), numTiles);
	}
	
	@Test
	public void getCompleteScoreTest(){
		int numTiles = 6;
		Tile t0 = new Tile("N=N S=N W=N E=C NS=0 NE=0 NW=0 WE=0 SE=0 SW=0");
		Side s0 = t0.getSide(SidePosition.E);
		city.addMember(s0);
		Side previous = s0;
		
		for(int i = 0; i < numTiles; i++){
			Tile t = new Tile("N=N S=N W=C E=C NS=0 NE=0 NW=0 WE=1 SE=0 SW=0");
			Side w = t.getSide(SidePosition.W);
			city.addMember(w);
			w.setOppositeSide(previous);
			previous.setOppositeSide(w);
			Side e = t.getSide(SidePosition.E);
			city.addMember(e);
			previous = e;
		}
		
		Tile tl = new Tile("N=N S=N W=C E=N NS=0 NE=0 NW=0 WE=0 SE=0 SW=0");
		Side sl = tl.getSide(SidePosition.W);
		city.addMember(sl);
		sl.setOppositeSide(previous);
		previous.setOppositeSide(sl);
		
		assertEquals(city.getScore(), 2*(2+numTiles));
	}
}
