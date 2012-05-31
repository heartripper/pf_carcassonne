package it.polimi.dei.provafinale.carcassonne.view.game;

import java.awt.Graphics;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * The class TilesPanel extends a JScrollPane in order to contain the
 * representation of the grid of the current match.
 * 
 */
public class TilesPanel extends JScrollPane {

	private static final long serialVersionUID = -2364217759081024054L;

	private TileGridPainter tilesPainter;

	/**
	 * TileGridPainter constructor. Creates a new instance of class
	 * TileGridPainter.
	 * 
	 * @param grid
	 *            the grid to represent.
	 */
	public TilesPanel(TileRepresentationGrid grid) {
		super();

		/* Creating a new panel. */
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		setViewportView(panel);

		/* Creating a new inner panel. */
		JPanel inner = new JPanel();
		inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));

		/* New instance of class TileGridPainter. */
		tilesPainter = new TileGridPainter(grid);

		/* Adding layout options to panel. */
		panel.add(Box.createHorizontalGlue());
		panel.add(inner);
		panel.add(Box.createHorizontalGlue());

		/* Adding layout options to inner panel. */
		inner.add(Box.createVerticalGlue());
		inner.add(tilesPainter);
		inner.add(Box.createVerticalGlue());
	}

	/**
	 * Updates the representation of the tiles on the grid.
	 */
	public void updateRepresentation() {
		tilesPainter.updateRepresentation();
		Graphics g = this.getGraphics();
		if(g != null){
			this.paint(g);
		}
	}
}
