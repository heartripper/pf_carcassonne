package it.polimi.dei.provafinale.carcassonne.view.game;

import it.polimi.dei.provafinale.carcassonne.view.CarcassonneFrame;
import it.polimi.dei.provafinale.carcassonne.view.ViewManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.filechooser.FileFilter;
import javax.swing.JFileChooser;

/**
 * Class ScreenShotListener implements an ActionListener in order to handle
 * user's request of take a screenshot during the match.
 * 
 */
public class ScreenShotListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {

		CarcassonneFrame frame = ViewManager.getInstance().getFrame();
		GamePanel panel = frame.getGamePanel();
		BufferedImage screenshot;

		if (!(panel instanceof SwingGamePanel)) {
			return;
		}

		SwingGamePanel sPanel = (SwingGamePanel) panel;

		JFileChooser fileChoose = new JFileChooser();
		fileChoose.setFileFilter(new PngFilter());
		fileChoose.setMultiSelectionEnabled(false);
		int r = fileChoose.showSaveDialog(frame);
		if (r == JFileChooser.APPROVE_OPTION) {
			screenshot = sPanel.takeScreenshot();
			File f = fileChoose.getSelectedFile();
			try {
				ImageIO.write(screenshot, "PNG", f);
			} catch (IOException ioe) {
				sPanel.showNotify("Error opening file");
			}
		}
	}

	/**
	 * Class PngFilter extends a FileFilter in order to filtrate the .png files
	 * in file chooser.
	 * 
	 */
	private static class PngFilter extends FileFilter {

		@Override
		public boolean accept(File pathname) {
			if (pathname.isDirectory()) {
				return true;
			} else if (pathname.getName().toLowerCase().endsWith("png")) {
				return true;
			} else {
				return false;
			}

		}

		@Override
		public String getDescription() {
			return "Png Image";
		}

	}
}
