package it.polimi.dei.provafinale.carcassonne.view;

import it.polimi.dei.provafinale.carcassonne.Utility;
import it.polimi.dei.provafinale.carcassonne.controller.client.WindowClose;
import it.polimi.dei.provafinale.carcassonne.view.game.GamePanel;
import it.polimi.dei.provafinale.carcassonne.view.menu.MenuPanel;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.CardLayout;

/**
 * The class CarcassonneFrame extends a JFrame in order to create the main
 * frame.
 * 
 */
public class CarcassonneFrame extends JFrame {

	private static final int CARCASSONNEFRAME_HEIGHT = 700;
	private static final int CARCASSONNEFRAME_WIDTH = 600;

	private static final long serialVersionUID = -7524847858570259831L;

	public static final String MENUPANEL = "menupanel";
	public static final String GAMEPANEL = "gamepanel";

	private CardLayout mainLayout;
	private MenuPanel menuPanel;
	private JPanel gamePanelContainer;
	private GamePanel gamePanel;

	/**
	 * CarcassonneFrame constructor. Creates a new instance of class
	 * CarcassonneFrame.
	 */
	public CarcassonneFrame() {

		super("Carcassonne");

		/* Setting icon. */
		setIconImage(Utility.readImage("/icon.png"));

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowClose());

		setVisible(true);
		setSize(CARCASSONNEFRAME_WIDTH, CARCASSONNEFRAME_HEIGHT);

		mainLayout = new CardLayout(0, 0);
		setLayout(mainLayout);

		/* Setting menuPanel. */
		menuPanel = new MenuPanel();
		add(menuPanel, MENUPANEL);
		menuPanel.setVisible(true);

		/* Setting gamePanelContainer. */
		gamePanelContainer = new JPanel();
		add(gamePanelContainer, GAMEPANEL);
		gamePanelContainer.setLayout(new BorderLayout());

		repaint();
	}

	/**
	 * Manages the change of mainPanel (eg. menu or game view) in
	 * CarcassonneFrame.
	 * 
	 * @param destination
	 *            a String representing the name of the panel we want to put in
	 *            mainLayout.
	 */
	public void changeMainPanel(String destination) {
		mainLayout.show(this.getContentPane(), destination);
	}

	/**
	 * Manages the change of menuPanel (eg. from HomePanel to LocalGamePanel,
	 * from HomePanel to InternetGamePanel) in CarcassonneFrame.
	 * 
	 * @param destination
	 *            a String representing the name of the panel we want to put in
	 *            menuPanel.
	 */
	public void changeMenuPanel(String destination) {
		menuPanel.changeMenuPanel(destination);
	}

	/**
	 * Sets a new instance of GamePanel if it doesn't exist.
	 * 
	 * @param gamePanel
	 *            an instance of class GamePanel.
	 */
	public void setGamePanel(GamePanel panel) {
		if (this.gamePanel == null) {
			this.gamePanel = panel;
			gamePanelContainer.add(gamePanel, BorderLayout.CENTER);
		} else {
			return;
		}
	}

	/**
	 * 
	 * @return the class attribute gamePanel.
	 */
	public GamePanel getGamePanel() {
		return gamePanel;
	}

}
