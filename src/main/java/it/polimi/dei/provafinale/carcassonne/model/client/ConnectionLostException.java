package it.polimi.dei.provafinale.carcassonne.model.client;

public class ConnectionLostException extends Exception{
	public ConnectionLostException(){
		super();
	}
	
	public ConnectionLostException(String s){
		super(s);
	}
}
