package it.polimi.dei.provafinale.carcassonne.view.menu;
import it.polimi.dei.provafinale.carcassonne.controller.MenuPanelSwitcher;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;



public class HomePanel extends JPanel{
	
	public HomePanel() {

		setBackground(new Color(0,0,0,0));
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		add(Box.createVerticalGlue());
		
		//Local game
		
		JPanel localGamePanel = new JPanel();
		localGamePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		localGamePanel.setBackground(new Color(0,0,0,0));
		add(Box.createRigidArea(new Dimension(0,15)));
		add(localGamePanel);

		JButton btnLocalGame = new JButton("Local Game");
		btnLocalGame.setForeground(Color.BLACK);
		btnLocalGame.setBackground(Color.WHITE);
		btnLocalGame.setFont(new Font("Papyrus", Font.PLAIN, 40));
		btnLocalGame.addActionListener(new MenuPanelSwitcher("LocalGamePanel"));
		localGamePanel.add(btnLocalGame);
		
		//Internet Game
		
		JPanel internetGamePanel = new JPanel();
		internetGamePanel.setBackground(new Color(0,0,0,0));
		add(Box.createRigidArea(new Dimension(0,15)));
		add(internetGamePanel);
		
		JButton btnInternetGame = new JButton("Internet Game");
		btnInternetGame.setBackground(Color.WHITE);
		btnInternetGame.setForeground(Color.BLACK);
		btnInternetGame.setFont(new Font("Papyrus", Font.PLAIN, 40));
		btnInternetGame.addActionListener(new MenuPanelSwitcher("InternetGamePanel"));
		internetGamePanel.add(btnInternetGame);

				
		//Rules
		
		JPanel rulesPanel = new JPanel();
		rulesPanel.setBackground(new Color(0,0,0,0));
		rulesPanel.setBorder(null);
		add(Box.createVerticalGlue());
		add(rulesPanel);
		
		JButton btnRules = new JButton("Rules");
		btnRules.setBackground(Color.WHITE);
		btnRules.setForeground(Color.BLACK);
		btnRules.setFont(new Font("Papyrus", Font.PLAIN, 40));
		btnRules.addActionListener(new MenuPanelSwitcher("RulesPanel"));
		rulesPanel.add(btnRules);
		
	}
}
