package it.polimi.dei.provafinale.carcassonne.view;

import it.polimi.dei.provafinale.carcassonne.model.card.Card;
import it.polimi.dei.provafinale.carcassonne.model.card.Side;
import it.polimi.dei.provafinale.carcassonne.model.card.SidePosition;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.JPanel;

public class TilePanel extends JPanel {
	

	
	private static final Color FIELD_COLOR = new Color(88,155,39);
	private static final Color CITY_COLOR = new Color(116,89,31);
	private static final Color ROAD_COLOR = new Color(239,209,125);
	
	private Card tile;
	
	public TilePanel(Card tile) {
		this.tile = tile;
	}

	private Polygon getMultipleEntityTriangle(SidePosition position) {

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

	private Polygon getSingleEntityTriangle(SidePosition position) {

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
			d = new Dimension(1 * width / 8, 7 * height / 16);
			return new Rectangle(p, d);
		case E:
			p = new Point(9 * width / 16, 7 * height / 16);
			d = new Dimension(7 * width / 16, 1 * height / 8);
			return new Rectangle(p, d);
		case S:
			p = new Point(7 * width / 16, 9 * height / 16);
			d = new Dimension(1 * width / 8, 7 * height / 16);
			return new Rectangle(p, d);
		case O:
			p = new Point(0, 7 * height / 16);
			d = new Dimension(7 * width / 16, 1 * height / 8);
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

		ArrayList<SidePosition> sidesToPaint = new ArrayList<SidePosition>();
		
		for(SidePosition pos : SidePosition.values()){
			sidesToPaint.add(pos);
		}
		
		g.setColor(FIELD_COLOR);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		for (SidePosition position: sidesToPaint){
			Side currentSide = tile.getSide(position);
			ArrayList<Side> sides = tile.sidesLinkedTo(currentSide);
			int sidesSize = sides.size();
			switch(currentSide.getType()){
			
			case S:
				Rectangle road = getRectangle(position);
				g.setColor(ROAD_COLOR);
				g.fillRect((int)road.getX(), (int)road.getY(), (int)road.getWidth(), (int)road.getHeight());
				//There is a side with an entity related to mine
				if(sidesSize != 0){
					Rectangle centralRoad = getCentralRectangle();
					g.setColor(ROAD_COLOR);
					g.fillRect((int)centralRoad.getX(), (int)centralRoad.getY(), (int)centralRoad.getWidth(), (int)centralRoad.getHeight());
				}
				break;
				
			case C:
				//There is a side with an entity related to mine
				if(sidesSize != 0){
					SidePosition oppositePosition = position.getOpposite();
					for (Side tmp: sides){
						if(tile.getSidePosition(tmp) == oppositePosition){
							Rectangle centralCity = getCentralRectangle();
							g.setColor(CITY_COLOR);
							g.fillRect((int)centralCity.getX(), (int)centralCity.getY(), (int)centralCity.getWidth(), (int)centralCity.getHeight());	
						}	
					}
					Polygon city = getMultipleEntityTriangle(position);
					g.setColor(CITY_COLOR);
					g.fillPolygon(city);
				}
				else{
					Polygon city = getSingleEntityTriangle(position);
					g.setColor(CITY_COLOR);
					g.fillPolygon(city);
				}
				break;
				
			default: 
				break;
			}	
		}
	}

}
