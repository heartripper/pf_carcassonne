package it.polimi.dei.provafinale.carcassonne.controller.client;

public class MatchController {

	private static Thread thread;
	private static MatchControllerImpl currentMatch;
	
	public static void startNewMatchController(ClientInterface ci){
		if(thread != null){
			return;
		}
		
		currentMatch = new MatchControllerImpl(ci);
		thread = new Thread(currentMatch);
		thread.start();
	}
	
	public static MatchControllerImpl getCurrentMatchController(){
		return currentMatch;
	}
	
}
