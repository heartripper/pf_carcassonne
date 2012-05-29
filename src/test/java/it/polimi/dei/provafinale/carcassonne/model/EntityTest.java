package it.polimi.dei.provafinale.carcassonne.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import it.polimi.dei.provafinale.carcassonne.PlayerColor;

import org.junit.*;
import static org.junit.Assert.*;

public class EntityTest {

	private Entity entity;
	private EntityType type = EntityType.C;

	@Before
	public void setUp() {
		entity = EntityFactory.createByType(type);
	}

	@Test
	public void acceptFollowerTest() {
		assertTrue(entity.acceptFollowers());
		Side s = new Side(null, type);
		s.setFollower(PlayerColor.R);
		entity.addMember(s);
		assertFalse(entity.acceptFollowers());
	}

	@Test
	public void addMemberTest(){
		Side s = new Side(null, type);
		entity.addMember(s);
		assertTrue(entity.getMembers().contains(s));
		assertTrue(entity.getMembers().size() == 1);
	}
	
	@Test
	public void countFollowersTest() {
		int numFollowers = 5;
		for (int i = 0; i < numFollowers; i++) {
			Side s = new Side(null, type);
			entity.addMember(s);
			s.setFollower(PlayerColor.valueOf(i));
		}

		int[] expected = { 1, 1, 1, 1, 1 };
		Arrays.equals(entity.countFollowers(5), expected);
	}

	@Test
	public void encloseTest() {
		Entity entity2 = EntityFactory.createByType(type);
		int entity1sides = 3;
		int entity2sides = 6;
		
		for (int i = 0; i < entity1sides; i++) {
			Side s = new Side(null, type);
			entity.addMember(s);
		}
		
		for (int i = 0; i < entity2sides; i++) {
			Side s = new Side(null, type);
			entity2.addMember(s);
		}

		List<Side> oldEntity1 = new ArrayList<Side>(entity.getMembers());
		List<Side> oldEntity2 = new ArrayList<Side>(entity2.getMembers());

		entity.enclose(entity2);

		if (entity2sides > entity1sides) {
			List<Side> newSides = entity2.getMembers();
			assertTrue(newSides.containsAll(oldEntity1));
			assertTrue(newSides.containsAll(oldEntity2));
			assertTrue(newSides.size() == oldEntity1.size() + oldEntity2.size());
		} else {
			List<Side> newSides = entity.getMembers();
			assertTrue(newSides.containsAll(oldEntity1));
			assertTrue(newSides.containsAll(oldEntity2));
			assertTrue(newSides.size() == oldEntity1.size() + oldEntity2.size());			
		}
	}

	@Test
	public void completeTest() {
		Side previous = null;
		int numSides = 4;
		for (int i = 0; i < numSides; i++) {
			Side s = new Side(null, type);
			entity.addMember(s);
			if (previous != null) {
				previous.setOppositeSide(s);
				s.setOppositeSide(previous);
				previous = null;
			} else {
				previous = s;
			}
		}
		assertTrue(entity.isComplete());
	}

	@Test
	public void notCompleteTest() {
		Side previous = null;
		int numSides = 5;
		for (int i = 0; i < numSides; i++) {
			Side s = new Side(null, type);
			entity.addMember(s);
			if (previous != null) {
				previous.setOppositeSide(s);
				s.setOppositeSide(previous);
				previous = null;
			} else {
				previous = s;
			}
		}
		assertFalse(entity.isComplete());
	}

	@Test
	public void removeFollowersTest() {
		int numFollowers = 5;
		for (int i = 0; i < numFollowers; i++) {
			Side s = new Side(null, type);
			entity.addMember(s);
			s.setFollower(PlayerColor.valueOf(i));
		}

		entity.removeFollowers();
		int[] expected = { 0, 0, 0, 0, 0 };
		assertTrue(Arrays.equals(entity.countFollowers(5), expected));
	}
}
