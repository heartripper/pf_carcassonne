package it.polimi.dei.provafinale.carcassonne.view;

import it.polimi.dei.provafinale.carcassonne.controller.WindowClose;
import it.polimi.dei.provafinale.carcassonne.view.game.GamePanel;
import it.polimi.dei.provafinale.carcassonne.view.menu.MenuPanel;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.CardLayout;

public class CarcassonneFrame extends JFrame{
	
	private static final long serialVersionUID = -7524847858570259831L;
	public static final String MENUPANEL = "menupanel";
	public static final String GAMEPANEL = "gamepanel";
	
	private MenuPanel menuPanel;
	private JPanel gamePanelContainer;
	private GamePanel gamePanel;
	private CardLayout mainLayout;
	
	public CarcassonneFrame() {
		
		//TODO AGGIUNGERE ICONA GIOCO NEL PROFILO DELLA FINESTRA
		
		super("Carcassonne");
		
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowClose());
        
		setVisible(true);
		setSize(600, 700);
		
		mainLayout = new CardLayout(0,0);
		setLayout(mainLayout);
		
		menuPanel = new MenuPanel();
		add(menuPanel, MENUPANEL);
		menuPanel.setVisible(true);
		
		gamePanelContainer = new JPanel();
		add(gamePanelContainer, GAMEPANEL);
		gamePanelContainer.setLayout(new BorderLayout());
		
		repaint();
	}

	public void changeMenuPanel(String destination) {
		menuPanel.changeMenuPanel(destination);
	}
	
	public void setGamePanel(GamePanel gamePanel){
		if(this.gamePanel == null){
			this.gamePanel = gamePanel;
			gamePanelContainer.add(gamePanel, BorderLayout.CENTER);
		}else{
			return;
		}
	}
	
	public void changeMainPanel(String destination){
		mainLayout.show(this.getContentPane(), destination);
	}
}
