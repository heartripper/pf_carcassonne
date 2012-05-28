package it.polimi.dei.provafinale.carcassonne.view;

import it.polimi.dei.provafinale.carcassonne.controller.WindowClose;
import it.polimi.dei.provafinale.carcassonne.view.game.GamePanel;
import it.polimi.dei.provafinale.carcassonne.view.menu.MenuPanel;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.CardLayout;

public class CarcassonneFrame extends JFrame {

	private static final long serialVersionUID = -7524847858570259831L;

	public static final String MENUPANEL = "menupanel";
	public static final String GAMEPANEL = "gamepanel";

	private CardLayout mainLayout;
	private MenuPanel menuPanel;
	private JPanel gamePanelContainer;
	private JPanel gamePanel;

	/**
	 * CarcassonneFrame constructor. Creates a new instance of class
	 * CarcassonneFrame.
	 */
	public CarcassonneFrame() {

		// TODO AGGIUNGERE ICONA GIOCO NEL PROFILO DELLA FINESTRA

		/* Superclass constructor invocation. */
		super("Carcassonne");

		/*
		 * Adding option "Are you sure you want to close this window?" to
		 * CarcassonneFrame.
		 */
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowClose());

		setVisible(true);
		setSize(600, 700);

		/* Setting layout. */
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
		/* There are no instances of GamePanel. */
		if (this.gamePanel == null) {
			this.gamePanel = panel;
			gamePanelContainer.add(gamePanel, BorderLayout.CENTER);
		}
		/* An instance of GamePanel already exists. */
		else {
			return;
		}
	}
	
	public JPanel getGamePanel(){
		return gamePanel;
	}

}
