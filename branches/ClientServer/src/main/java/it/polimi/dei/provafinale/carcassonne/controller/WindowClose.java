package it.polimi.dei.provafinale.carcassonne.controller;

import it.polimi.dei.provafinale.carcassonne.view.CarcassonneFrame;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JOptionPane;

public class WindowClose extends WindowAdapter{

	public void windowClosing(WindowEvent e){
		
		CarcassonneFrame frame = (CarcassonneFrame) e.getWindow();
		int ans = JOptionPane.showConfirmDialog(null, "Are you sure you want to close this window?","Attention!", JOptionPane.YES_NO_OPTION);
		
		if(ans == JOptionPane.YES_OPTION){
			exit(frame);
			
		}
	}
	
	static void exit (CarcassonneFrame frame){
		frame.dispose();
	}
	
}
