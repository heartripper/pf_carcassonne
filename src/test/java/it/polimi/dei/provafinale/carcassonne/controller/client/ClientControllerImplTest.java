package it.polimi.dei.provafinale.carcassonne.controller.client;

import static org.junit.Assert.*;

import java.awt.Color;

import it.polimi.dei.provafinale.carcassonne.PlayerColor;
import it.polimi.dei.provafinale.carcassonne.controller.ClientInterface;
import it.polimi.dei.provafinale.carcassonne.controller.Message;
import it.polimi.dei.provafinale.carcassonne.controller.MessageType;

import org.junit.Before;
import org.junit.Test;

public class ClientControllerImplTest {

	private FakeClientInterface fakeClientInterface;
	private FakeViewInterface fakeViewInterface;
	private ClientControllerImpl clientControllerImpl;
	private Thread thread;
	private int numPlayers = 3;

	@Before
	public void setUp() {

		fakeClientInterface = new FakeClientInterface();
		fakeViewInterface = new FakeViewInterface();

		clientControllerImpl = new ClientControllerImpl(fakeClientInterface,
				fakeViewInterface);

		thread = new Thread(clientControllerImpl);
		thread.start();

	}

	@Test
	public void test() {
		String payload = String.format("%s, %s, %s, %s",
				"N=N S=C W=S E=S NS=0 NE=0 NW=0 WE=1 SE=0 SW=0", "local",
				PlayerColor.R, numPlayers);
		Message msg = new Message(MessageType.START, payload);
		fakeClientInterface.writeOnInput(msg);
		assertTrue(fakeViewInterface.readAction() == Actions.INITIALIZE);
	}

	private class FakeClientInterface implements ClientInterface {

		MessageBuffer testInput;
		MessageBuffer testOutput;

		public FakeClientInterface() {
			testInput = new MessageBuffer();
			testOutput = new MessageBuffer();
		}

		@Override
		public void connect() {

		}

		@Override
		public void sendMessage(Message msg) {
			testOutput.write(msg);
		}

		@Override
		public Message readMessage() {
			return testInput.read();
		}

		@Override
		public void reconnect(String matchName, PlayerColor color) {

		}

		public Message readFromOutput() {
			return testOutput.read();
		}

		public void writeOnInput(Message msg) {
			testInput.write(msg);
		}

	}

	private class FakeViewInterface implements ViewInterface {

		private Actions action;

		@Override
		public void initialize(String initializeString) {
			writeAction(Actions.INITIALIZE);
		}

		@Override
		public void updateGridRepresentation(String msg) {
			writeAction(Actions.UPDATE_GRID_REPRESENTATION);
		}

		@Override
		public void updateCurrentTile(String rep) {
			writeAction(Actions.UPDATE_CURRENT_TILE);
		}

		@Override
		public void updateScore(String msg) {
			writeAction(Actions.UPDATE_SCORE);
		}

		@Override
		public void setUIActive(boolean enabled) {
			writeAction(Actions.SET_UI_ACTIVE);
		}

		@Override
		public void setCurrentPlayer(PlayerColor color) {
			writeAction(Actions.SET_CURRENT_PLAYER);
		}

		@Override
		public void showNotify(String msg) {

		}

		public synchronized Actions readAction() {
			while (this.action == null) {
				try {
					wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			Actions action = this.action;
			this.action = null;
			notifyAll();
			return action;
		}

		private synchronized void writeAction(Actions action) {
			while (this.action != null) {
				try {
					wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			this.action = action;
			notifyAll();
		}

	}

	private enum Actions {

		INITIALIZE, UPDATE_GRID_REPRESENTATION, UPDATE_CURRENT_TILE, UPDATE_SCORE, SET_UI_ACTIVE, SET_CURRENT_PLAYER;

	}

}
