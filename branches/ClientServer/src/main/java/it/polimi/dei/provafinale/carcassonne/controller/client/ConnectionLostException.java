package it.polimi.dei.provafinale.carcassonne.controller.client;

public class ConnectionLostException extends Exception{

	private static final long serialVersionUID = 7560807455345741619L;

	public ConnectionLostException(){
		super();
	}
	
	public ConnectionLostException(String s){
		super(s);
	}
}
