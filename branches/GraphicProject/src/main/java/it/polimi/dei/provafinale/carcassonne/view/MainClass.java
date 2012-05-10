package it.polimi.dei.provafinale.carcassonne.view;

import javax.swing.JFrame;

public class MainClass {
	public static void main(String[] args) {
		//ViewManager.getInstance();
		JFrame frame = new JFrame();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.add(new TilePanel());
	}
}
