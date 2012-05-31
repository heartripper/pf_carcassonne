package it.polimi.dei.provafinale.carcassonne.controller.server;

import static org.junit.Assert.*;

import java.awt.Color;

import it.polimi.dei.provafinale.carcassonne.PlayerColor;
import it.polimi.dei.provafinale.carcassonne.controller.Message;
import it.polimi.dei.provafinale.carcassonne.controller.MessageType;

import org.junit.Before;
import org.junit.Test;

public class MatchHandlerTest {

	private final int playerNumber = 3;

	private Message msg = new Message(MessageType.START, null);
	private Color red;
	private GameInterface gameInterface;
	
	@Before
	public void setUp(){
		
	}
	
	@Test
	public void test() {
		fail("Not yet implemented");
	}

	
	private class fakeGameInterface implements GameInterface{

		@Override
		public int askPlayerNumber() {
			return playerNumber;
		}

		@Override
		public Message readFromPlayer(PlayerColor color)
				throws PlayersDisconnectedException {
			
			return null;
		}

		@Override
		public void sendPlayer(PlayerColor color, Message msg)
				throws PlayersDisconnectedException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void sendAllPlayer(Message msg)
				throws PlayersDisconnectedException {
			// TODO Auto-generated method stub
			
		}
		
	}
}
