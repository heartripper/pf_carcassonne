package it.polimi.dei.provafinale.carcassonne.view;

public class ViewManager{
	
	private static ViewManager instance;
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
	
	
}
