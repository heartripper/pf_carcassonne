package it.polimi.dei.provafinale.carcassonne.view.menu;

import it.polimi.dei.provafinale.carcassonne.controller.MenuPanelSwitcher;
import it.polimi.dei.provafinale.carcassonne.controller.client.StartLocalGame;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.BoxLayout;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JButton;

public class LocalGamePanel extends JPanel {

	private static final long serialVersionUID = -6424162656647447061L;
	private Image background;

	public LocalGamePanel(Image background) {
		this.background = background;

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		Component verticalGlue01 = Box.createVerticalGlue();
		add(verticalGlue01);

		JPanel titlePanel = new JPanel();
		titlePanel.setBackground(new Color(0, 0, 0, 0));
		add(titlePanel);
		titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));

		JLabel lblNewLocalGame = new JLabel("NEW LOCAL GAME");
		titlePanel.add(lblNewLocalGame);

		Component verticalStrut01 = Box.createVerticalStrut(20);
		add(verticalStrut01);

		JPanel selectPlayersNumberPanel = new JPanel();
		selectPlayersNumberPanel.setBackground(new Color(0, 0, 0, 0));
		add(selectPlayersNumberPanel);
		selectPlayersNumberPanel.setLayout(new FlowLayout());

		Component horizontalGlue01 = Box.createHorizontalGlue();
		selectPlayersNumberPanel.add(horizontalGlue01);

		JLabel lblInsertPlayersNumber = new JLabel("Insert players number:");
		selectPlayersNumberPanel.add(lblInsertPlayersNumber);

		String[] data = { "2", "3", "4", "5" };
		JComboBox playerComboBox = new JComboBox(data);
		selectPlayersNumberPanel.add(playerComboBox);

		Component horizontalGlue02 = Box.createHorizontalGlue();
		selectPlayersNumberPanel.add(horizontalGlue02);

		Component verticalStrut = Box.createVerticalStrut(20);
		add(verticalStrut);
		
		JPanel guiPanel = new JPanel();
		guiPanel.setBackground(new Color(0,0,0,0));
		add(guiPanel);
		
		JLabel lblSelectGui = new JLabel("Select GUI:");
		guiPanel.add(lblSelectGui);
		
		String[] gui = {"Swing", "Textual"};
		JComboBox guiComboBox = new JComboBox(gui);
		guiPanel.add(guiComboBox);

		JPanel playGamePanel = new JPanel();
		playGamePanel.setBackground(new Color(0, 0, 0, 0));
		add(playGamePanel);
		playGamePanel.setLayout(new BoxLayout(playGamePanel, BoxLayout.X_AXIS));

		Component horizontalGlue03 = Box.createHorizontalGlue();
		playGamePanel.add(horizontalGlue03);

		JButton btnPlayGame = new JButton("Play Game!");
		btnPlayGame.addActionListener(new StartLocalGame(playerComboBox,guiComboBox));
		playGamePanel.add(btnPlayGame);

		Component horizontalGlue = Box.createHorizontalGlue();
		playGamePanel.add(horizontalGlue);

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

}
