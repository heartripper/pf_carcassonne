package it.polimi.dei.provafinale.carcassonne.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;

import javax.swing.JPanel;

public class TilePanel extends JPanel {
	public TilePanel() {
	}

	private Polygon getSingleEntityTriangle(SidePosition position) {

		int width = getWidth();
		int height = getHeight();

		switch (position) {
		case N:
			int[] x0 = { 0, width, width / 2 };
			int[] y0 = { 0, 0, height / 2 };
			return new Polygon(x0, y0, 3);
		case E:
			int[] x1 = { width, width / 2, width };
			int[] y1 = { 0, height / 2, height };
			return new Polygon(x1, y1, 3);
		case S:
			int[] x2 = { width / 2, 0, width };
			int[] y2 = { height / 2, height, height };
			return new Polygon(x2, y2, 3);
		case O:
			int[] x3 = { 0, width / 2, 0 };
			int[] y3 = { 0, height / 2, height };
			return new Polygon(x3, y3, 3);
		default:
			return null;
		}
	}

	private Polygon getMultipleEntityTriangle(SidePosition position) {

		int width = getWidth();
		int height = getHeight();

		switch (position) {
		case N:
			int[] x0 = { 0, width, width / 2 };
			int[] y0 = { 0, 0, height / 4 };
			return new Polygon(x0, y0, 3);
		case E:
			int[] x1 = { width, 3 * width / 4, width };
			int[] y1 = { 0, height / 2, height };
			return new Polygon(x1, y1, 3);
		case S:
			int[] x2 = { width / 2, 0, width };
			int[] y2 = { 3 * height / 4, height, height };
			return new Polygon(x2, y2, 3);
		case O:
			int[] x3 = { 0, width / 4, 0 };
			int[] y3 = { 0, height / 2, height };
			return new Polygon(x3, y3, 3);
		default:
			return null;
		}
	}

	private Rectangle getRectangle(SidePosition position) {

		int width = getWidth();
		int height = getHeight();

		Point p;
		Dimension d;

		switch (position) {
		case N:
			p = new Point(7 * width / 16, 0);
			d = new Dimension(1 * width / 8, 3 * height / 8);
			return new Rectangle(p, d);
		case E:
			p = new Point(5 * width / 8, 7 * height / 16);
			d = new Dimension(3 * width / 8, 1 * height / 8);
			return new Rectangle(p, d);
		case S:
			p = new Point(7 * width / 16, 5 * height / 8);
			d = new Dimension(1 * width / 8, 3 * height / 8);
			return new Rectangle(p, d);
		case O:
			p = new Point(0, 7 * height / 16);
			d = new Dimension(3 * width / 8, 1 * height / 8);
			return new Rectangle(p, d);
		default:
			return null;
		}
	}

	private Rectangle getCentralRectangle() {

		int width = getWidth();
		int height = getHeight();

		Point p;
		Dimension d;

		p = new Point(7 * width / 16, 7 * height / 16);
		d = new Dimension(1 * width / 8, 1 * height / 8);

		return new Rectangle(p, d);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponents(g);

		Rectangle r = getCentralRectangle();
		g.setColor(Color.RED);
		// g.fillPolygon(getMultipleEntityTriangle(SidePosition.S));
		g.fillRect((int) r.getX(), (int) r.getY(), (int) r.getWidth(),
				(int) r.getHeight());
	}

}
