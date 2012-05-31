package it.polimi.dei.provafinale.carcassonne.controller.server;

import static org.junit.Assert.*;

import java.awt.Color;

import it.polimi.dei.provafinale.carcassonne.PlayerColor;
import it.polimi.dei.provafinale.carcassonne.controller.Message;
import it.polimi.dei.provafinale.carcassonne.controller.MessageType;
import it.polimi.dei.provafinale.carcassonne.controller.client.MessageBuffer;

import org.junit.Before;
import org.junit.Test;

public class MatchHandlerTest {

	private final int playerNumber = 3;
	private FakeGameInterface fakeInterface;
	
	@Before
	public void setUp(){
		fakeInterface = new FakeGameInterface();
		MatchHandler handler = new MatchHandler(fakeInterface);
		Thread t = new Thread(handler);
		t.start();
		handler.startGame();
	}
	
	@Test
	public void test() {
		Message testRes = fakeInterface.readTestResult();
		assertTrue(testRes.type.equals(MessageType.START));
		assertFalse(!testRes.type.equals(MessageType.START));
	}

	
	private class FakeGameInterface implements GameInterface{
		
		private MessageBuffer testInput;
		private MessageBuffer testOutput;

		@Override
		public int askPlayerNumber() {
			return playerNumber;
		}

		@Override
		public Message readFromPlayer(PlayerColor color)
				throws PlayersDisconnectedException {
			return testInput.read();
		}

		@Override
		public void sendPlayer(PlayerColor color, Message msg)
				throws PlayersDisconnectedException {
			testOutput.write(msg);
		}

		@Override
		public void sendAllPlayer(Message msg)
				throws PlayersDisconnectedException {
			testOutput.write(msg);
		}
		
		public Message readTestResult(){
			return testOutput.read();
		}
		
		public void writeOnBuffer(Message msg){
			testInput.write(msg);
		}
		
	}
}
