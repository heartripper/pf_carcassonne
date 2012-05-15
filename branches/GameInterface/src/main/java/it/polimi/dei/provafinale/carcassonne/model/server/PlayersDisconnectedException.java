package it.polimi.dei.provafinale.carcassonne.model.server;

import java.util.ArrayList;

import it.polimi.dei.provafinale.carcassonne.model.player.PlayerColor;

public class PlayersDisconnectedException extends Exception {
	
	private ArrayList<PlayerColor> colors;
	
	public PlayersDisconnectedException(PlayerColor color){
		super();
		colors = new ArrayList<PlayerColor>();
		colors.add(color);
	}

	public PlayersDisconnectedException(ArrayList<PlayerColor> colors){
		super();
		this.colors = colors;
	}
	
	public void add(ArrayList<PlayerColor> newColors){
		colors.addAll(newColors);
	}
	
	public ArrayList<PlayerColor> getDisconnectedPlayers(){
		return colors;
	}
}
