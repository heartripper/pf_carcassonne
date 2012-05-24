package it.polimi.dei.provafinale.carcassonne.view.game;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import it.polimi.dei.provafinale.carcassonne.Constants;

public class CurrentTilePanel extends JPanel{
	
	private static final long serialVersionUID = 1677387556314454292L;
	
	private String currentTileRep;
	private JLabel tileLabel;
	private TilePainter tilePainter = TilePainter.getInstance();
	
	public CurrentTilePanel(){
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		int tileDim = Constants.TILE_PIXEL_DIMENSION;
		Dimension d = new Dimension(tileDim, tileDim);
		
		tileLabel = new JLabel();
		tileLabel.setMinimumSize(new Dimension(125, 125));
		tileLabel.setMaximumSize(new Dimension(125, 125));
		tileLabel.setPreferredSize(d);
		
		JPanel inner = new JPanel();
		inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
		inner.add(Box.createVerticalGlue());
		inner.add(tileLabel);
		inner.add(Box.createVerticalGlue());
		
		add(Box.createHorizontalGlue());
		add(inner);
		add(Box.createHorizontalGlue());
	}
	
	public void setCurrentTile(String rep){
		currentTileRep = rep;
		Graphics g = getGraphics();
		paint(g);
	}
	
	protected void paintComponent(Graphics g){
		if(currentTileRep == null){
			tilePainter.paintPlaceHolder(g, 0, 0);
		}else{
			tilePainter.paintTile(currentTileRep, g, 0, 0);
		}
	}
}
