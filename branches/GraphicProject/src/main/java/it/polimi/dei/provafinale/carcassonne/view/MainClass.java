package it.polimi.dei.provafinale.carcassonne.view;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import it.polimi.dei.provafinale.carcassonne.model.Coord;
import it.polimi.dei.provafinale.carcassonne.model.card.Card;
import it.polimi.dei.provafinale.carcassonne.model.card.TileGrid;
import it.polimi.dei.provafinale.carcassonne.model.card.TileStack;
import it.polimi.dei.provafinale.carcassonne.view.game.GridPainter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class MainClass {

	
	public static void main(String[] args) {
		// ViewManager.getInstance();

		JFrame frame = new JFrame("test");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setSize(600, 600);
		frame.setLayout(new FlowLayout());
		
		TileGrid grid = new TileGrid();
		GridPainter painter = new GridPainter(grid);
		
		grid.putTile(new Card("N=N S=C O=S E=S NS=0 NE=0 NO=0 OE=1 SE=0 SO=0"), new Coord(0, 0));
		grid.putTile(new Card("N=C S=C O=N E=N NS=0 NE=0 NO=0 OE=0 SE=0 SO=0"), new Coord(0, -1));
		grid.putTile(new Card("N=C S=C O=N E=N NS=0 NE=0 NO=0 OE=0 SE=0 SO=0"), new Coord(0, -2));
		grid.putTile(new Card("N=C S=C O=S E=S NS=0 NE=0 NO=0 OE=0 SE=0 SO=0"), new Coord(-1,0));
		
		painter.paintImmediately(0, 0, painter.getWidth(), painter.getWidth());		
		JScrollPane scroll = new JScrollPane();
		scroll.setViewportView(painter);
		frame.add(scroll);

	}
}
