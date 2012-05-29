package it.polimi.dei.provafinale.carcassonne.model;

import it.polimi.dei.provafinale.carcassonne.Constants;
import it.polimi.dei.provafinale.carcassonne.PlayerColor;

import org.junit.*;
import static org.junit.Assert.*;

public class SideTest {

	private Side side;
	private EntityType type = EntityType.C;
	private PlayerColor color = PlayerColor.B;

	@Before
	public void setUp() {
		this.side = new Side(null, type);
	}

	@Test
	public void followerTest() {
		side.setFollower(color);
		assertTrue(color.equals(side.getFollower()));
	}

	@Test
	public void entityTest() {
		Entity e = EntityFactory.createByType(type);
		side.setEntity(e);
		assertTrue(e.equals(side.getEntity()));
	}

	@Test
	public void getTypeTest() {
		assertTrue(type.equals(side.getType()));
	}

	@Test
	public void toStringTest() {
		assertTrue(side.toString().equals(type.toString()));
		side.setFollower(color);
		String expectedText = type.toString() + Constants.FOLLOWER_SEPARATOR
				+ side.getFollower().toString();
		assertTrue(side.toString().equals(expectedText));
	}
	
	@Test
	public void getOwnerCardTest(){
		assertNull(side.getOwnerCard());
	}
	
	@Test
	public void getOppositeSideTest(){
		assertNull(side.getOppositeSide());
	}
}
