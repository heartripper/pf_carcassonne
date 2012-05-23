package it.polimi.dei.provafinale.carcassonne.model.server;

import java.util.ArrayList;
import java.util.List;

import it.polimi.dei.provafinale.carcassonne.model.gamelogic.player.PlayerColor;

public class PlayersDisconnectedException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	private List<PlayerColor> colors;
	
	public PlayersDisconnectedException(PlayerColor color){
		super();
		colors = new ArrayList<PlayerColor>();
		colors.add(color);
	}

	public PlayersDisconnectedException(List<PlayerColor> colors){
		super();
		this.colors = colors;
	}
	
	public void add(List<PlayerColor> newColors){
		colors.addAll(newColors);
	}
	
	public List<PlayerColor> getDisconnectedPlayers(){
		return colors;
	}
}
