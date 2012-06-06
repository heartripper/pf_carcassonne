package it.polimi.dei.provafinale.carcassonne.view.game;

import it.polimi.dei.provafinale.carcassonne.Coord;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JLabel;

/**
 * The class TileGridPainter extends a JLabel in order to paint the updated grid
 * of the game.
 * 
 */
public class TileGridPainter extends JLabel {

	private static final long serialVersionUID = -2603074766780325918L;

	private TileRepresentationGrid grid;
	private final int tileDim = 125;

	private TilePainter tilePainter = TilePainter.getInstance();

	/**
	 * TileGridPainter constructor. Creates a new instance of class
	 * TileGridPainter.
	 * 
	 * @param grid
	 *            the grid to represent.
	 */
	public TileGridPainter(TileRepresentationGrid grid) {
		this.grid = grid;
		addMouseListener(new TileClickListener(grid));
	}

	/**
	 * Updates the grid using the current dimension.
	 */
	public void updateRepresentation() {
		Dimension d = grid.getDimension();
		setDimension(d.width * tileDim, d.height * tileDim);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		for (int i = grid.smallestX(); i <= grid.greatestX(); i++) {
			for (int j = grid.greatestY(); j >= grid.smallestY(); j--) {

				Coord gridCoord = new Coord(i, j);
				String tileRep = grid.getTileRepresentation(gridCoord);
				Coord realCoord = grid.toRealCoord(gridCoord);
				/* The tile is present at a given coordinate. */
				if (tileRep != null) {
					printCard(g, tileRep, realCoord);
				}
				/*
				 * The is the neighbor of another tile (that is really present
				 * on the grid).
				 */
				else if (grid.hasTileNeighbor(gridCoord)) {
					printPlaceHolder(g, realCoord);
				}
			}
		}
	}

	/**
	 * Manages the tile painting.
	 * 
	 * @param g
	 *            an instance of class Graphics.
	 * @param tile
	 *            a Tile we want to paint.
	 * @param realPos
	 *            the coordinates where we want to put the Tile.
	 */
	private void printCard(Graphics g, String tile, Coord realPos) {
		int x = realPos.getX() * tileDim;
		int y = realPos.getY() * tileDim;
		tilePainter.paintTile(tile, g, x, y);
	}

	/**
	 * Manages the placeholder (empty tile) painting.
	 * @param g an instance of class Graphics.
	 * @param realCoord the coordinates where we want to put the placeholder.
	 */
	private void printPlaceHolder(Graphics g, Coord realCoord) {
		/* Tile representation. */
		int x = realCoord.getX() * tileDim;
		int y = realCoord.getY() * tileDim;
		tilePainter.paintPlaceHolder(g, x, y);
		/* Calculating the coordinates. */
		Coord gridCoord = grid.toGridCoord(realCoord);
		String s = String.format("(%s,%s)", gridCoord.getX(), gridCoord.getY());
		/* Writing the coordinates on the placeholder representation */
		g.setColor(Color.BLACK);
		g.drawString(s, x + tileDim / 3, y + tileDim / 2);
	}

	/**
	 * Sets the dimension of an instance of the class TileGridPainter.
	 * 
	 * @param x
	 *            a number that indicates the horizontal dimension.
	 * @param y
	 *            a number that indicates the vertical dimension.
	 */
	public void setDimension(int x, int y) {
		Dimension d = new Dimension(x, y);
		setSize(d);
		setPreferredSize(d);
		setMaximumSize(d);
		setMinimumSize(d);
	}
}