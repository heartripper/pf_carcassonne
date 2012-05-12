package it.polimi.dei.provafinale.carcassonne.controller;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JOptionPane;

public class WindowClose extends WindowAdapter{

	public void windowClosing(WindowEvent e){
		
		int ans = JOptionPane.showConfirmDialog(null, "Are you sure you want to close this window?","Attention!", JOptionPane.YES_NO_OPTION);
		
		if(ans == JOptionPane.YES_OPTION){
			System.exit(0);
		}
	}
	
}
