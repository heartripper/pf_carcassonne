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
		add(titlePanel);
		titlePanel.setLayout(new BorderLayout(0, 0));
		
		JLabel lblNewLocalGame = new JLabel("NEW LOCAL GAME");
		lblNewLocalGame.setFont(new Font("Papyrus", Font.BOLD, 30));
		lblNewLocalGame.setHorizontalAlignment(SwingConstants.CENTER);
		titlePanel.add(lblNewLocalGame, BorderLayout.CENTER);
		
		JPanel selectPlayersNumberPanel = new JPanel();
		add(selectPlayersNumberPanel);
		
		JLabel lblSelectPlayersNumber = new JLabel("Select players number:");
		lblSelectPlayersNumber.setFont(new Font("Papyrus", Font.PLAIN, 20));
		selectPlayersNumberPanel.add(lblSelectPlayersNumber);
		
		String[] data = {"2", "3", "4", "5"};
		JComboBox comboBox = new JComboBox(data);
		comboBox.setFont(new Font("Papyrus", Font.PLAIN, 20));
		selectPlayersNumberPanel.add(comboBox);
		
		JPanel playTheGamePanel = new JPanel();
		add(playTheGamePanel);
		playTheGamePanel.setLayout(new BorderLayout(0, 0));
		
		JButton btnPlayTheGame = new JButton("Play the Game");
		btnPlayTheGame.setFont(new Font("Papyrus", Font.PLAIN, 23));
		playTheGamePanel.add(btnPlayTheGame, BorderLayout.CENTER);
		
		JPanel backToHomePanel = new JPanel();
		backToHomePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(backToHomePanel);
		
		JButton btnBackToHome = new JButton("Back to Home");
		btnBackToHome.setFont(new Font("Papyrus", Font.PLAIN, 15));
		btnBackToHome.addActionListener(new MenuPanelSwitcher("HomePanel"));
		backToHomePanel.add(btnBackToHome);
	}

}
