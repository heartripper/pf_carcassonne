package it.polimi.dei.provafinale.carcassonne.model;

import it.polimi.dei.provafinale.carcassonne.PlayerColor;

import org.junit.*;
import static org.junit.Assert.*;

public class PlayerTest {
	
	private final int followerNum = 7;
	private Player player;
	PlayerColor color = PlayerColor.R;
	
	@Before
	public void setUp() {
		this.player = new Player(color);
	}

	@Test
	public void testFollowers() {
		for (int i = 0; i < followerNum; i++) {
			assertTrue(player.hasFollowers());
			player.removeFollower();
		}

		assertFalse(player.hasFollowers());
	}

	@Test
	public void testColor() {
		assertTrue(color.equals(player.getColor()));
	}

	@Test
	public void testScore() {
		int[] scores = { 4, 7, 12, 4, 6, 7 };
		int sum = 0;
		
		for(int i : scores){
			player.addScore(i);
			sum += i;
		}
		
		assertTrue(sum == player.getScore());
	}
	
	@Test
	public void activeTest(){
		assertTrue(player.isActive());
		player.setInactive();
		assertFalse(player.isActive());
	}
}