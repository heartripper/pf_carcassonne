package it.polimi.dei.provafinale.carcassonne.view.game;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.BoxLayout;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JScrollPane;
import javax.swing.JLabel;
import java.awt.FlowLayout;

public class GamePanel extends JPanel {
	public GamePanel() {
		setLayout(new BorderLayout(0, 0));
		
		JPanel gamePanel = new JPanel();
		add(gamePanel, BorderLayout.CENTER);
		
		JLabel tilesLabel = new JLabel("");

		JPanel gridPanel = new JPanel();
		gridPanel.setLayout(new GridLayout());
		
		Dimension dim = new Dimension(10,10000);
		gamePanel.setLayout(new BoxLayout(gamePanel, BoxLayout.X_AXIS));
		tilesLabel.setSize(dim);
		tilesLabel.setMaximumSize(dim);
		tilesLabel.setMinimumSize(dim);
		tilesLabel.setPreferredSize(dim);
		
		JPanel tilesPanel = new JPanel();
		tilesPanel.setBackground(new Color(0,0,0,0));
		tilesPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(tilesPanel);
		tilesPanel.add(tilesLabel);
		gamePanel.add(scrollPane);
		
		JPanel notificationPanel = new JPanel();
		notificationPanel.setBackground(Color.GRAY);
		notificationPanel.setPreferredSize(new Dimension(195, 10));
		add(notificationPanel, BorderLayout.EAST);
		notificationPanel.setLayout(new BoxLayout(notificationPanel, BoxLayout.Y_AXIS));
		
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
		
	}

}
