package it.polimi.dei.provafinale.carcassonne.view.menu;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * The class MenuPanel extends a JPanel in order to manage the switch of menu
 * panels (HomePanel, InternetGamePanel, LocalGamePanel):
 * 
 */
public class MenuPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public static final String INTERNETGAMEPANEL = "internetgamepanel";
	public static final String LOCALGAMEPANEL = "localgamepanel";
	public static final String HOMEPANEL = "homepanel";

	private CardLayout menuLayout;
	private JPanel menuContainer;

	/**
	 * MenuPanel constructor. Creates a new instance of class MenuPanel.
	 */
	public MenuPanel() {

		/* Setting background. */
		String path = "src/main/resources/background.png";
		BufferedImage background = null;
		try {
			background = ImageIO.read(new File(path));
		} catch (IOException e) {
			System.out.println("Error reading images.");
		}

		setLayout(new BorderLayout(0, 0));

		/* Setting banner. */
		JPanel bannerPanel = new BannerPanel();
		add(bannerPanel, BorderLayout.NORTH);

		/*
		 * Adding an instance of menuContainer that will contain an instance of
		 * HomePanel, LocalGamePanel or InternetGamePanel.
		 */
		menuContainer = new JPanel();
		add(menuContainer, BorderLayout.CENTER);
		menuLayout = new CardLayout(0, 0);

		menuContainer.setLayout(menuLayout);
		menuContainer.add(new HomePanel(background), HOMEPANEL);
		menuContainer.add(new LocalGamePanel(background), LOCALGAMEPANEL);
		menuContainer.add(new InternetGamePanel(background), INTERNETGAMEPANEL);

		setVisible(true);
	}

	/**
	 * Changes the content of menuContainer into one of the following panel
	 * (HomePanel, LocalGamePanel, InternetGamePanel):
	 * 
	 * @param destination
	 *            a String that identifies the panel we want to put into
	 *            menuContainer.
	 */
	public void changeMenuPanel(String destination) {
		menuLayout.show(menuContainer, destination);
	}
}
