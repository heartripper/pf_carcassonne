package it.polimi.dei.provafinale.carcassonne.model;

import org.junit.*;
import static org.junit.Assert.*;

public class SideConnectionTest {

	private SideConnection connection;
	private Side s1;
	private Side s2;
	private EntityType type = EntityType.C;
	
	@Before
	public void setUp(){
		s1 = new Side(null, type);
		s2 = new Side(null, type);
		connection = new SideConnection(s1, s2);
	}
	
	@Test
	public void equalsTest(){
		SideConnection c2 = new SideConnection(s2, s1);
		assertTrue(connection.equals(connection));
		assertTrue(connection.equals(c2));
	}
	
}
