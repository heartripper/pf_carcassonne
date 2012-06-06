package it.polimi.dei.provafinale.carcassonne.view.menu;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import it.polimi.dei.provafinale.carcassonne.controller.client.MenuPanelSwitcher;
import it.polimi.dei.provafinale.carcassonne.controller.client.StartInternetGame;
import javax.swing.JComboBox;

/**
 * The class InternetGamePanel extends a JPanel in order to create the internet
 * menu of the game.
 * 
 */
public class InternetGamePanel extends JPanel {

	private static final int COLUMNS_NUMBER = 10;
	private static final int PIXEL_SPACING = 20;

	private static final long serialVersionUID = 8480541024375120364L;

	private JLabel messageLabel;
	private JTextField serverIPField;
	private JTextField serverPortField;
	private JComboBox connectionComboBox;
	private JComboBox viewComboBox;
	private JButton btnJoinGame;
	private BufferedImage background;

	/**
	 * InternetGamePanel constructor. Creates a new instance of class
	 * InternetGamePanel.
	 * 
	 * @param background
	 *            an Image we want to set as background of the panel.
	 */
	public InternetGamePanel(BufferedImage background) {

		this.background = background;

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		Component verticalGlue01 = Box.createVerticalGlue();
		add(verticalGlue01);

		/* Creating title panel. */
		JPanel titlePanel = new JPanel();
		titlePanel.setBackground(new Color(0, 0, 0, 0));
		titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
		add(titlePanel);
		/* Adding new internet game label to title panel. */
		JLabel lblNewInternetGame = new JLabel("NEW INTERNET GAME");
		titlePanel.add(lblNewInternetGame);

		Component verticalStrut01 = Box.createVerticalStrut(PIXEL_SPACING);
		add(verticalStrut01);

		/* Creating server info panel. */
		JPanel serverInfoPanel = new JPanel();
		serverInfoPanel.setBackground(new Color(0, 0, 0, 0));
		BoxLayout boxLayout = new BoxLayout(serverInfoPanel, BoxLayout.Y_AXIS);
		serverInfoPanel.setLayout(boxLayout);
		add(serverInfoPanel);
		/* Layout option. */
		Component horizontalGlue01 = Box.createHorizontalGlue();
		serverInfoPanel.add(horizontalGlue01);
		/* Creating ip panel to put into server info panel. */
		JPanel ipPanel = new JPanel();
		ipPanel.setBackground(new Color(0, 0, 0, 0));
		/* Adding label insert server ip to put into ip panel. */
		JLabel lblInsertServerIp = new JLabel("Insert server IP address:");
		ipPanel.add(lblInsertServerIp);
		/*
		 * Adding text field, that will contain the server ip address, to put
		 * into ip panel.
		 */
		serverIPField = new JTextField();
		serverIPField.setColumns(COLUMNS_NUMBER);
		ipPanel.add(serverIPField);
		/* Creating port panel to put into server info panel. */
		JPanel portPanel = new JPanel();
		portPanel.setBackground(new Color(0, 0, 0, 0));
		/* Adding label insert port to put into port panel. */
		JLabel lblInsertPort = new JLabel("Insert server port:");
		portPanel.add(lblInsertPort);
		/*
		 * Adding text field, that will contain the server port address, to put
		 * into port panel.
		 */
		serverPortField = new JTextField();
		portPanel.add(serverPortField);
		serverPortField.setColumns(COLUMNS_NUMBER);
		/* Adding ip panel and port panel to server info panel. */
		serverInfoPanel.add(ipPanel);
		serverInfoPanel.add(portPanel);
		/* Layout option. */
		Component horizontalGlue02 = Box.createHorizontalGlue();
		serverInfoPanel.add(horizontalGlue02);

		Component verticalStrut = Box.createVerticalStrut(PIXEL_SPACING);
		add(verticalStrut);

		/* Creating protocol panel. */
		JPanel protocolPanel = new JPanel();
		protocolPanel.setBackground(new Color(0, 0, 0, 0));
		add(protocolPanel);
		/* Adding label select protocol to protocol panel. */
		JLabel lblSelectProtocol = new JLabel("Select protocol: ");
		protocolPanel.add(lblSelectProtocol);
		/* Adding combobox protocol to protocol panel. */
		String[] protocol = { "Socket", "RMI" };
		connectionComboBox = new JComboBox(protocol);
		protocolPanel.add(connectionComboBox);

		/* Creating gui panel. */
		JPanel guiPanel = new JPanel();
		guiPanel.setBackground(new Color(0, 0, 0, 0));
		add(guiPanel);
		/* Adding label select gui to gui panel. */
		JLabel lblSelectGui = new JLabel("Select GUI: ");
		guiPanel.add(lblSelectGui);
		/* Adding combobox gui to gui panel. */
		String[] gui = { "Swing", "Textual" };
		viewComboBox = new JComboBox(gui);
		guiPanel.add(viewComboBox);

		/* Creating join game panel. */
		JPanel joinGamePanel = new JPanel();
		joinGamePanel.setBackground(new Color(0, 0, 0, 0));
		joinGamePanel.setLayout(new BoxLayout(joinGamePanel, BoxLayout.X_AXIS));
		add(joinGamePanel);
		/* Layout option. */
		Component horizontalGlue03 = Box.createHorizontalGlue();
		joinGamePanel.add(horizontalGlue03);
		/* Adding button join game to join game panel. */
		btnJoinGame = new JButton("Join Game!");
		btnJoinGame.addActionListener(new StartInternetGame(this));
		joinGamePanel.add(btnJoinGame);
		/* Layout option. */
		Component horizontalGlue04 = Box.createHorizontalGlue();
		joinGamePanel.add(horizontalGlue04);

		/* Creating message panel. */
		JPanel messagePanel = new JPanel();
		messagePanel.setBackground(new Color(0, 0, 0, 0));
		add(messagePanel);
		/* Adding label message to message panel. */
		messageLabel = new JLabel();
		messagePanel.add(messageLabel);

		Component verticalGlue02 = Box.createVerticalGlue();
		add(verticalGlue02);

		/* Creating back to home panel. */
		JPanel backToHomePanel = new JPanel();
		backToHomePanel.setBackground(new Color(0, 0, 0, 0));
		backToHomePanel.setLayout(new BoxLayout(backToHomePanel,
				BoxLayout.X_AXIS));
		add(backToHomePanel);
		/* Adding button back to home to home panel. */
		JButton btnBackToHome = new JButton("Back to home");
		backToHomePanel.add(btnBackToHome);
		ActionListener home = new MenuPanelSwitcher(MenuPanel.HOMEPANEL);
		btnBackToHome.addActionListener(home);
	}

