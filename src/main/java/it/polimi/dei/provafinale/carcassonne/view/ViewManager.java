package it.polimi.dei.provafinale.carcassonne.view;

import java.awt.Graphics;

public class ViewManager{
	
	private static ViewManager instance = null;
	private CarcassonneFrame frame;
	
	private ViewManager(){
		frame = new CarcassonneFrame();
	}

	public static ViewManager getInstance(){
		if (instance == null){
			instance = new ViewManager();
		}
		
		return instance;
	}
	
	public void changeMenuPanel(String panelName){
		frame.changeMenuPanel(panelName);
	}
	
	public CarcassonneFrame getFrame(){
		return frame;
	}
	
	public void updateView(){
		Graphics g = frame.getGraphics();
		if(g != null){
			frame.paint(g);
		}
	}
	
}
