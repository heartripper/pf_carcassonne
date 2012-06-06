package it.polimi.dei.provafinale.carcassonne.controller.client;

import it.polimi.dei.provafinale.carcassonne.Constants;
import it.polimi.dei.provafinale.carcassonne.view.CarcassonneFrame;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JOptionPane;

/**
 * Class WindowClose extends a WindowAdapter in order to manage the closure of a
 * window.
 * 
 */
public class WindowClose extends WindowAdapter {

	/**
	 * Asks the question "Are you sure you want to close this window?" after
	 * pressing the close button.
	 */
	public void windowClosing(WindowEvent e) {
		/* Retrieving the window that has activated the listener. */
		CarcassonneFrame frame = (CarcassonneFrame) e.getWindow();
		/* No options on window closure. */
		if (!Constants.ASK_ON_CLOSE) {
			close(frame);
		}
		/* Option setted on window closure. */
		else {
			int ans = JOptionPane.showConfirmDialog(null,
					"Are you sure you want to close this window?",
					"Attention!", JOptionPane.YES_NO_OPTION);
			if (ans == JOptionPane.YES_OPTION) {
				close(frame);
			}
		}
	}

	/**
	 * Closes the current window.
	 * 
	 * @param frame
	 *            a CarcassonneFrame to be closed.
	 */
	public void close(CarcassonneFrame frame) {
		frame.dispose();
		System.exit(0);
	}

}
