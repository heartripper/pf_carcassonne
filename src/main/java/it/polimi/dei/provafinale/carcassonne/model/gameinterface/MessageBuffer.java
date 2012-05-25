package it.polimi.dei.provafinale.carcassonne.model.gameinterface;

public class MessageBuffer {

	private Message buffer = null;

	public synchronized void write(Message msg) {
		while (buffer != null) {
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}

		buffer = msg;
		notifyAll();
	}

	public synchronized Message read() {
		while (buffer == null) {
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}
		
		Message msg = buffer;
		buffer = null;
		notifyAll();
		return msg;
	}
}
