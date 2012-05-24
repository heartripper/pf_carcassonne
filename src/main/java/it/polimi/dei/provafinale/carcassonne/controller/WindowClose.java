package it.polimi.dei.provafinale.carcassonne.controller;

import it.polimi.dei.provafinale.carcassonne.Constants;
import it.polimi.dei.provafinale.carcassonne.view.CarcassonneFrame;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JOptionPane;

public class WindowClose extends WindowAdapter {

	public void windowClosing(WindowEvent e) {

		CarcassonneFrame frame = (CarcassonneFrame) e.getWindow();

		if (!Constants.ASK_ON_CLOSE) {
			close(frame);
		} else {
			int ans = JOptionPane.showConfirmDialog(null,
					"Are you sure you want to close this window?",
					"Attention!", JOptionPane.YES_NO_OPTION);

			if (ans == JOptionPane.YES_OPTION) {
				close(frame);
			}
		}
	}

	public void close(CarcassonneFrame frame) {
		frame.dispose();
		System.exit(0);
	}

}
