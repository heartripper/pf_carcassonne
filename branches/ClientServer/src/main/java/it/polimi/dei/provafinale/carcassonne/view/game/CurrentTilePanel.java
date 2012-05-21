package it.polimi.dei.provafinale.carcassonne.view.game;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

public class CurrentTilePanel extends JPanel{
	
	private static final long serialVersionUID = 1677387556314454292L;
	
	private final int TILE_DIM = 125;
	private String currentTileRep;
	private TilePainter tilePainter = TilePainter.getInstance();
	
	public CurrentTilePanel(){
		Dimension d = new Dimension(TILE_DIM, TILE_DIM);
		setMaximumSize(d);
		setMinimumSize(d);
	}
	
	public void setCurrentTile(String rep){
		currentTileRep = rep;
		Graphics g = getGraphics();
		paint(g);
	}
	
	protected void paintComponent(Graphics g){
		if(currentTileRep == null)
			tilePainter.paintPlaceHolder(g, 0, 0);
		else
			tilePainter.paintTile(currentTileRep, g, 0, 0);
	}
}
