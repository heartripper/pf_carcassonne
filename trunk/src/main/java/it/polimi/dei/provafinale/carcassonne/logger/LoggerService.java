package it.polimi.dei.provafinale.carcassonne.logger;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class LoggerService {

	private final int width = 800;
	private final int height = 600;
	
	private JTabbedPane tabs;
	private static LoggerService instance = null;
	
	
	public static synchronized LoggerService getService(){
		if(instance == null){
			instance = new LoggerService();
		}
		
		return instance;
	}
	
	private LoggerService() {
		JFrame frame = new JFrame("Logger");
		frame.setSize(width, height);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		tabs = new JTabbedPane();
		frame.add(tabs, BorderLayout.CENTER);
		frame.setVisible(true);
	}
	
	public Logger register(String panelName){
		Logger logger = new Logger();
		tabs.addTab(panelName, logger);
		return logger;
	}

}
