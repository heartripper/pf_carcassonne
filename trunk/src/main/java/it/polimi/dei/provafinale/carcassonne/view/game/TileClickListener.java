package it.polimi.dei.provafinale.carcassonne.view.game;

import it.polimi.dei.provafinale.carcassonne.Constants;
import it.polimi.dei.provafinale.carcassonne.Coord;
import it.polimi.dei.provafinale.carcassonne.view.ViewManager;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class TileClickListener implements MouseListener{
	
	private TileRepresentationGrid tileGrid;
	
	public TileClickListener(TileRepresentationGrid tileGrid) {
		super();
		this.tileGrid = tileGrid;
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		computeCoord(arg0);
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	private void computeCoord(MouseEvent e){
		int x = e.getX() / Constants.TILE_PIXEL_DIMENSION;
		int y = e.getY() / Constants.TILE_PIXEL_DIMENSION;
		Coord coord = tileGrid.toGridCoord(new Coord(x,y));
		if(!tileGrid.hasTileNeighbor(coord)){
			return;
		}
		
		GamePanel gp = ViewManager.getInstance().getFrame().getGamePanel();
		if(!(gp instanceof SwingGamePanel)){
			return;
		}
		SwingGamePanel sgp = (SwingGamePanel)gp;
		sgp.setCoord(coord);
	}
	
}
