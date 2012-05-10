package it.polimi.dei.provafinale.carcassonne.view.menu;
import it.polimi.dei.provafinale.carcassonne.controller.MenuPanelSwitcher;

import javax.swing.JPanel;
import javax.swing.BoxLayout;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JTextField;



public class InternetGamePanel extends JPanel {
	private JTextField textField;
	public InternetGamePanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JPanel titlePanel = new JPanel();
		add(titlePanel);
		titlePanel.setLayout(new BorderLayout(0, 0));
		
		JLabel lblNewInternetGame = new JLabel("NEW INTERNET GAME");
		lblNewInternetGame.setFont(new Font("Papyrus", Font.BOLD, 30));
		lblNewInternetGame.setHorizontalAlignment(SwingConstants.CENTER);
		titlePanel.add(lblNewInternetGame, BorderLayout.CENTER);
		
		JPanel serverIpPanel = new JPanel();
		add(serverIpPanel);
		
		JLabel lblServerIpAddress = new JLabel("Server IP address:");
		lblServerIpAddress.setFont(new Font("Papyrus", Font.PLAIN, 20));
		serverIpPanel.add(lblServerIpAddress);
		
		textField = new JTextField();
		textField.setFont(new Font("Papyrus", Font.PLAIN, 20));
		serverIpPanel.add(textField);
		textField.setColumns(10);
		
		JPanel joinGamePanel = new JPanel();
		add(joinGamePanel);
		joinGamePanel.setLayout(new BorderLayout(0, 0));
		
		JButton btnJoinGame = new JButton("Join Game");
		btnJoinGame.setFont(new Font("Papyrus", Font.PLAIN, 23));
		joinGamePanel.add(btnJoinGame, BorderLayout.CENTER);
		
		JPanel goHomePanel = new JPanel();
		goHomePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(goHomePanel);
		
		JButton btnBackToHome = new JButton("Back to Home");
		btnBackToHome.setFont(new Font("Papyrus", Font.PLAIN, 15));
		btnBackToHome.addActionListener(new MenuPanelSwitcher("HomePanel"));
		goHomePanel.add(btnBackToHome);
	}

}
