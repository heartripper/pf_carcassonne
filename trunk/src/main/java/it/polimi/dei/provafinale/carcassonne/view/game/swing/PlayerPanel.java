package it.polimi.dei.provafinale.carcassonne.view.game.swing;

import it.polimi.dei.provafinale.carcassonne.model.PlayerColor;

import java.awt.Dimension;

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

	public PlayerPanel(PlayerColor playerColor) {
		setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		this.playerColor = playerColor;
		
		setPreferredSize(new Dimension(135, 70));
		setLayout(new BorderLayout());

		colorPanel = new JPanel();
		colorPanel.setBackground(playerColor.getColor());
		colorPanel.setPreferredSize(new Dimension(20,15));
		add(colorPanel, BorderLayout.WEST);
		
		JPanel content = new JPanel();
		add(content, BorderLayout.CENTER);
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
		
		String name = playerColor.getFullName();
		JPanel namePanel = new JPanel();
		namePanel.setPreferredSize(new Dimension(10, 20));
		lblName = new JLabel(name);
		namePanel.add(lblName);
		
		JPanel scorePanel = new JPanel();
		scorePanel.setPreferredSize(new Dimension(10, 20));
		lblScore = new JLabel("Score: 0");
		scorePanel.add(lblScore);
		
		JPanel followerPanel = new JPanel();
		followerPanel.setPreferredSize(new Dimension(10, 20));
		lblFollower = new JLabel("Follower: 7");
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

	}

	public void setClientPlayer(){
		lblName.setText(playerColor.getFullName() + " (You)");
	}
}
