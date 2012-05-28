package it.polimi.dei.provafinale.carcassonne.view.game;

import it.polimi.dei.provafinale.carcassonne.model.Coord;
import it.polimi.dei.provafinale.carcassonne.model.Tile;
import it.polimi.dei.provafinale.carcassonne.model.TileGrid;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JLabel;

public class TileGridPainter extends JLabel {

	private static final long serialVersionUID = -2603074766780325918L;

	private TileGrid grid;
	private final int tileDim = 125;
	private int currOffsetX;
	private int currOffsetY;

	private TilePainter tilePainter = TilePainter.getInstance();

	public TileGridPainter(TileGrid grid) {
		this.grid = grid;
	}

	public void updateRepresentation() {
		int[] bounds = grid.getBounds();
		int vertTile = 3 + bounds[2] - bounds[0];
		int horTile = 3 + bounds[1] - bounds[3];
		currOffsetX = 1 - bounds[3];
		currOffsetY = 1 + bounds[2];
		setDimension(horTile * tileDim, vertTile * tileDim);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		int[] bounds = grid.getBounds();

		for (int j = bounds[2] + 1; j >= bounds[0] - 1; j--) {
			for (int i = bounds[3] - 1; i <= bounds[1] + 1; i++) {
				Coord c = new Coord(i, j);
				Tile tile = grid.getTile(c);
				if (tile != null) {
					printCard(g, tile);
				} else if (grid.hasNeighborForCoord(c)) {
					printPlaceHolder(g, i, j);
				}
			}
		}
	}

	private void printCard(Graphics g, Tile tile) {
		Coord coord = tile.getCoordinates();
		int x = (currOffsetX + coord.getX()) * tileDim;
		int y = (currOffsetY - coord.getY()) * tileDim;
		tilePainter.paintTile(tile.toString(), g, x, y);
	}

	private void printPlaceHolder(Graphics g, int i, int j) {
		int offsetX = (currOffsetX + i) * tileDim;
		int offsetY = (currOffsetY - j) * tileDim;
		tilePainter.paintPlaceHolder(g, offsetX, offsetY);
		String coord = String.format("(%s,%s)", i, j);
		g.setColor(Color.BLACK);
		g.drawString(coord, offsetX + tileDim / 4, offsetY + tileDim / 2);

	}

	public void setDimension(int x, int y) {
		Dimension d = new Dimension(x, y);
		setSize(d);
		setPreferredSize(d);
		setMaximumSize(d);
		setMinimumSize(d);
	}
}