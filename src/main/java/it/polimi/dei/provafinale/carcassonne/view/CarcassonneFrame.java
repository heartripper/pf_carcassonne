package it.polimi.dei.provafinale.carcassonne.view;
import it.polimi.dei.provafinale.carcassonne.view.menu.MenuPanel;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;



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
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
		
	public void changeMenuPanel(String panelName){
		menu.changePanel(panelName);
	}
		
		
}