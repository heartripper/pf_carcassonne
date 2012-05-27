package it.polimi.dei.provafinale.carcassonne.view.viewInterface;

import it.polimi.dei.provafinale.carcassonne.model.gamelogic.player.PlayerColor;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.tile.TileGrid;
import it.polimi.dei.provafinale.carcassonne.view.CarcassonneFrame;
import it.polimi.dei.provafinale.carcassonne.view.ViewManager;
import it.polimi.dei.provafinale.carcassonne.view.game.GamePanel;
import it.polimi.dei.provafinale.carcassonne.view.game.PlayerPanel;

public class GUIViewInterface implements ViewInterface{

	private GamePanel gamePanel;
	
	@Override
	public void initialize(TileGrid grid, int numPlayers,
			PlayerColor clientColor) {
		
		gamePanel = new GamePanel();

		// Set game panel
		gamePanel.setTilesPanelGrid(grid);
		gamePanel.createPlayerPanels(numPlayers);
		if(clientColor != null){
			int index = PlayerColor.indexOf(clientColor);
			gamePanel.getPlayerPanels()[index].setClientPlayer();
		}
		gamePanel.setUIActive(false);

		// Append game panel to frame
		CarcassonneFrame frame = ViewManager.getInstance().getFrame();
		frame.setGamePanel(gamePanel);
		frame.changeMainPanel(CarcassonneFrame.GAMEPANEL);
		gamePanel.updateTileGridPanel();
	}

	@Override
	public void updateGridRepresentation() {
		gamePanel.updateTileGridPanel();
	}

	@Override
	public void updateCurrentTile(String rep) {
		gamePanel.setCurrentTile(rep);
	}

	@Override
	public void updateScore(String msg) {
		String[] scores = msg.split(",");
		PlayerPanel[] panels = gamePanel.getPlayerPanels();
		for(String s : scores){
			String[] split = s.split("=");
			PlayerColor color = PlayerColor.valueOf(split[0].trim());
			int colorIndex = PlayerColor.indexOf(color);
			int score = Integer.parseInt(split[1].trim());
			panels[colorIndex].setScore(score);
		}
	}

	@Override
	public void showNotify(String msg) {
		gamePanel.setMessageText(msg);
	}

	@Override
	public void setUIActive(boolean enabled) {
		gamePanel.setUIActive(enabled);
	}

	@Override
	public void setCurrentPlayer(PlayerColor color) {
		// TODO Auto-generated method stub
		
	}

}
