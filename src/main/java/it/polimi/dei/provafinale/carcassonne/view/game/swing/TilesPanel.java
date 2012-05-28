package it.polimi.dei.provafinale.carcassonne.view.game.swing;

import it.polimi.dei.provafinale.carcassonne.model.TileGrid;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class TilesPanel extends JScrollPane{
	
	private static final long serialVersionUID = -2364217759081024054L;
	
	private TileGridPainter tilesPainter;
	
	public TilesPanel(TileGrid grid){
		super();
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		setViewportView(panel);
		
		JPanel inner = new JPanel();
		inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
		
		tilesPainter = new TileGridPainter(grid);
		panel.add(Box.createHorizontalGlue());
		panel.add(inner);
		panel.add(Box.createHorizontalGlue());
		
		inner.add(Box.createVerticalGlue());
		inner.add(tilesPainter);
		inner.add(Box.createVerticalGlue());
	}
	
	public void updateRepresentation(){
		tilesPainter.updateRepresentation();
	}
}
