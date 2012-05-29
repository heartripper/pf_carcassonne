package it.polimi.dei.provafinale.carcassonne.model;

import org.junit.*;
import static org.junit.Assert.*;

public class EntityFactoryTest {

	@Test
	public void testEntityCreation(){
		Entity c = EntityFactory.createByType(EntityType.C);
		assertTrue(c instanceof City);
		
		Entity r = EntityFactory.createByType(EntityType.S);
		assertTrue(r instanceof Road);
	}
}
