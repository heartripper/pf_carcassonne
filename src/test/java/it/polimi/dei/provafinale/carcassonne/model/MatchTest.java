package it.polimi.dei.provafinale.carcassonne.model;

import java.util.Arrays;
import java.util.List;

import it.polimi.dei.provafinale.carcassonne.Coord;
import it.polimi.dei.provafinale.carcassonne.PlayerColor;

import org.junit.*;
import static org.junit.Assert.*;

public class MatchTest {

	private Match match;
	private int playersNumber = 3;

	@Before
	public void setUp() {
		match = new Match(playersNumber);
	}

	@Test
	public void testPutTile() {
		String[] tileRep = { "N=S S=S W=S E=S NS=0 NE=0 NW=0 WE=0 SE=0 SW=0",
				"N=C S=S W=S E=N NS=0 NE=0 NW=0 WE=0 SE=0 SW=1",
				"N=C S=S W=S E=N NS=0 NE=0 NW=0 WE=0 SE=0 SW=1" };

		Tile[] tiles = toTileArray(tileRep);

		/* Checks that we cannot add a tile in a incompatible position. */
		assertFalse(match.putTile(tiles[2], new Coord(0, 1)));
		/* Checks that we can add a tile in a compatible position. */
		assertTrue(match.putTile(tiles[1], new Coord(0, -1)));
		assertTrue(match.putTile(tiles[0], new Coord(-1, -1)));
		/* Check opposite sides */
		Side s1 = tiles[1].getSide(SidePosition.W);
		Side s2 = tiles[0].getSide(SidePosition.E);
		assertEquals(s1, s2.getOppositeSide());
		assertEquals(s1.getOppositeSide(), s2);
		/* Check entities */
		Entity e1 = s1.getEntity();
		Entity e2 = s2.getEntity();
		assertEquals(e1, e2);
	}

	@Test
	public void completeEntityHandlingTest1() {
		String[] reps = { "N=C S=N W=C E=N NS=0 NE=0 NW=1 WE=0 SE=0 SW=0",
				"N=N S=N W=N E=C NS=0 NE=0 NW=0 WE=0 SE=0 SW=0" };

		Coord[] coords = { new Coord(0, -1), new Coord(-1, -1) };
		Tile[] tiles = toTileArray(reps);

		initMatch(tiles, coords);

		Tile targetTile = tiles[0];

		assertTrue(match.putFollower(targetTile, SidePosition.N, PlayerColor.B));

		Entity e = targetTile.getSide(SidePosition.N).getEntity();
		assertTrue(e.isComplete());

		List<Tile> updates = match.checkForCompletedEntities(targetTile);
		assertTrue(updates.contains(targetTile));
		assertEquals(updates.size(), 1);

		int[] scores = match.getScores();
		int[] expectedScores = { 0, 6, 0 };
		assertTrue(Arrays.equals(scores, expectedScores));
	}

	@Test
	public void completeEntityHandlingTest2() {
		String[] reps = { "N=N S=S W=N E=S NS=0 NE=0 NW=0 WE=0 SE=1 SW=0",
				"N=N S=S W=S E=N NS=0 NE=0 NW=0 WE=0 SE=0 SW=1",
				"N=S S=S W=N E=C NS=1 NE=0 NW=0 WE=0 SE=0 SW=0",
				"N=S S=S W=C E=N NS=1 NE=0 NW=0 WE=0 SE=0 SW=0",
				"N=C S=N W=C E=C NS=0 NE=1 NW=1 WE=1 SE=0 SW=0" };

		Coord[] coords = { new Coord(-1, 0), new Coord(1, 0),
				new Coord(-1, -1), new Coord(1, -1), new Coord(0, -1) };
		Tile[] tiles = toTileArray(reps);

		assertTrue(match.putTile(tiles[0], coords[0]));
		assertTrue(match.putTile(tiles[1], coords[1]));
		assertTrue(match.putTile(tiles[2], coords[2]));
		assertTrue(match.putFollower(tiles[2], SidePosition.E, PlayerColor.R));
		assertTrue(match.putTile(tiles[3], coords[3]));
		assertTrue(match.putFollower(tiles[3], SidePosition.W, PlayerColor.B));
		assertTrue(match.putTile(tiles[4], coords[4]));

		Tile t2 = tiles[2];
		Tile t3 = tiles[3];
		Entity e = tiles[4].getSide(SidePosition.N).getEntity();

		assertTrue(e.isComplete());

		List<Tile> updates = match.checkForCompletedEntities(t2);
		assertTrue(updates.contains(t2));
		assertTrue(updates.contains(t3));
		assertEquals(updates.size(), 2);

		int[] scores = match.getScores();
		int[] expectedScores = { 8, 8, 0 };
		assertTrue(Arrays.equals(scores, expectedScores));
	}

