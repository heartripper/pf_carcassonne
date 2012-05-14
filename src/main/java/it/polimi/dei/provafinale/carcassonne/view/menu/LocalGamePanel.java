package it.polimi.dei.provafinale.carcassonne.view.menu;
import it.polimi.dei.provafinale.carcassonne.controller.MenuPanelSwitcher;

import javax.swing.JPanel;
import javax.swing.BoxLayout;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;
import javax.swing.JButton;


import java.awt.Color;
import java.awt.Component;
import java.awt.Font;


public class LocalGamePanel extends JPanel {
	
	public LocalGamePanel() {
		
		setBackground(new Color(0,0,0,0));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JPanel titlePanel = new JPanel();
		titlePanel.setLayout(new BorderLayout(0, 0));
		titlePanel.setBackground(new Color(0,0,0,0));
		add(titlePanel);
		
		//new local game
		
		JLabel lblNewLocalGame = new JLabel("NEW LOCAL GAME");
		lblNewLocalGame.setForeground(Color.WHITE);
		lblNewLocalGame.setFont(new Font("Papyrus", Font.BOLD, 30));
		lblNewLocalGame.setHorizontalAlignment(SwingConstants.CENTER);
		titlePanel.add(lblNewLocalGame, BorderLayout.CENTER);
		
		//select players number (Label)
		
		JPanel selectPlayersNumberPanel = new JPanel();
		selectPlayersNumberPanel.setBackground(new Color(0,0,0,0));
		add(selectPlayersNumberPanel);
		
		JLabel lblSelectPlayersNumber = new JLabel("Select players number:");
		lblSelectPlayersNumber.setForeground(Color.WHITE);
		lblSelectPlayersNumber.setFont(new Font("Papyrus", Font.PLAIN, 20));
		selectPlayersNumberPanel.add(lblSelectPlayersNumber);
		
		//select players number (ComboBox)
		
		String[] data = {"2", "3", "4", "5"};
		JComboBox comboBox = new JComboBox(data);
		comboBox.setForeground(Color.BLACK);
		comboBox.setBackground(Color.WHITE);
		comboBox.setFont(new Font("Papyrus", Font.PLAIN, 20));
		selectPlayersNumberPanel.add(comboBox);
		
		//play the game
		
		JPanel playTheGamePanel = new JPanel();
		playTheGamePanel.setLayout(new BorderLayout(0, 0));
		playTheGamePanel.setBackground(new Color(0,0,0,0));
		add(playTheGamePanel);
		
		JButton btnPlayTheGame = new JButton("Play the Game");
		btnPlayTheGame.setForeground(Color.BLACK);
		btnPlayTheGame.setBackground(Color.WHITE);
		btnPlayTheGame.setFont(new Font("Papyrus", Font.PLAIN, 23));
		playTheGamePanel.add(btnPlayTheGame, BorderLayout.CENTER);
		
		//back to home
		
		JPanel backToHomePanel = new JPanel();
		backToHomePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		backToHomePanel.setBackground(new Color(0,0,0,0));
		add(backToHomePanel);
		
		JButton btnBackToHome = new JButton("Back to Home");
		btnBackToHome.setForeground(Color.BLACK);
		btnBackToHome.setBackground(Color.WHITE);
		btnBackToHome.setFont(new Font("Papyrus", Font.PLAIN, 15));
		btnBackToHome.addActionListener(new MenuPanelSwitcher("HomePanel"));
		backToHomePanel.add(btnBackToHome);
	}

}
