package it.polimi.dei.provafinale.carcassonne.view.menu;

import it.polimi.dei.provafinale.carcassonne.controller.MenuPanelSwitcher;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JSplitPane;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import javax.swing.Box;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Component;

public class HomePanel extends JPanel {
	public HomePanel() {
		BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
		setLayout(layout);

		Component verticalGlue01 = Box.createVerticalGlue();
		
		JPanel newLocalGamePanel = new JPanel();
		newLocalGamePanel.setLayout(new BoxLayout(newLocalGamePanel,
				BoxLayout.X_AXIS));
		JButton btnNewLocalGame = new JButton("New Local Game");
		newLocalGamePanel.add(btnNewLocalGame);
		ActionListener local = new MenuPanelSwitcher(MenuPanel.LOCALGAMEPANEL);
		btnNewLocalGame.addActionListener(local);

		Component verticalStrut = Box.createVerticalStrut(20);
		
		JPanel newInternetGamePanel = new JPanel();
		newInternetGamePanel.setLayout(new BoxLayout(newInternetGamePanel,
				BoxLayout.X_AXIS));

		JButton btnNewInternetGame = new JButton("New Internet Game");
		newInternetGamePanel.add(btnNewInternetGame);
		ActionListener internet = new MenuPanelSwitcher(MenuPanel.INTERNETGAMEPANEL);
		btnNewInternetGame.addActionListener(internet);
		
		Component verticalGlue02 = Box.createVerticalGlue();
		
		//Add components to panel
		add(verticalGlue01);
		add(newLocalGamePanel);
		add(verticalStrut);
		add(newInternetGamePanel);
		add(verticalGlue02);
		
		repaint();
		
	}

}
