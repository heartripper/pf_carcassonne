package it.polimi.dei.provafinale.carcassonne.model;

import it.polimi.dei.provafinale.carcassonne.PlayerColor;

import org.junit.*;
import static org.junit.Assert.*;

public class PlayerCircularArrayTest {

	private final int numPlayers = 4;
	private final int inactivePlayerIndex = 2;

	private PlayerCircularArray array;

	@Before
	public void setup() {
		array = new PlayerCircularArray(numPlayers);
	}

	@Test
	public void getSizeTest(){
		assertTrue(array.getSize() == numPlayers);
	}
	
	@Test
	public void getNextTest() {
		/*Sets a player as inactive*/
		PlayerColor color = PlayerColor.valueOf(inactivePlayerIndex);
		array.getByColor(color).setInactive();
		
		/*Assert it jumps the inactive player*/
		for(int i = 0; i < numPlayers; i++){
			Player p = array.getNext();
			assertTrue(p.isActive());
		}
	}

	@Test
	public void getByColorTest() {
		for (int i = 0; i < numPlayers; i++) {
			Player p = array.getNext();
			PlayerColor c = PlayerColor.valueOf(i);
			assertTrue(c.equals(p.getColor()));
		}
	}

	@Test
	public void getScoresTest(){
		int[] scores = {13, 15, 12, 9};
		
		for(int i = 0; i < numPlayers; i++){
			PlayerColor color = PlayerColor.valueOf(i);
			Player p = array.getByColor(color);
			p.addScore(scores[i]);
		}
		
		int[] derivedScores = array.getScores();
		
		for(int i =0; i<numPlayers; i++){
			assertTrue(scores[i] == derivedScores[i]);
		}
	}
}
