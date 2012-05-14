package it.polimi.dei.provafinale.carcassonne.view;
import it.polimi.dei.provafinale.carcassonne.controller.WindowClose;
import it.polimi.dei.provafinale.carcassonne.view.menu.MenuPanel;

import java.awt.CardLayout;

import javax.swing.JFrame;


public class CarcassonneFrame extends JFrame{
	
	private CardLayout mainLayout;
	private MenuPanel menu;
		
	public CarcassonneFrame(){
		super("Carcassonne");
		
		mainLayout = new CardLayout(0, 0);
		setLayout(mainLayout);
		
		menu = new MenuPanel();
		add(menu, "menu");
		
		setSize(800,600);
	
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowClose());
        
		setVisible(true);
	}
		
	public void changeMenuPanel(String panelName){
		menu.changePanel(panelName);
	}
		
		
}