package it.polimi.dei.provafinale.carcassonne.view.game;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.BoxLayout;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JScrollPane;
import javax.swing.JLabel;
import java.awt.FlowLayout;
import javax.swing.JTextField;
import javax.swing.JRadioButton;
import javax.swing.JComboBox;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.SwingConstants;

public class GamePanel extends JPanel {
	private JTextField textField;

	public GamePanel() {
		setLayout(new BorderLayout(0, 0));

		JPanel gamePanel = new JPanel();
		add(gamePanel, BorderLayout.CENTER);

		JLabel tilesLabel = new JLabel("");

		JPanel gridPanel = new JPanel();
		gridPanel.setLayout(new GridLayout());

		Dimension dim = new Dimension(10, 10000);
		gamePanel.setLayout(new BoxLayout(gamePanel, BoxLayout.X_AXIS));
		tilesLabel.setSize(dim);
		tilesLabel.setMaximumSize(dim);
		tilesLabel.setMinimumSize(dim);
		tilesLabel.setPreferredSize(dim);

		JPanel tilesPanel = new JPanel();
		tilesPanel.setBackground(new Color(0, 0, 0, 0));
		tilesPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(tilesPanel);
		tilesPanel.add(tilesLabel);
		gamePanel.add(scrollPane);

		JPanel notificationPanel = new JPanel();
		notificationPanel.setBackground(Color.GRAY);
		notificationPanel.setPreferredSize(new Dimension(195, 10));
		add(notificationPanel, BorderLayout.EAST);
		notificationPanel.setLayout(new BoxLayout(notificationPanel,
				BoxLayout.Y_AXIS));

		JPanel cardPanel = new JPanel();
		notificationPanel.add(cardPanel);

		JPanel rotatePanel = new JPanel();
		notificationPanel.add(rotatePanel);

		JButton btnRotate = new JButton("Rotate");
		rotatePanel.add(btnRotate);

		JPanel insertCoordinatesPanel = new JPanel();
		notificationPanel.add(insertCoordinatesPanel);
		insertCoordinatesPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel lblInsertCoordinatesxy = new JLabel("Insert coordinates (x,y):");
		insertCoordinatesPanel.add(lblInsertCoordinatesxy);
		
		textField = new JTextField();
		insertCoordinatesPanel.add(textField);
		textField.setColumns(10);

		JPanel followerPanel = new JPanel();
		notificationPanel.add(followerPanel);
		followerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JLabel lblFollower = new JLabel("Follower:");
		followerPanel.add(lblFollower);

		String[] followerOptions = { "none", "north", "east", "south", "west" };
		JComboBox comboBox = new JComboBox(followerOptions);
		followerPanel.add(comboBox);

		JPanel putPanel = new JPanel();
		notificationPanel.add(putPanel);

		JButton btnPut = new JButton("Put!");
		putPanel.add(btnPut);
		
		JPanel takeScreenshotPanel = new JPanel();
		notificationPanel.add(takeScreenshotPanel);
		
		JButton btnTakeAScreenshot = new JButton("Take a ScreenShot");
		takeScreenshotPanel.add(btnTakeAScreenshot);

		JPanel bottomPanel = new JPanel();
		add(bottomPanel, BorderLayout.SOUTH);
		bottomPanel.setLayout(new BorderLayout(0, 0));

		JPanel backToHomePanel = new JPanel();
		bottomPanel.add(backToHomePanel, BorderLayout.EAST);

		JButton btnBackToHome = new JButton("Back to Home");
		btnBackToHome.setFont(new Font("Papyrus", Font.PLAIN, 15));
		backToHomePanel.add(btnBackToHome);

		JPanel playerPanel = new JPanel();
		bottomPanel.add(playerPanel, BorderLayout.WEST);

	}

}
