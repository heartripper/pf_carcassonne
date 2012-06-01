package it.polimi.dei.provafinale.carcassonne.model;

import it.polimi.dei.provafinale.carcassonne.Coord;
import it.polimi.dei.provafinale.carcassonne.PlayerColor;

import java.util.Arrays;

import org.junit.*;
import static org.junit.Assert.*;

public class EntityReportTest {

	@Test
	public void entityReportTest() {
		int playerNumber = 3;
		Match match = new Match(playerNumber);
		
		String[] reps = { "N=N S=S W=N E=S NS=0 NE=0 NW=0 WE=0 SE=1 SW=0",
				"N=N S=S W=S E=N NS=0 NE=0 NW=0 WE=0 SE=0 SW=1",
				"N=S S=S W=N E=C NS=1 NE=0 NW=0 WE=0 SE=0 SW=0",
				"N=S S=S W=C E=N NS=1 NE=0 NW=0 WE=0 SE=0 SW=0",
				"N=C S=N W=C E=C NS=0 NE=1 NW=1 WE=1 SE=0 SW=0" };

		Coord[] coords = { new Coord(-1, 0), new Coord(1, 0),
				new Coord(-1, -1), new Coord(1, -1), new Coord(0, -1) };
		Tile[] tiles = new Tile[reps.length];
		
		for(int i = 0; i < tiles.length; i++){
			tiles[i] = new Tile(reps[i]);
		}

		assertTrue(match.putTile(tiles[0], coords[0]));
		assertTrue(match.putTile(tiles[1], coords[1]));
		assertTrue(match.putTile(tiles[2], coords[2]));
		assertTrue(match.putFollower(tiles[2], SidePosition.E, PlayerColor.V));
		assertTrue(match.putTile(tiles[3], coords[3]));
		assertTrue(match.putFollower(tiles[3], SidePosition.W, PlayerColor.B));
		assertTrue(match.putTile(tiles[4], coords[4]));

		Entity e = tiles[4].getSide(SidePosition.N).getEntity();
		EntityReport er = new EntityReport(e, playerNumber);
		int[] expectedScores = {0,8,8};
		int[] expectedFollowers = {0,1,1};
		assertTrue(Arrays.equals(er.getScores(), expectedScores));
		assertTrue(Arrays.equals(er.getFollowers(), expectedFollowers));
		
	}
}
