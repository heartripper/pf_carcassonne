package it.polimi.dei.provafinale.carcassonne.controller;

import it.polimi.dei.provafinale.carcassonne.view.ViewManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class MenuPanelSwitcher implements ActionListener{

	private String panelName;
	
	public MenuPanelSwitcher(String panelName){
		 this.panelName = panelName;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		ViewManager.getInstance().changeMenuPanel(panelName);
	}

	
}
