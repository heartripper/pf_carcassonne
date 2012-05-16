package it.polimi.dei.provafinale.carcassonne.view.menu;

import it.polimi.dei.provafinale.carcassonne.controller.MenuPanelSwitcher;

import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Component;
import javax.swing.Box;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.CardLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionListener;

public class InternetGamePanel extends JPanel {
	private JTextField textField;
	public InternetGamePanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		Component verticalGlue01 = Box.createVerticalGlue();
		add(verticalGlue01);
		
		JPanel titlePanel = new JPanel();
		add(titlePanel);
		titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
		
		JLabel lblNewInternetGame = new JLabel("NEW INTERNET GAME");
		titlePanel.add(lblNewInternetGame);
		
		Component verticalStrut_1 = Box.createVerticalStrut(20);
		add(verticalStrut_1);
		
		JPanel insertServerIpAddressPanel = new JPanel();
		insertServerIpAddressPanel.setPreferredSize(new Dimension(10, 0));
		add(insertServerIpAddressPanel);
		LayoutManager layout = new FlowLayout();
		insertServerIpAddressPanel.setLayout(layout);
		
		Component horizontalGlue01 = Box.createHorizontalGlue();
		insertServerIpAddressPanel.add(horizontalGlue01);
		
		JLabel lblInsertServerIp = new JLabel("Insert server IP address:");
		insertServerIpAddressPanel.add(lblInsertServerIp);
		
		textField = new JTextField();
		insertServerIpAddressPanel.add(textField);
		textField.setColumns(10);
		
		Component horizontalGlue02 = Box.createHorizontalGlue();
		insertServerIpAddressPanel.add(horizontalGlue02);
		
		Component verticalStrut = Box.createVerticalStrut(20);
		add(verticalStrut);
		
		JPanel joinGamePanel = new JPanel();
		add(joinGamePanel);
		joinGamePanel.setLayout(new BoxLayout(joinGamePanel, BoxLayout.X_AXIS));
		
		Component horizontalGlue03 = Box.createHorizontalGlue();
		joinGamePanel.add(horizontalGlue03);
		
		JButton btnJoinGame = new JButton("Join Game!");
		joinGamePanel.add(btnJoinGame);
		
		Component horizontalGlue04 = Box.createHorizontalGlue();
		joinGamePanel.add(horizontalGlue04);
		
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
