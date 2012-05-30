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
	public void getMemberTest() {
		int numSides = 12;
		for (int i = 0; i < numSides; i++) {
			Side s = new Side(null, type);
			entity.addMember(s);
		}

		List<Side> sides = entity.getMembers();
		assertEquals(sides.size(), numSides);
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
	public void addMemberTest() {
		Side s = new Side(null, type);
		entity.addMember(s);
		assertTrue(entity.getMembers().contains(s));
		assertTrue(entity.getMembers().size() == 1);

		/* Check that a side does'nt get added twice */
		entity.addMember(s);
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
	public void encloseTest1() {
		/* Test enclosing a small entity in a bigger one */
		encloseTestBase(10, 5);
	}

	@Test
	public void encloseTest2() {
		/* Test enclosing a big entity in a small one */
		encloseTestBase(3, 20);
	}

	private void encloseTestBase(int e1Sides, int e2Sides) {
		Entity entity2 = EntityFactory.createByType(type);

		for (int i = 0; i < e1Sides; i++) {
			Side s = new Side(null, type);
			entity.addMember(s);
			s.setEntity(entity);
		}

		/* Put a follower on last member of entity */
		entity.getMembers().get(e1Sides - 1).setFollower(PlayerColor.R);

		for (int i = 0; i < e2Sides; i++) {
			Side s = new Side(null, type);
			entity2.addMember(s);
			s.setEntity(entity2);
		}

		List<Side> oldEntity1 = new ArrayList<Side>(entity.getMembers());
		List<Side> oldEntity2 = new ArrayList<Side>(entity2.getMembers());

		Entity newEntity = entity.enclose(entity2);

		List<Side> newSides = newEntity.getMembers();
				
		/* Check that new entity has all members of previous two entities */
		assertTrue(newSides.containsAll(oldEntity1));
		assertTrue(newSides.containsAll(oldEntity2));
		assertTrue(newSides.size() == oldEntity1.size() + oldEntity2.size());
		
		/* Check that all sides have newEntity as entity */
		for (Side s : newSides) {
			assertEquals(s.getEntity(), newEntity);
		}

		/* Check that new entity doesn't accept followers as entity didn't */
		assertFalse(newEntity.acceptFollowers());
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

		/* Remove followers of just one color */
		entity.removeFollowers(PlayerColor.R);
		int[] expected = { 0, 1, 1, 1, 1 };
		assertTrue(Arrays.equals(entity.countFollowers(5), expected));

		/* remove all followers */
		entity.removeFollowers(null);
		int[] expected1 = { 0, 0, 0, 0, 0 };
		assertTrue(Arrays.equals(entity.countFollowers(5), expected1));
	}
}
