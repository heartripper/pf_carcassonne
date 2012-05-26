package it.polimi.dei.provafinale.carcassonne.view.viewInterface;

import it.polimi.dei.provafinale.carcassonne.model.gamelogic.card.TileGrid;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.player.PlayerColor;

public interface ViewInterface {
	public abstract void initialize(TileGrid grid, int numPlayers,
			PlayerColor clientColor);

	public abstract void updateGridRepresentation();

	public abstract void updateCurrentTile(String rep);

	public abstract void updateScore(String msg);

	public abstract void showNotify(String msg);
	
	public abstract void setUIActive(boolean enabled);
	
	public abstract void setCurrentPlayer(PlayerColor color);
}
