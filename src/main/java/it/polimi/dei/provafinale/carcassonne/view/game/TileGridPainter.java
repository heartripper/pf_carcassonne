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

	private static final int FIRST_TILE_OCCUPATION = 1;
	private static final int INITIAL_GRID_DIMENSION = 3;

	private static final long serialVersionUID = -2603074766780325918L;

	private TileRepresentationGrid grid;
	private final int tileDim = 125;
	private int currOffsetX;
	private int currOffsetY;
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
	}

	/**
	 * Updates the grid using the current dimension.
	 */
	public void updateRepresentation() {
		/* Obtaining new bounds. */
		int[] bounds = grid.getBounds();
		/* Setting the dimension of the new grid. */
		int vertTile = INITIAL_GRID_DIMENSION + bounds[2] - bounds[0];
		int horTile = INITIAL_GRID_DIMENSION + bounds[1] - bounds[3];
		currOffsetX = FIRST_TILE_OCCUPATION - bounds[3];
		currOffsetY = FIRST_TILE_OCCUPATION + bounds[2];
		setDimension(horTile * tileDim, vertTile * tileDim);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		/* Obtaining bounds. */
		int[] bounds = grid.getBounds();
		/* Print the components in the grid. */
		for (int j = bounds[2] + FIRST_TILE_OCCUPATION; j >= bounds[0]
				- FIRST_TILE_OCCUPATION; j--) {
			for (int i = bounds[3] - FIRST_TILE_OCCUPATION; i <= bounds[1]
					+ FIRST_TILE_OCCUPATION; i++) {
				Coord c = new Coord(i, j);
				String tileRep = grid.getTileRepresentation(c);
				/* The tile is present at a given coordinate. */
				if (tileRep != null) {
					printCard(g, tileRep, i, j);
				}
				/*
				 * The is the neighbor of another tile (that is really present
				 * on the grid).
				 */
				else if (grid.hasTileNeighbor(c)) {
					printPlaceHolder(g, i, j);
				}
			}
		}
	}

	private void printCard(Graphics g, String tile, int x, int y) {
		/* Calculating the coordinates. */
		int absX = (currOffsetX + x) * tileDim;
		int absY = (currOffsetY - y) * tileDim;
		/* Tile representation. */
		tilePainter.paintTile(tile, g, absX, absY);
	}

	private void printPlaceHolder(Graphics g, int x, int y) {
		/* Calculating the coordinates. */
		int absX = (currOffsetX + x) * tileDim;
		int absY = (currOffsetY - y) * tileDim;
		/* Tile representation. */
		tilePainter.paintPlaceHolder(g, absX, absY);
		/* Calculating the coordinates. */
		String coord = String.format("(%s,%s)", x, y);
		/* Writing the coordinates on the placeholder representation */
		g.setColor(Color.BLACK);
		g.drawString(coord, absX + tileDim / 4, absY + tileDim / 2);

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