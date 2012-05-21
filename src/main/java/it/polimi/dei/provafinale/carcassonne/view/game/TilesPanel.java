package it.polimi.dei.provafinale.carcassonne.view.game;

import it.polimi.dei.provafinale.carcassonne.model.gamelogic.card.TileGrid;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class TilesPanel extends JScrollPane{
	
	private TilesPainter tilesPainter;
	
	public TilesPanel(TileGrid grid){
		super();
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		setViewportView(panel);
		
		tilesPainter = new TilesPainter(grid);
		panel.add(tilesPainter, BorderLayout.CENTER);
	}
	
	public void updateRepresentation(){
		tilesPainter.updateRepresentation();
	}

}
