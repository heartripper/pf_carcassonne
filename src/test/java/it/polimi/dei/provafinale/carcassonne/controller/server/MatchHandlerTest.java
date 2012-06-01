package it.polimi.dei.provafinale.carcassonne.controller.server;

import static org.junit.Assert.*;

import it.polimi.dei.provafinale.carcassonne.Coord;
import it.polimi.dei.provafinale.carcassonne.PlayerColor;
import it.polimi.dei.provafinale.carcassonne.controller.Message;
import it.polimi.dei.provafinale.carcassonne.controller.MessageType;
import it.polimi.dei.provafinale.carcassonne.controller.client.MessageBuffer;
import it.polimi.dei.provafinale.carcassonne.model.SidePosition;

import org.junit.Before;
import org.junit.Test;

public class MatchHandlerTest {

	private final int playerNumber = 3;
	private FakeGameInterface fakeInterface;
	private Thread runningThread;

	@Before
	public void setUp() {
		fakeInterface = new FakeGameInterface();
		MatchHandler match = new MatchHandler(fakeInterface);
		runningThread = new Thread(match);
		runningThread.start();
	}

	@Test
	public void test() {
		
		Message testRes;
		Coord[] targetCoords = { new Coord(0, 1), new Coord(1, 0),
				new Coord(0, -1), new Coord(-1, 0) };

		/* Test the start message. */

		testRes = fakeInterface.readTestResult();
		assertTrue(testRes.type.equals(MessageType.START));

		/* Test that the first messages arrive on the right order. */

		testRes = fakeInterface.readTestResult();
		assertTrue(testRes.type.equals(MessageType.TURN));

		/* Test that the right message arrives. */

		testRes = fakeInterface.readTestResult();
		assertTrue(testRes.type.equals(MessageType.NEXT));

		/* Test on tile rotation. */

		fakeInterface.writeOnBuffer(new Message(MessageType.ROTATE, null));
		testRes = fakeInterface.readTestResult();
		assertTrue(testRes.type.equals(MessageType.ROTATED));

		/* Test: we cannot pass if we have not placed a tile. */

		fakeInterface.writeOnBuffer(new Message(MessageType.PASS, null));
		testRes = fakeInterface.readTestResult();
		assertTrue(testRes.type.equals(MessageType.INVALID_MOVE));

		/*
		 * Test: we cannot place a follower if we have not added a tile on the
		 * grid.
		 */

		fakeInterface.writeOnBuffer(new Message(MessageType.FOLLOWER, "N"));
		testRes = fakeInterface.readTestResult();
		assertTrue(testRes.type.equals(MessageType.INVALID_MOVE));

		/* Test on the addition of a tile. */

		MessageType type = MessageType.PLACE;
		int rotCount = 0;
		boolean added = false;

		while (rotCount < 4) {
			for (Coord c : targetCoords) {
				Message msg = new Message(type, c.getX() + ", " + c.getY());
				fakeInterface.writeOnBuffer(msg);
				Message resp = fakeInterface.readTestResult();
				if (resp.type == MessageType.UPDATE) {
					added = true;
					break;
				}
			}

			if (!added) {
				Message rotateMsg = new Message(MessageType.ROTATE, null);
				fakeInterface.writeOnBuffer(rotateMsg);
				Message resp = fakeInterface.readTestResult();
				assertTrue(resp.type == MessageType.ROTATED);
			} else {
				break;
			}
		}

		if (!added) {
			fail("Received an unplaceable tile.");
		}

		Message rotateMsg = new Message(MessageType.ROTATE, null);
		fakeInterface.writeOnBuffer(rotateMsg);
		Message resp = fakeInterface.readTestResult();
		assertTrue(resp.type == MessageType.INVALID_MOVE);

		/* Test on follower addition. */

		boolean updated = false;
		for (SidePosition s : SidePosition.values()) {
			fakeInterface.writeOnBuffer(new Message(MessageType.FOLLOWER,
					s.toString()));
			resp = fakeInterface.readTestResult();
			if (resp.type == MessageType.UPDATE) {
				updated = true;
				break;
			}
		}

		if (!updated) {
			fail("Cannot place follower.");
		}

	}

	private class FakeGameInterface implements GameInterface {

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
			Message newMsg;
			if (msg.type == MessageType.UPDATE_SINGLE) {
				newMsg = new Message(MessageType.UPDATE, msg.payload);
			} else {
				newMsg = msg;
			}

			testOutput.write(newMsg);
		}

		@Override
		public void sendAllPlayer(Message msg)
				throws PlayersDisconnectedException {
			testOutput.write(msg);
		}

		public Message readTestResult() {
			return testOutput.read();
		}

		public void writeOnBuffer(Message msg) {
			testInput.write(msg);
		}

		public FakeGameInterface() {
			testInput = new MessageBuffer();
			testOutput = new MessageBuffer();
		}

	}
}
