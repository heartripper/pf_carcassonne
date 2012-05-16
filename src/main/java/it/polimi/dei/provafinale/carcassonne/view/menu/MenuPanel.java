package it.polimi.dei.provafinale.carcassonne.view.menu;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

public class MenuPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	public static final String INTERNETGAMEPANEL = "internetgamepanel";
	public static final String LOCALGAMEPANEL = "localgamepanel";
	public static final String HOMEPANEL = "homepanel";

	private CardLayout menuLayout;
	private JPanel menuContainer;
	
	private Image banner;
	private Image background;
	
	public MenuPanel() {
		setLayout(new BorderLayout(0, 0));
		
		JPanel bannerPanel = new BannerPanel();
		add(bannerPanel, BorderLayout.NORTH);
		
		menuContainer = new MenuContainer();
		add(menuContainer, BorderLayout.CENTER);
		
		menuLayout = new CardLayout(0,0);
		menuContainer.setLayout(menuLayout);
		menuContainer.add(new HomePanel(), HOMEPANEL);
		menuContainer.add(new LocalGamePanel(), LOCALGAMEPANEL);
		menuContainer.add(new InternetGamePanel(), INTERNETGAMEPANEL);
		
		setVisible(true);
	}
	
	public void changeMenuPanel(String destination){
		menuLayout.show(menuContainer, destination);
	}

}
