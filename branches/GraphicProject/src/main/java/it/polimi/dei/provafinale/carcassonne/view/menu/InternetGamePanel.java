package it.polimi.dei.provafinale.carcassonne.view.menu;
import it.polimi.dei.provafinale.carcassonne.controller.MenuPanelSwitcher;

import javax.swing.JPanel;
import javax.swing.BoxLayout;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JButton;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JTextField;



public class InternetGamePanel extends JPanel {
	private JTextField textField;
	public InternetGamePanel() {
		
		setBackground(new Color(0,0,0,0));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JPanel titlePanel = new JPanel();
		titlePanel.setLayout(new BorderLayout(0, 0));
		titlePanel.setBackground(new Color(0,0,0,0));
		add(titlePanel);
		
		//new Internet game
		
		JLabel lblNewInternetGame = new JLabel("NEW INTERNET GAME");
		lblNewInternetGame.setForeground(Color.WHITE);
		lblNewInternetGame.setFont(new Font("Papyrus", Font.BOLD, 30));
		lblNewInternetGame.setHorizontalAlignment(SwingConstants.CENTER);
		titlePanel.add(lblNewInternetGame, BorderLayout.CENTER);
		
		//server IP address
		
		JPanel serverIpPanel = new JPanel();
		serverIpPanel.setBackground(new Color(0,0,0,0));
		add(serverIpPanel);
		
		JLabel lblServerIpAddress = new JLabel("Server IP address:");
		lblServerIpAddress.setForeground(Color.WHITE);
		lblServerIpAddress.setFont(new Font("Papyrus", Font.PLAIN, 20));
		serverIpPanel.add(lblServerIpAddress);
		
		//text field
		
		textField = new JTextField();
		textField.setFont(new Font("Papyrus", Font.PLAIN, 20));
		textField.setColumns(10);
		serverIpPanel.add(textField);
		
		//join game
		
		JPanel joinGamePanel = new JPanel();
		joinGamePanel.setLayout(new BorderLayout(0, 0));
		joinGamePanel.setBackground(new Color(0,0,0,0));
		add(joinGamePanel);
		
		JButton btnJoinGame = new JButton("Join Game");
		btnJoinGame.setForeground(Color.WHITE);
		btnJoinGame.setFont(new Font("Papyrus", Font.PLAIN, 23));
		joinGamePanel.add(btnJoinGame, BorderLayout.CENTER);
		
		//back to home
		
		JPanel goHomePanel = new JPanel();
		goHomePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		goHomePanel.setBackground(new Color(0,0,0,0));
		add(goHomePanel);
		
		JButton btnBackToHome = new JButton("Back to Home");
		btnBackToHome.setForeground(Color.WHITE);
		btnBackToHome.setFont(new Font("Papyrus", Font.PLAIN, 15));
		btnBackToHome.addActionListener(new MenuPanelSwitcher("HomePanel"));
		goHomePanel.add(btnBackToHome);
	}

}
