package it.polimi.dei.provafinale.carcassonne.logger;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

/**
 * The class LoggerService serves as container of the Logger.
 * 
 */
public class LoggerService {

	private final int width = 800;
	private final int height = 600;

	private JTabbedPane tabs;

	private static LoggerService instance = null;

	/**
	 * Gives the unique active instance of LoggerService (if it doesn't exists
	 * creates a new one).
	 * 
	 * @return the active instance of LoggerService.
	 */
	public static synchronized LoggerService getService() {
		/* We have to create a new instance of LoggerService. */
		if (instance == null) {
			instance = new LoggerService();
		}
		return instance;
	}

	/**
	 * LoggerService constructor. Creates a new instance of class LoggerService.
	 */
	private LoggerService() {
		/* Creating a new JFrame. */
		JFrame frame = new JFrame("Logger");
		frame.setSize(width, height);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		/* Setting JTabbedPane. */
		tabs = new JTabbedPane();
		frame.add(tabs, BorderLayout.CENTER);
		// frame.setVisible(true);
	}

	/**
	 * Return the Logger contained into tabs (a TabbedPane):
	 * 
	 * @param panelName
	 *            the name to give to the TabbedPane.
	 * @return the Logger contained into tabs.
	 */
	public Logger register(String panelName) {
		Logger logger = new Logger();
		/*
		 * Creates a new TabbedPane that has the name given by the String
		 * panelName and contains a Logger.
		 */
		tabs.addTab(panelName, logger);
		return logger;
	}

}
