package it.polimi.dei.provafinale.carcassonne.view.menu;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.BoxLayout;
import java.awt.Font;
import java.awt.Component;
import java.io.InputStream;
import javax.swing.JLabel;
import java.awt.CardLayout;


public class MenuPanel extends JPanel{
	
	private final String HOME = "Home";
	private final String MATCH = "Match";
	private final String LOCAL = "Local";
	private final String INTERNET = "Internet";
	private final String RULES = "Rules";
	private JPanel content;
	private CardLayout layout;
	
	private Image background;
	
	public MenuPanel() {
		
		String path = "src/main/resources/background.png";
		background = Toolkit.getDefaultToolkit().createImage(path);
		
		setLayout(new BorderLayout(0, 0));		
		
		JPanel banner = new JPanel();
		banner.setBackground(Color.GRAY);
		banner.setPreferredSize(new Dimension(10, 150));
		add(banner, BorderLayout.NORTH);
		
		JLabel lblCarcassonne = new JLabel("Carcassonne");
		lblCarcassonne.setForeground(Color.WHITE);
		lblCarcassonne.setFont(new Font("Papyrus", Font.BOLD, 145));
		banner.add(lblCarcassonne);
		
		JPanel container = new JPanel();
		container.setBackground(new Color(0, 0, 0, 0));
		
		add(container, BorderLayout.CENTER);
		
		container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
		
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(600, 10));
		panel.setBackground(new Color(0,0,0,100));
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		container.add(Box.createHorizontalGlue());
		container.add(panel);		
		container.add(Box.createHorizontalGlue());

		content = new JPanel();
		content.setBackground(new Color(0,0,0,0));
		layout = new CardLayout(0, 0);
		content.setLayout(layout);
		content.add(new HomePanel(), "HomePanel");
		content.add(new LocalGamePanel(), "LocalGamePanel");
		content.add(new InternetGamePanel(), "InternetGamePanel");

		panel.add(Box.createVerticalGlue());
		panel.add(content);
		panel.add(Box.createVerticalGlue());
		
		repaint();
	}
	
	public void changePanel(String panelName){
		layout.show(content, panelName);
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(background, 0, 0, null);
	}

}
