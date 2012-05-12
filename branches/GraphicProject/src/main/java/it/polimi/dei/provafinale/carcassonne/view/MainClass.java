package it.polimi.dei.provafinale.carcassonne.view;

import it.polimi.dei.provafinale.carcassonne.model.card.Card;

import javax.swing.JFrame;

public class MainClass {
	public static void main(String[] args) {
		//ViewManager.getInstance();
		JFrame frame = new JFrame();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Card c = new Card("N=C S=C O=S E=S NS=1 NE=0 NO=0 OE=0 SE=0 SO=0");
		
		frame.add(new TilePanel(c));
	}
}
