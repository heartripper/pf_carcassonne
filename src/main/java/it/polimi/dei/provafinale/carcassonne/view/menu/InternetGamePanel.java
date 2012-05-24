package it.polimi.dei.provafinale.carcassonne.view.menu;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import it.polimi.dei.provafinale.carcassonne.controller.MenuPanelSwitcher;
import it.polimi.dei.provafinale.carcassonne.controller.client.StartInternetGame;

public class InternetGamePanel extends JPanel {

	private static final long serialVersionUID = 8480541024375120364L;

	private JLabel messageLabel;
	private JTextField serverIPField;
	private JTextField serverPortField;
	private JButton btnJoinGame;

	private BufferedImage background;

	public InternetGamePanel(BufferedImage background) {
		this.background = background;

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		Component verticalGlue01 = Box.createVerticalGlue();
		add(verticalGlue01);

		JPanel titlePanel = new JPanel();
		titlePanel.setBackground(new Color(0, 0, 0, 0));
		add(titlePanel);
		titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));

		JLabel lblNewInternetGame = new JLabel("NEW INTERNET GAME");
		titlePanel.add(lblNewInternetGame);

		Component verticalStrut01 = Box.createVerticalStrut(20);
		add(verticalStrut01);

		JPanel serverInfoPanel = new JPanel();
		serverInfoPanel.setBackground(new Color(0, 0, 0, 0));
		serverInfoPanel.setPreferredSize(new Dimension(10, 0));
		add(serverInfoPanel);
		BoxLayout boxLayout = new BoxLayout(serverInfoPanel, BoxLayout.Y_AXIS);
		serverInfoPanel.setLayout(boxLayout);

		Component horizontalGlue01 = Box.createHorizontalGlue();
		serverInfoPanel.add(horizontalGlue01);

		JPanel ipPanel = new JPanel();
		ipPanel.setBackground(new Color(0, 0, 0, 0));
		JLabel lblInsertServerIp = new JLabel("Insert server IP address:");
		ipPanel.add(lblInsertServerIp);
		serverIPField = new JTextField();
		ipPanel.add(serverIPField);
		serverIPField.setColumns(10);

		JPanel portPanel = new JPanel();
		portPanel.setBackground(new Color(0, 0, 0, 0));
		JLabel lblInsertPort = new JLabel("Insert server port:");
		portPanel.add(lblInsertPort);
		serverPortField = new JTextField();
		portPanel.add(serverPortField);
		serverPortField.setColumns(10);

		serverInfoPanel.add(ipPanel);
		serverInfoPanel.add(portPanel);

		Component horizontalGlue02 = Box.createHorizontalGlue();
		serverInfoPanel.add(horizontalGlue02);

		Component verticalStrut = Box.createVerticalStrut(20);
		add(verticalStrut);

		JPanel joinGamePanel = new JPanel();
		joinGamePanel.setBackground(new Color(0, 0, 0, 0));
		add(joinGamePanel);
		joinGamePanel.setLayout(new BoxLayout(joinGamePanel, BoxLayout.X_AXIS));

		Component horizontalGlue03 = Box.createHorizontalGlue();
		joinGamePanel.add(horizontalGlue03);

		btnJoinGame = new JButton("Join Game!");
		joinGamePanel.add(btnJoinGame);
		btnJoinGame.addActionListener(new StartInternetGame(this));

		Component horizontalGlue04 = Box.createHorizontalGlue();
		joinGamePanel.add(horizontalGlue04);

		JPanel messagePanel = new JPanel();
		messagePanel.setBackground(new Color(0, 0, 0, 0));
		add(messagePanel);

		messageLabel = new JLabel();
		messagePanel.add(messageLabel);

		Component verticalGlue02 = Box.createVerticalGlue();
		add(verticalGlue02);

		JPanel backToHomePanel = new JPanel();
		backToHomePanel.setBackground(new Color(0, 0, 0, 0));
		add(backToHomePanel);
		backToHomePanel.setLayout(new BoxLayout(backToHomePanel,
				BoxLayout.X_AXIS));

		JButton btnBackToHome = new JButton("Back to home");
		backToHomePanel.add(btnBackToHome);
		ActionListener home = new MenuPanelSwitcher(MenuPanel.HOMEPANEL);
		btnBackToHome.addActionListener(home);
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		int width = getWidth();
		g.drawImage(background, (width - 778) / 2, 0, null);
	}

	public void setNotifyText(String msg) {
		messageLabel.setText(msg);
	}

	public String getPortFieldValue() {
		return serverPortField.getText();
	}

	public String getIPFieldValue() {
		return serverIPField.getText();
	}

	public void setUIActive(boolean active) {
		serverIPField.setEnabled(active);
		serverPortField.setEnabled(active);
		btnJoinGame.setEnabled(active);
	}
}