package it.polimi.dei.provafinale.carcassonne.view;

import it.polimi.dei.provafinale.carcassonne.controller.WindowClose;
import it.polimi.dei.provafinale.carcassonne.view.game.GamePanel;
import it.polimi.dei.provafinale.carcassonne.view.menu.MenuPanel;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.CardLayout;

public class CarcassonneFrame extends JFrame{
	
	public static final String MENUPANEL = "menupanel";
	public static final String GAMEPANEL = "gamepanel";
	
	private MenuPanel menuPanel;
	private GamePanel gamePanel;
	private CardLayout mainLayout;
	
	public CarcassonneFrame() {
		
		//TODO AGGIUNGERE ICONA GIOCO NEL PROFILO DELLA FINESTRA
		
		super("Carcassonne");
		
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowClose());
        
		setVisible(true);
		setSize(600, 300);
		
		mainLayout = new CardLayout(0,0);
		setLayout(mainLayout);
		menuPanel = new MenuPanel();
		add(menuPanel, MENUPANEL);
		menuPanel.setVisible(true);
		
		repaint();
	}

	public void changeMenuPanel(String destination) {
		menuPanel.changeMenuPanel(destination);
	}
	
	public void setGamePanel(GamePanel gamePanel){
		if(gamePanel == null){
			add(gamePanel, GAMEPANEL);
			this.gamePanel = gamePanel;
		}else{
			return;
		}
	}
	
	public void changeMainPanel(String destination){
		mainLayout.show(this, destination);
	}
}
