package it.polimi.dei.provafinale.carcassonne.view.game;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import it.polimi.dei.provafinale.carcassonne.model.Coord;
import it.polimi.dei.provafinale.carcassonne.model.card.Card;
import it.polimi.dei.provafinale.carcassonne.model.card.SidePosition;
import it.polimi.dei.provafinale.carcassonne.model.card.TileGrid;

import javax.imageio.ImageIO;
import javax.swing.JLabel;

public class GridPainter extends JLabel {

	private TileGrid grid;

	private final int TILE_DIM = 125;

	private int currOffsetX;
	private int currOffsetY;

	private String pathPlaceHolder = "src/main/resources/placeholder.png";
	private Image placeHolder;

	private String pathCard = "src/main/resources/card.png";
	private Image cardImg;

	public GridPainter(TileGrid grid) {
		this.grid = grid;
		placeHolder = Toolkit.getDefaultToolkit().createImage(pathPlaceHolder);
		cardImg = Toolkit.getDefaultToolkit().createImage(pathCard);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		System.out.println("Paint!");
		int[] bounds = grid.getBounds();
		int vertTile = bounds[0] - bounds[2];
		int horTile = bounds[1] - bounds[3];

		currOffsetX = -(bounds[3] - 1);
		currOffsetY = -(bounds[0] - 1);

		setDimension(horTile * TILE_DIM, vertTile * TILE_DIM);

		for (int j = bounds[0] - 1; j <= bounds[2] + 1; j++) {
			for (int i = bounds[3] - 1; i <= bounds[3] + 1; i++) {
				Coord c = new Coord(i, j);
				Card tile = grid.getTile(c);
				if (tile != null) {
					printCard(g, tile);
				} else {
					for (SidePosition position : SidePosition.values()) {
						Coord offset = SidePosition
								.getOffsetForPosition(position);
						if (grid.getTile(c.add(offset)) != null) {
							printPlaceHolder(g, i, j);
							break;
						}
					}
				}
			}
		}
	}

	private void printCard(Graphics g, Card tile) {
		Coord coord = tile.getCoordinates();
		int x = (currOffsetX + coord.getX()) * TILE_DIM;
		int y = (currOffsetY + coord.getY()) * TILE_DIM;
		g.drawImage(cardImg, x, y, TILE_DIM, TILE_DIM, null);
	}

	private void printPlaceHolder(Graphics g, int i, int j) {
		int x = (currOffsetX + i) * TILE_DIM;
		int y = (currOffsetY + j) * TILE_DIM;
		g.drawImage(placeHolder, x, y, TILE_DIM, TILE_DIM, null);
	}

	private void setDimension(int x, int y) {
		Dimension d = new Dimension(x, y);
		setSize(d);
		setPreferredSize(d);
		setMaximumSize(d);
		setMinimumSize(d);
	}

}
