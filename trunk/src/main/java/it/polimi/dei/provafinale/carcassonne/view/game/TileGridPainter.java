package it.polimi.dei.provafinale.carcassonne.view.game;

import it.polimi.dei.provafinale.carcassonne.Coord;
import it.polimi.dei.provafinale.carcassonne.model.Tile;
import it.polimi.dei.provafinale.carcassonne.model.TileGrid;

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

	private static final int INITIAL_GRID_DIMENSION = 3;

	private static final long serialVersionUID = -2603074766780325918L;

	private TileGrid grid;
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
	public TileGridPainter(TileGrid grid) {
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
		int horTile = 3 + bounds[1] - bounds[3];
		currOffsetX = 1 - bounds[3];
		currOffsetY = 1 + bounds[2];
		setDimension(horTile * tileDim, vertTile * tileDim);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		/* Obtaining bounds. */
		int[] bounds = grid.getBounds();
		/* Print the components in the grid. */
		for (int j = bounds[2] + 1; j >= bounds[0] - 1; j--) {
			for (int i = bounds[3] - 1; i <= bounds[1] + 1; i++) {
				Coord c = new Coord(i, j);
				Tile tile = grid.getTile(c);
				/* The tile is present at a given coordinate. */
				if (tile != null) {
					printCard(g, tile);
				}
				/*
				 * The is the neighbor of another tile (that is really present
				 * on the grid).
				 */
				else if (grid.hasNeighborForCoord(c)) {
					printPlaceHolder(g, i, j);
				}
			}
		}
	}

	private void printCard(Graphics g, Tile tile) {
		/* Calculating the coordinates. */
		Coord coord = tile.getCoordinates();
		int x = (currOffsetX + coord.getX()) * tileDim;
		int y = (currOffsetY - coord.getY()) * tileDim;
		/* Tile representation. */
		tilePainter.paintTile(tile.toString(), g, x, y);
	}

	private void printPlaceHolder(Graphics g, int i, int j) {
		/* Calculating the coordinates. */
		int offsetX = (currOffsetX + i) * tileDim;
		int offsetY = (currOffsetY - j) * tileDim;
		/* Tile representation. */
		tilePainter.paintPlaceHolder(g, offsetX, offsetY);
		/* Calculating the coordinates. */
		String coord = String.format("(%s,%s)", i, j);
		/* Writing the coordinates on the placeholder representation */
		g.setColor(Color.BLACK);
		g.drawString(coord, offsetX + tileDim / 4, offsetY + tileDim / 2);

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