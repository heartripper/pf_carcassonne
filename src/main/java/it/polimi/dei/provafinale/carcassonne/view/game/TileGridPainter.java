package it.polimi.dei.provafinale.carcassonne.view.game;

import it.polimi.dei.provafinale.carcassonne.model.gamelogic.Coord;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.card.Card;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.card.TileGrid;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JLabel;

public class TileGridPainter extends JLabel {

	private static final long serialVersionUID = -2603074766780325918L;

	private TileGrid grid;
	private final int TILE_DIM = 125;
	private int currOffsetX;
	private int currOffsetY;
	
	private TilePainter tilePainter = TilePainter.getInstance();

	public TileGridPainter(TileGrid grid) {
		this.grid = grid;
	}

	public void updateRepresentation(){
		int[] bounds = grid.getBounds();
		int vertTile = 3 + bounds[2] - bounds[0];
		int horTile = 3 + bounds[1] - bounds[3];
		currOffsetX = 1 + ( - bounds[3]);
		currOffsetY = 1 + ( - bounds[0]);
		setDimension(horTile * TILE_DIM, vertTile * TILE_DIM);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		int[] bounds = grid.getBounds();

		for (int j = bounds[0] - 1; j <= bounds[2] + 1; j++) {
			for (int i = bounds[3] - 1; i <= bounds[1] + 1; i++) {
				Coord c = new Coord(i, j);
				Card tile = grid.getTile(c);
				if (tile != null) {
					printCard(g, tile);
				} else if(grid.hasNeighborForCoord(c)){
					printPlaceHolder(g, i, j);
				}
			}
		}
	}

	private void printCard(Graphics g, Card tile) {
		Coord coord = tile.getCoordinates();
		int x = (currOffsetX + coord.getX()) * TILE_DIM;
		int y = (currOffsetY + coord.getY()) * TILE_DIM;
		tilePainter.paintTile(tile.toString(), g, x, y);
	}

	private void printPlaceHolder(Graphics g, int i, int j) {
		int x = (currOffsetX + i) * TILE_DIM;
		int y = (currOffsetY + j) * TILE_DIM;
		tilePainter.paintPlaceHolder(g, x, y);
	}

	public void setDimension(int x, int y) {
		Dimension d = new Dimension(x, y);
		setSize(d);
		setPreferredSize(d);
		setMaximumSize(d);
		setMinimumSize(d);
	}
}