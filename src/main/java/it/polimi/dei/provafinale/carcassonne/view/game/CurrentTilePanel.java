package it.polimi.dei.provafinale.carcassonne.view.game;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.JPanel;

import it.polimi.dei.provafinale.carcassonne.Constants;

public class CurrentTilePanel extends JPanel {

	private static final long serialVersionUID = 1677387556314454292L;

	private String currentTileRep;
	private JLabel tileLabel = null;
	private TilePainter tilePainter = TilePainter.getInstance();
	private Dimension dim;

	/**
	 * CurrentTilePanel constructor. Creates a new instance of class
	 * CurrentTilePanel.
	 */
	public CurrentTilePanel() {
		setLayout(new BorderLayout());

		/* Setting dimension. */
		int tileDim = Constants.TILE_PIXEL_DIMENSION;
		dim = new Dimension(tileDim, tileDim);

		tileLabel = new JLabel();
		tileLabel.setSize(dim);
		tileLabel.setPreferredSize(dim);
		tileLabel.setMaximumSize(dim);
		tileLabel.setMinimumSize(dim);

		add(tileLabel);
	}

	/**
	 * Draws the current tile graphic representation (empty tile or game tile,
	 * depending on its textual identificator).
	 * 
	 * @param rep
	 *            a String that represents the tile we want to represent.
	 */
	public void setCurrentTile(String rep) {
		currentTileRep = rep;
		Graphics g = tileLabel.getGraphics();
		if(g == null){
			return;
		}
		paintComponent(g);
	}

	protected void paintComponent(Graphics g) {
		/* There are no tiles corresponding to the given textual representation. */
		if (currentTileRep == null) {
			tilePainter.paintPlaceHolder(g, 0, 0);
		}
		/* There is one tile corresponding to the given textual representation. */
		else {
			tilePainter.paintTile(currentTileRep, g, 0, 0);
		}
	}

}
