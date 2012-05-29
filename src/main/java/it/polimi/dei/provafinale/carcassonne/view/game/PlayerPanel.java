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

	/* Background colors. */
	private final Color selectedBGColor = new Color(255, 255, 255);
	private final Color unselectedBGColor = new Color(225, 225, 225);
	private final Color transparentBGColor = new Color(0, 0, 0, 0);

	/* Fonts. */
	private final Font selNameFont = new Font(Font.SANS_SERIF, Font.BOLD, 15);
	private final Font unselNameFont = new Font(Font.SANS_SERIF, Font.PLAIN, 15);
	private final Font commonFont = new Font(Font.SANS_SERIF, Font.PLAIN, 13);

	/* Dimension. */
	private final Dimension panelDimension = new Dimension(135, 70);
	private final Dimension labelDimension = new Dimension(10, 20);
	private final Dimension colorDimension = new Dimension(20, 15);

	/**
	 * PlayerPanel constructor. Creates a new instance of class PlayerPanel.
	 * 
	 * @param playerColor
	 *            the color of the player, which the panel refers to.
	 */
	public PlayerPanel(PlayerColor playerColor) {

		setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		setPreferredSize(panelDimension);
		setLayout(new BorderLayout());

		this.playerColor = playerColor;

		/*
		 * Setting the color panel (to put on the left side of player
		 * informations).
		 */
		colorPanel = new JPanel();
		colorPanel.setBackground(playerColor.getColor());
		colorPanel.setPreferredSize(colorDimension);
		add(colorPanel, BorderLayout.WEST);

		/*
		 * Setting content panel, which contains all the informations about a
		 * player (name, score, follower).
		 */
		JPanel content = new JPanel();
		content.setBackground(transparentBGColor);
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
		add(content, BorderLayout.CENTER);
		/*
		 * Setting name panel (contains the name that identifies a player) to
		 * put into content panel.
		 */
		JPanel namePanel = new JPanel();
		String name = playerColor.getFullName();
		lblName = new JLabel(name);
		namePanel.setBackground(transparentBGColor);
		namePanel.setPreferredSize(labelDimension);
		namePanel.add(lblName);
		/*
		 * Setting score panel (contains the actual score of a player) to put
		 * into content panel.
		 */
		JPanel scorePanel = new JPanel();
		lblScore = new JLabel("Score: 0");
		lblScore.setFont(commonFont);
		scorePanel.setPreferredSize(labelDimension);
		scorePanel.setBackground(transparentBGColor);
		scorePanel.add(lblScore);
		/*
		 * Setting followers panel (contains the actual number of players that
		 * belongs to a player) to put into content panel.
		 */
		JPanel followerPanel = new JPanel();
		lblFollower = new JLabel("Follower: 7");
		lblFollower.setFont(commonFont);
		followerPanel.setPreferredSize(labelDimension);
		followerPanel.setBackground(transparentBGColor);
		followerPanel.add(lblFollower);
		/* Adding panels to content panel. */
		content.add(namePanel);
		content.add(scorePanel);
		content.add(followerPanel);
	}

	/**
	 * Sets the actual score of a player into the JLabel lblScore (attribute of
	 * the class PlayerPanel).
	 * 
	 * @param score
	 *            the score we want to add to the current one.
	 */
	public void setScore(int score) {
		lblScore.setText("Score: " + score);
	}

	/**
	 * Sets the actual number of followers that belongs to a player into the
	 * JLabel lblFollowers (attribute of the class PlayerPanel).
	 * 
	 * @param numFollowers
	 *            a number that indicates the current number of followers of a
	 *            player.
	 */
	public void setFollowers(int numFollowers) {
		lblFollower.setText("Follower: " + numFollowers);
	}

	/**
	 * Sets the status of a player: if a player is active the background color
	 * of his panel will be white and the font-weight bold, otherwise of the
	 * default color and the default font-size.
	 * 
	 * @param active
	 *            if true indicates that is the player turn, if false indicates
	 *            that is the turn of another player.
	 */
	public void setActive(boolean active) {
		Font f;
		/* It's current player turn. */
		if (active) {
			f = selNameFont;
			setBackground(selectedBGColor);
		}
		/* It's another player turn. */
		else {
			f = unselNameFont;
			setBackground(unselectedBGColor);
		}
		lblName.setFont(f);
	}

	/**
	 * Sets this player as the one playing on the client.
	 */
	public void setClientPlayer() {
		lblName.setText(playerColor.getFullName() + " (You)");
	}
}
