package it.polimi.dei.provafinale.carcassonne.view.game;

import it.polimi.dei.provafinale.carcassonne.PlayerColor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;

import javax.swing.border.EtchedBorder;

public class PlayerPanel extends JPanel {

	private static final long serialVersionUID = -4975696838928188946L;

	private PlayerColor playerColor;

	private JPanel colorPanel;
	private JLabel lblName;
	private JLabel lblScore;
	private JLabel lblFollower;

	// Background colors
	private final Color selectedBGColor = new Color(255, 255, 255);
	private final Color unselectedBGColor = new Color(225, 225, 225);
	private final Color transparentBGColor = new Color(0, 0, 0, 0);

	// Fonts
	private final Font selNameFont = new Font(Font.SANS_SERIF, Font.BOLD, 15);
	private final Font unselNameFont = new Font(Font.SANS_SERIF, Font.PLAIN, 15);
	private final Font commonFont = new Font(Font.SANS_SERIF, Font.PLAIN, 13);

	// Dimension
	private final Dimension panelDimension = new Dimension(135, 70);
	private final Dimension labelDimension = new Dimension(10,20);
	private final Dimension colorDimension = new Dimension(20, 15);

	public PlayerPanel(PlayerColor playerColor) {
		setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		this.playerColor = playerColor;

		setPreferredSize(panelDimension);
		setLayout(new BorderLayout());

		colorPanel = new JPanel();
		colorPanel.setBackground(playerColor.getColor());
		colorPanel.setPreferredSize(colorDimension);
		add(colorPanel, BorderLayout.WEST);

		JPanel content = new JPanel();
		content.setBackground(transparentBGColor);
		add(content, BorderLayout.CENTER);
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

		String name = playerColor.getFullName();
		JPanel namePanel = new JPanel();
		namePanel.setBackground(transparentBGColor);
		namePanel.setPreferredSize(labelDimension);
		lblName = new JLabel(name);
		namePanel.add(lblName);

		JPanel scorePanel = new JPanel();
		scorePanel.setPreferredSize(labelDimension);
		scorePanel.setBackground(transparentBGColor);
		lblScore = new JLabel("Score: 0");
		lblScore.setFont(commonFont);
		scorePanel.add(lblScore);

		JPanel followerPanel = new JPanel();
		followerPanel.setPreferredSize(labelDimension);
		followerPanel.setBackground(transparentBGColor);
		lblFollower = new JLabel("Follower: 7");
		lblFollower.setFont(commonFont);
		followerPanel.add(lblFollower);

		content.add(namePanel);
		content.add(scorePanel);
		content.add(followerPanel);
	}

	public void setScore(int score) {
		lblScore.setText("Score: " + score);
	}

	public void setFollowers(int numFollowers) {
		lblFollower.setText("Follower: " + numFollowers);
	}

	public void setActive(boolean active) {
		Font f;
		if (active) {
			f = selNameFont;
			setBackground(selectedBGColor);
		} else {
			f = unselNameFont;
			setBackground(unselectedBGColor);
		}

		lblName.setFont(f);
	}

	public void setClientPlayer() {
		lblName.setText(playerColor.getFullName() + " (You)");
	}
}
