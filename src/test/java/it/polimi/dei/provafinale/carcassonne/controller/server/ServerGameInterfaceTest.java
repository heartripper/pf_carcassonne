package it.polimi.dei.provafinale.carcassonne.controller.server;

import static org.junit.Assert.*;

import java.awt.Color;
import java.rmi.Remote;
import java.util.ArrayList;
import java.util.List;

import it.polimi.dei.provafinale.carcassonne.PlayerColor;
import it.polimi.dei.provafinale.carcassonne.controller.ConnectionLostException;
import it.polimi.dei.provafinale.carcassonne.controller.Message;
import it.polimi.dei.provafinale.carcassonne.controller.MessageType;
import it.polimi.dei.provafinale.carcassonne.controller.RemotePlayer;

import org.junit.Before;
import org.junit.Test;

public class ServerGameInterfaceTest {

	private List<RemotePlayer> remotePlayers;
	private ServerGameInterface serverGameInterface;

	private final int numPlayers = 3;

	@Before
	public void setUp() {
		remotePlayers = new ArrayList<RemotePlayer>(numPlayers);
		for (int i = 0; i < numPlayers; i++) {
			RemotePlayer p = new FakeRemotePlayer();
			remotePlayers.add(i, p);
		}
		serverGameInterface = new ServerGameInterface(remotePlayers);
	}

	/* Test that a message sent to all players arrives to all players. */
	@Test
	public void sendToAllTest() {
		Message msg = new Message(MessageType.SCORE, null);
		try {
			serverGameInterface.sendAllPlayer(msg);
		} catch (PlayersDisconnectedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (RemotePlayer rm : remotePlayers) {
			FakeRemotePlayer frm = (FakeRemotePlayer) rm;
			assertTrue(frm.hasDataToRead);
			assertTrue(frm.readOutput() == msg);
		}
	}

	/* Test that a message sent to one player arrives only to that player. */
	@Test
	public void sendSinglePlayerTest() {
		PlayerColor color = PlayerColor.R;
		Message msg = new Message(MessageType.SCORE, null);
		try {
			serverGameInterface.sendPlayer(color, msg);
		} catch (PlayersDisconnectedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int colorIndex = PlayerColor.indexOf(color);
		for (int i = 0; i < numPlayers; i++) {
			FakeRemotePlayer frm = (FakeRemotePlayer) remotePlayers.get(i);
			if (i == colorIndex) {
				assertTrue(frm.hasDataToRead);
				assertTrue(frm.readOutput() == msg);
			} else {
				assertFalse(frm.hasDataToRead);
			}
		}
	}

	/* Test that messages are not sent to inactive players. */
	@Test
	public void inactivePlayerTest() {
		PlayerColor color = PlayerColor.R;
		Message msg = new Message(MessageType.SCORE, null);
		int index = color.indexOf(color);
		FakeRemotePlayer player = (FakeRemotePlayer) remotePlayers.get(index);
		player.setInactive();
		try {
			serverGameInterface.sendAllPlayer(msg);
		} catch (PlayersDisconnectedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < numPlayers; i++) {
			FakeRemotePlayer frp = (FakeRemotePlayer) remotePlayers.get(i);
			if (i == index) {
				assertFalse(frp.hasDataToRead);
			} else {
				assertTrue(frp.hasDataToRead);
			}
		}
	}

	private class FakeRemotePlayer implements RemotePlayer {

		private Message testInput;
		private Message testOutput;
		private boolean hasDataToRead = false;
		private boolean active = true;

		public boolean getHasDataToRead() {
			return hasDataToRead;
		}

		public Message readOutput() {
			return testOutput;
		}

		public void writeOnInput(Message msg) {
			testInput = msg;
		}

		@Override
		public Message readMessage() throws ConnectionLostException {
			return testInput;
		}

		@Override
		public void sendMessage(Message msg) throws ConnectionLostException {
			testOutput = msg;
			hasDataToRead = true;
		}

		@Override
		public void close() throws ConnectionLostException {

		}

		@Override
		public boolean isActive() {
			return active;
		}

		@Override
		public void setInactive() {
			active = false;
		}

	}

}
