package it.polimi.dei.provafinale.carcassonne.model.gameinterface;

public class MessageBuffer {

	private Message buffer = null;

	/**
	 * Provides a buffer to allow different threads to exchange messages.
	 * 
	 * @param msg
	 *            a Message to write in the buffer.
	 */
	public synchronized void write(Message msg) {
		/* The buffer hasn't been read yet. */
		while (buffer != null) {
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}
		/* Saves the written message in the buffer. */
		buffer = msg;
		/* Notifies all the waiting threads. */
		notifyAll();
	}

	/**
	 * Reads available message in the buffer.
	 * 
	 * @return the read Message.
	 */
	public synchronized Message read() {
		/* If the buffer is empty, the current threads waits for a message. */
		while (buffer == null) {
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}
		/* Saves the read message in msg. */
		Message msg = buffer;
		/* Empties the buffer. */
		buffer = null;
		/* Notifies all the waiting threads. */
		notifyAll();
		return msg;
	}
}