	/**
	 * Paints the background image on the panel.
	 */
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		int x = (getWidth() - background.getWidth()) / 2;
		int y = (getHeight() -background.getHeight());
		g.drawImage(background, x, y, null);
	}

	/**
	 * Shows a message text in the special label in messagePanel.
	 * 
	 * @param msg
	 *            a message to show in messagePanel.
	 */
	public void setNotifyText(String msg) {
		messageLabel.setText(msg);
	}

	/**
	 * Takes port field value.
	 * 
	 * @return serverPortField content.
	 */
	public String getPortFieldValue() {
		return serverPortField.getText();
	}

	/**
	 * Takes ip field value.
	 * 
	 * @return serverIPField content.
	 */
	public String getIPFieldValue() {
		return serverIPField.getText();
	}

	/**
	 * Takes the connection type.
	 * 
	 * @return the connection type selected in the ComboBox.
	 */
	public int getConnType() {
		return connectionComboBox.getSelectedIndex();
	}

	/**
	 * Takes the GUI type.
	 * 
	 * @return the gui type selected in the ComboBox.
	 */
	public int getViewType() {
		return viewComboBox.getSelectedIndex();
	}

	/**
	 * Sets the activity of user interface.
	 * 
	 * @param active
	 *            if true activates the user interface, if false disactivates
	 *            the user interface.
	 */
	public void setUIActive(boolean active) {
		serverIPField.setEnabled(active);
		serverPortField.setEnabled(active);
		btnJoinGame.setEnabled(active);
	}
}