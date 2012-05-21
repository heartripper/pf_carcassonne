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
	
	public MenuPanel() {
		
		String path = "src/main/resources/background.png";
		Image background = Toolkit.getDefaultToolkit().createImage(path);
		
		setLayout(new BorderLayout(0, 0));
		
		JPanel bannerPanel = new BannerPanel();
		add(bannerPanel, BorderLayout.NORTH);
		
		menuContainer = new JPanel();
		add(menuContainer, BorderLayout.CENTER);
		
		menuLayout = new CardLayout(0,0);
		menuContainer.setLayout(menuLayout);
		menuContainer.add(new HomePanel(background), HOMEPANEL);
		menuContainer.add(new LocalGamePanel(background), LOCALGAMEPANEL);
		menuContainer.add(new InternetGamePanel(background), INTERNETGAMEPANEL);
		
		setVisible(true);
	}
	
	public void changeMenuPanel(String destination){
		menuLayout.show(menuContainer, destination);
	}

}
