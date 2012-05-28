package it.polimi.dei.provafinale.carcassonne.view.game.textual;

import it.polimi.dei.provafinale.carcassonne.model.gamelogic.player.PlayerColor;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.tile.Tile;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.tile.TileGrid;
import it.polimi.dei.provafinale.carcassonne.view.CarcassonneFrame;
import it.polimi.dei.provafinale.carcassonne.view.ViewManager;
import it.polimi.dei.provafinale.carcassonne.view.viewInterface.ViewInterface;

public class TextualView implements ViewInterface {

	private TileGridRepresenter grid;
	private TextualPanelGame gamePanel;
	private PlayerColor playerColor;

	@Override
	public void initialize(TileGrid grid, int numPlayers,
			PlayerColor clientColor) {
		this.grid = new TileGridRepresenter(grid);

		gamePanel = new TextualPanelGame();
		/* Sets gamePanel in the View. */
		ViewManager.getInstance().getFrame().setGamePanel(gamePanel);
		ViewManager.getInstance().changeMenuPanel(CarcassonneFrame.GAMEPANEL);

		String notification = String.format("Match starts with %s players.\n",
				numPlayers);
		if (clientColor != null) {
			notification += String.format("You are player %s.\n", clientColor);
		}
		gamePanel.print(notification);
	}

	@Override
	public void updateGridRepresentation() {
		gamePanel.print(grid.getRepresentation());
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
		gamePanel.print("Enter command: ");
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
