package it.polimi.dei.provafinale.carcassonne.view.game;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.BoxLayout;

public class GamePanel extends JPanel {
	public GamePanel() {
		setLayout(new BorderLayout(0, 0));
		
		JPanel gamePanel = new JPanel();
		add(gamePanel, BorderLayout.CENTER);
		
		JPanel bottomPanel = new JPanel();
		add(bottomPanel, BorderLayout.SOUTH);
		bottomPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel backToHomePanel = new JPanel();
		bottomPanel.add(backToHomePanel, BorderLayout.EAST);
		
		JButton btnBackToHome = new JButton("Back to Home");
		btnBackToHome.setFont(new Font("Papyrus", Font.PLAIN, 15));
		backToHomePanel.add(btnBackToHome);
		
		JPanel playerPanel = new JPanel();
		bottomPanel.add(playerPanel, BorderLayout.WEST);
		
		JPanel notificationPanel = new JPanel();
		add(notificationPanel, BorderLayout.EAST);
		notificationPanel.setLayout(new BoxLayout(notificationPanel, BoxLayout.Y_AXIS));
	}

}
