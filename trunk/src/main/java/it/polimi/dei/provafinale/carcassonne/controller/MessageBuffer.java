package it.polimi.dei.provafinale.carcassonne.controller;


/**
 * Class MessageBuffer provides a buffer, that will be used to put message to
 * exchange between different threads.
 * 
 */
public class MessageBuffer {

	private Message buffer = null;

	/**
	 * Provides a buffer to allow different threads to exchange messages.
	 * 
	 * @param message
	 *            a Message to write in the buffer.
	 */
	public synchronized void write(Message message) {
		/* The buffer hasn't been read yet. */
		while (buffer != null) {
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}
		
		buffer = message;
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
		
		Message msg = buffer;
		buffer = null;
		notifyAll();
		return msg;
	}
}
