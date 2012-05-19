package it.polimi.dei.provafinale.carcassonne.model;

public class NotEnoughPlayersException extends Exception{

	private static final long serialVersionUID = 5515480920138514643L;

	public NotEnoughPlayersException(){
		super();
	}
	
	public NotEnoughPlayersException(String s){
		super(s);
	}
}
