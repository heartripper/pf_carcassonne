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
	private Thread runningThread;
	
	@Before
	public void setUp(){

	}
	
	@Test
	public void test() {

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
		
		public FakeGameInterface(){
			testInput = new MessageBuffer();
			testOutput = new MessageBuffer();
		}
		
	}
}
