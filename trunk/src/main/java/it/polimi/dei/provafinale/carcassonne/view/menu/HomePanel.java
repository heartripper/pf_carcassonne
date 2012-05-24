package it.polimi.dei.provafinale.carcassonne.view.menu;

import it.polimi.dei.provafinale.carcassonne.controller.MenuPanelSwitcher;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JButton;
import javax.swing.BoxLayout;
import javax.swing.Box;
import java.awt.event.ActionListener;
import java.awt.Component;

public class HomePanel extends JPanel {

	private static final long serialVersionUID = 8988417171482193429L;
	private Image background;

	public HomePanel(Image background) {
		this.background = background;

		BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
		setLayout(layout);

		Component verticalGlue01 = Box.createVerticalGlue();

		JPanel newLocalGamePanel = new JPanel();
		newLocalGamePanel.setBackground(new Color(0, 0, 0, 0));
		newLocalGamePanel.setLayout(new BoxLayout(newLocalGamePanel,
				BoxLayout.X_AXIS));
		JButton btnNewLocalGame = new JButton("New Local Game");
		newLocalGamePanel.add(btnNewLocalGame);
		ActionListener local = new MenuPanelSwitcher(MenuPanel.LOCALGAMEPANEL);
		btnNewLocalGame.addActionListener(local);

		Component verticalStrut = Box.createVerticalStrut(20);

		JPanel newInternetGamePanel = new JPanel();
		newInternetGamePanel.setBackground(new Color(0, 0, 0, 0));
		newInternetGamePanel.setLayout(new BoxLayout(newInternetGamePanel,
				BoxLayout.X_AXIS));

		JButton btnNewInternetGame = new JButton("New Internet Game");
		newInternetGamePanel.add(btnNewInternetGame);
		ActionListener internet = new MenuPanelSwitcher(
				MenuPanel.INTERNETGAMEPANEL);
		btnNewInternetGame.addActionListener(internet);

		Component verticalGlue02 = Box.createVerticalGlue();

		// Add components to panel
		add(verticalGlue01);
		add(newLocalGamePanel);
		add(verticalStrut);
		add(newInternetGamePanel);
		add(verticalGlue02);

		repaint();
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		int width = getWidth();
		g.drawImage(background, (width - 778) / 2, 0, null);
	}

}