	@Test
	public void putFollowerTest() {
		String[] tileRep = { "N=C S=S W=S E=N NS=0 NE=0 NW=0 WE=0 SE=0 SW=1",
				"N=S S=S W=N E=C NS=1 NE=0 NW=0 WE=0 SE=0 SW=0",
				"N=N S=S W=C E=S NS=0 NE=0 NW=0 WE=0 SE=1 SW=0",

				"N=S S=S W=C E=N NS=1 NE=0 NW=0 WE=0 SE=0 SW=0",
				"N=S S=S W=C E=N NS=1 NE=0 NW=0 WE=0 SE=0 SW=0",
				"N=S S=S W=C E=N NS=1 NE=0 NW=0 WE=0 SE=0 SW=0",
				"N=S S=S W=C E=N NS=1 NE=0 NW=0 WE=0 SE=0 SW=0",
				"N=S S=S W=C E=N NS=1 NE=0 NW=0 WE=0 SE=0 SW=0",
				"N=S S=S W=C E=N NS=1 NE=0 NW=0 WE=0 SE=0 SW=0",
				"N=S S=S W=C E=N NS=1 NE=0 NW=0 WE=0 SE=0 SW=0",
				"N=S S=S W=C E=N NS=1 NE=0 NW=0 WE=0 SE=0 SW=0" };

		Tile[] tiles = toTileArray(tileRep);

		assertTrue(match.putTile(tiles[0], new Coord(0, -1)));

		/* We can't add a follower on a null-entity side */
		assertFalse(match.putFollower(tiles[0], SidePosition.E, PlayerColor.B));
		/* But we can on another side */
		assertTrue(match.putFollower(tiles[0], SidePosition.W, PlayerColor.B));
		/*
		 * We can't put a follower on a side who belongs to an entity which
		 * doesn't accept followers
		 */
		assertTrue(match.putTile(tiles[2], new Coord(-1, -1)));
		assertFalse(match.putFollower(tiles[1], SidePosition.E, PlayerColor.B));

		/* A player who doesn't have followers can't add one */
		for (int i = 0; i < 7; i++) {
			assertTrue(match.putTile(tiles[i + 3], new Coord(0, -2 - i)));
			assertTrue(match.putFollower(tiles[i + 3], SidePosition.W,
					PlayerColor.R));
		}

		assertTrue(match.putTile(tiles[10], new Coord(0, -9)));
		assertFalse(match.putFollower(tiles[10], SidePosition.N, PlayerColor.R));

	}

	@Test
	public void removePlayerTest() {
		String[] reps = { "N=N S=S W=N E=S NS=0 NE=0 NW=0 WE=0 SE=1 SW=0",
				"N=N S=S W=S E=N NS=0 NE=0 NW=0 WE=0 SE=0 SW=1",
				"N=S S=S W=N E=C NS=1 NE=0 NW=0 WE=0 SE=0 SW=0",
				"N=S S=S W=C E=N NS=1 NE=0 NW=0 WE=0 SE=0 SW=0" };

		Coord[] coords = { new Coord(-1, 0), new Coord(1, 0),
				new Coord(-1, -1), new Coord(1, -1) };
		Tile[] tiles = toTileArray(reps);

		assertTrue(match.putTile(tiles[0], coords[0]));
		assertTrue(match.putFollower(tiles[0], SidePosition.E, PlayerColor.B));
		assertTrue(match.putTile(tiles[1], coords[1]));
		assertTrue(match.putTile(tiles[2], coords[2]));
		assertTrue(match.putFollower(tiles[2], SidePosition.E, PlayerColor.R));
		assertTrue(match.putTile(tiles[3], coords[3]));
		assertTrue(match.putFollower(tiles[3], SidePosition.W, PlayerColor.B));
		
		List<Tile> updates;
		
		try{
			updates = match.removePlayer(PlayerColor.B);
			assertTrue(updates.contains(tiles[0]));
			assertTrue(updates.contains(tiles[3]));
			assertEquals(updates.size(), 2);
		}catch(NotEnoughPlayersException e){
			
		}
		
		/*Check that all player blu's followers are removed*/
		for(Tile t : tiles){
			for(SidePosition pos : SidePosition.values()){
				PlayerColor follower = t.getSide(pos).getFollower();
				assertFalse(follower == PlayerColor.B);
			}
		}
		
		/*Check that player rosso's follower is still there*/
		PlayerColor follower = tiles[2].getSide(SidePosition.E).getFollower();
		assertTrue(follower == PlayerColor.R);
		
		/*Check that removed player is not returned as next player*/
		for(int i = 0; i < playersNumber*2; i++){
			assertFalse(match.getNextPlayer() == PlayerColor.B);
		}	
	}

	@Test
	public void removePlayerExceptionTest() {
		//TODO
	}
	
	private Tile[] toTileArray(String[] reps) {
		int size = reps.length;
		Tile[] tiles = new Tile[size];
		for (int i = 0; i < size; i++) {
			tiles[i] = new Tile(reps[i]);
		}
		return tiles;
	}

	private void initMatch(Tile[] tiles, Coord[] coords) {
		if (tiles.length != coords.length) {
			fail();
		}

		for (int i = 0; i < tiles.length; i++) {
			assertTrue(match.putTile(tiles[i], coords[i]));
		}
	}

}
