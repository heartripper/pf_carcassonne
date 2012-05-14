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
		JButton btnLocalGame = new JButton("Local Game");
		localGamePanel.add(btnLocalGame);
		btnLocalGame.setFont(new Font("Papyrus", Font.PLAIN, 40));
		btnLocalGame.addActionListener(new MenuPanelSwitcher("LocalGamePanel"));
		
		add(localGamePanel);
		add(Box.createRigidArea(new Dimension(0,15)));
		
		//Internet Game
		JPanel internetGamePanel = new JPanel();
		JButton btnInternetGame = new JButton("Internet Game");
		internetGamePanel.add(btnInternetGame);
		btnInternetGame.setFont(new Font("Papyrus", Font.PLAIN, 40));
		btnInternetGame.addActionListener(new MenuPanelSwitcher("InternetGamePanel"));
		
		add(internetGamePanel);
		add(Box.createRigidArea(new Dimension(0,15)));
				
		//Rules
		JPanel rulesPanel = new JPanel();
		rulesPanel.setBorder(null);
		JButton btnRules = new JButton("Rules");
		rulesPanel.add(btnRules);
		btnRules.setFont(new Font("Papyrus", Font.PLAIN, 40));
		btnRules.addActionListener(new MenuPanelSwitcher("RulesPanel"));
		
		add(rulesPanel);
		add(Box.createVerticalGlue());
	}
}
