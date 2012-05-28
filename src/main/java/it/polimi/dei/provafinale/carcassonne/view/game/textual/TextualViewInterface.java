package it.polimi.dei.provafinale.carcassonne.view.game.textual;

import it.polimi.dei.provafinale.carcassonne.model.PlayerColor;
import it.polimi.dei.provafinale.carcassonne.model.Tile;
import it.polimi.dei.provafinale.carcassonne.model.TileGrid;
import it.polimi.dei.provafinale.carcassonne.view.CarcassonneFrame;
import it.polimi.dei.provafinale.carcassonne.view.ViewManager;
import it.polimi.dei.provafinale.carcassonne.view.game.swing.GamePanel;
import it.polimi.dei.provafinale.carcassonne.view.viewInterface.ViewInterface;

public class TextualViewInterface implements ViewInterface {

	private TileGridRepresenter gridRepresenter;
	private TextualGamePanel gamePanel;
	private PlayerColor playerColor;

	@Override
	public void initialize(TileGrid grid, int numPlayers,
			PlayerColor clientColor) {
		
		this.gamePanel = new TextualGamePanel();
		this.gridRepresenter = new TileGridRepresenter(grid);
		
		// Append game panel to frame
		CarcassonneFrame frame = ViewManager.getInstance().getFrame();
		frame.setGamePanel(gamePanel);
		frame.changeMainPanel(CarcassonneFrame.GAMEPANEL);

		String notification = String.format("Match starts with %s players.\n",
				numPlayers);
		if (clientColor != null) {
			notification += String.format("You are player %s.\n", clientColor);
		}
		
		gamePanel.print(notification);
	}

	@Override
	public void updateGridRepresentation() {
		gamePanel.print(gridRepresenter.getRepresentation());
	}

	@Override
	public void updateCurrentTile(String rep) {
		Tile tile = new Tile(rep);
		String tileRepresentation = TileGridRepresenter
				.getTileRepresentation(tile);
		gamePanel.print("Your tile:\n" + tileRepresentation);
	}

	@Override
	public void updateScore(String msg) {
		gamePanel.print("Players scores: " + msg);
	}

	@Override
	public void setUIActive(boolean enabled) {
		if(enabled){
			gamePanel.print("Enter command: ");
		}
	}

	@Override
	public void setCurrentPlayer(PlayerColor color) {
		if (playerColor != null && playerColor.equals(color)) {
			gamePanel.print("It's your turn");
		} else {
			gamePanel.print(String.format("It's player %s turn",
					color.getFullName()));
		}

	}

	@Override
	public void showNotify(String msg) {
		gamePanel.print(msg);
	}

}
