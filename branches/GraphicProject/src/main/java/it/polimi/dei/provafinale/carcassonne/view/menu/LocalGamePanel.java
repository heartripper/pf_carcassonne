package it.polimi.dei.provafinale.carcassonne.view.menu;

import it.polimi.dei.provafinale.carcassonne.controller.MenuPanelSwitcher;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JButton;

public class LocalGamePanel extends JPanel {
	public LocalGamePanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		Component verticalGlue01 = Box.createVerticalGlue();
		add(verticalGlue01);
		
		JPanel titlePanel = new JPanel();
		add(titlePanel);
		titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
		
		JLabel lblNewLocalGame = new JLabel("NEW LOCAL GAME");
		titlePanel.add(lblNewLocalGame);
		
		Component verticalStrut_1 = Box.createVerticalStrut(20);
		add(verticalStrut_1);
		
		JPanel selectPlayersNumberPanel = new JPanel();
		add(selectPlayersNumberPanel);
		selectPlayersNumberPanel.setLayout(new FlowLayout());
		
		Component horizontalGlue01 = Box.createHorizontalGlue();
		selectPlayersNumberPanel.add(horizontalGlue01);
		
		JLabel lblInsertPlayersNumber = new JLabel("Insert players number:");
		selectPlayersNumberPanel.add(lblInsertPlayersNumber);
		
		String[] data = {"2", "3", "4", "5"}; 
		JComboBox comboBox = new JComboBox(data);
		selectPlayersNumberPanel.add(comboBox);
		
		Component horizontalGlue02 = Box.createHorizontalGlue();
		selectPlayersNumberPanel.add(horizontalGlue02);
		
		Component verticalStrut = Box.createVerticalStrut(20);
		add(verticalStrut);
		
		JPanel playGamePanel = new JPanel();
		add(playGamePanel);
		playGamePanel.setLayout(new BoxLayout(playGamePanel, BoxLayout.X_AXIS));
		
		Component horizontalGlue03 = Box.createHorizontalGlue();
		playGamePanel.add(horizontalGlue03);
		
		JButton btnPlayGame = new JButton("Play Game!");
		playGamePanel.add(btnPlayGame);
		
		Component horizontalGlue = Box.createHorizontalGlue();
		playGamePanel.add(horizontalGlue);
		
		Component verticalGlue02 = Box.createVerticalGlue();
		add(verticalGlue02);
		
		JPanel backToHomePanel = new JPanel();
		add(backToHomePanel);
		backToHomePanel.setLayout(new BoxLayout(backToHomePanel, BoxLayout.X_AXIS));
		
		JButton btnBackToHome = new JButton("Back to home");
		backToHomePanel.add(btnBackToHome);
		ActionListener home = new MenuPanelSwitcher(MenuPanel.HOMEPANEL);
		btnBackToHome.addActionListener(home);
		
	}

}
