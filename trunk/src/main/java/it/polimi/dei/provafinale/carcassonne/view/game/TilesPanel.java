package it.polimi.dei.provafinale.carcassonne.view.game;

import it.polimi.dei.provafinale.carcassonne.Coord;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
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
	 * Takes a screenshot of represented tiles.
	 * 
	 * @return a BufferedImage containing the screenshot.
	 * */
	public BufferedImage takeScreenshot() {
		int width = tilesPainter.getWidth();
		int height = tilesPainter.getHeight();
		BufferedImage img = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = img.createGraphics();
		tilesPainter.paint(g2d);
		g2d.dispose();
		return img;
	}

	/**
	 * Updates the representation of the tiles on the grid.
	 */
	public void updateRepresentation() {
		tilesPainter.updateRepresentation();
		Graphics g = this.getGraphics();
		if (g != null) {
			this.paint(g);
		}
	}
	
	/**
	 * The class TileGridPainter extends a JLabel in order to paint the updated grid
	 * of the game.
	 * 
	 */
	private class TileGridPainter extends JLabel {

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
}