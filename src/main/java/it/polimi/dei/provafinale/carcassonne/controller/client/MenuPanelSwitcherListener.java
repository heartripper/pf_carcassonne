package it.polimi.dei.provafinale.carcassonne.controller.client;

import it.polimi.dei.provafinale.carcassonne.view.ViewManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Class MenuPanelSwitcherListener implements an ActionListener in order to switch
 * panel.
 * 
 */
public class MenuPanelSwitcherListener implements ActionListener {

	private String panelName;

	/**
	 * MenuPanelSwitcherListener constructor. Creates a new instance of class
	 * MenuPanelSwitcherListener.
	 * 
	 * @param panelName
	 */
	public MenuPanelSwitcherListener(String panelName) {
		this.panelName = panelName;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		ViewManager.getInstance().changeMenuPanel(panelName);
	}

}
