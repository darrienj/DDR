package display;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import control.Constants;

public class SongSelectorPanel extends JPanel {

	Dimension scaleFactor;
	public static final int SONGS_ON_SCREEN = 15;
	public final int TOTAL_SONGS_ON_SCREEN;
	public final int SMALLEST_SELECT = SONGS_ON_SCREEN / 2 + 1;
	public final int BIGGEST_SELECT;
	private int selection = SMALLEST_SELECT;
	private String[][] songList;
	private SongPanel[] songPanel;
	private BufferedImage highlight;
	private BufferedImage img;
	private DancePanelInput dancePanelInput;

	public SongSelectorPanel(Dimension scaleFactor, String[][] songList,
			SelectorBackgroundPanel sbp) {
		dancePanelInput = new DancePanelInput(sbp);
		this.scaleFactor = scaleFactor;
		this.songList = songList;
		this.setLayout(new GridBagLayout());
		this.setOpaque(false);
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		int borderWidth = 3;
		TOTAL_SONGS_ON_SCREEN = songList.length + SONGS_ON_SCREEN;
		BIGGEST_SELECT = this.songList.length + SMALLEST_SELECT - 1;
		try {
			img = ImageIO.read(new File(Constants.SELECT_GRADIENT));
			Image tmp = img.getScaledInstance(
					(int) (1280 * scaleFactor.getWidth() * .4 * .9) - 2
							* borderWidth, (int) (800 * scaleFactor.getHeight()
							* (1.0 / SONGS_ON_SCREEN) - 2 * borderWidth),
					BufferedImage.TYPE_INT_RGB);
			img = new BufferedImage(tmp.getWidth(null), tmp.getHeight(null),
					BufferedImage.TYPE_INT_RGB);
			Graphics g = img.getGraphics();
			g.drawImage(tmp, 0, 0, img.getWidth(), img.getHeight(), null);

			highlight = ImageIO.read(new File(
					Constants.SELECT_GRADIENT_HIGHLIGHTED));
			tmp = highlight.getScaledInstance(
					(int) (1280 * scaleFactor.getWidth() * .4 * .9) - 2
							* borderWidth, (int) (800 * scaleFactor.getHeight()
							* (1.0 / SONGS_ON_SCREEN) - 2 * borderWidth),
					BufferedImage.TYPE_INT_RGB);
			highlight = new BufferedImage(tmp.getWidth(null),
					tmp.getHeight(null), BufferedImage.TYPE_INT_RGB);
			g = highlight.getGraphics();
			g.drawImage(tmp, 0, 0, highlight.getWidth(), highlight.getHeight(),
					null);

		} catch (Exception e) {
			img = null;
			highlight = null;
		}
		int pageHeight = (int) ((800 * scaleFactor.getHeight()
				* (1.0 / SONGS_ON_SCREEN) - 4) * (songList.length + SONGS_ON_SCREEN)); // -4
																						// to
																						// account
																						// for
																						// the
																						// border
																						// on
																						// the
																						// border
		this.setPreferredSize(new Dimension((int) (1280 * scaleFactor
				.getWidth() * .4 * .9), pageHeight));
		this.songPanel = new SongPanel[songList.length + SONGS_ON_SCREEN];
		for (int index = 0; index < songList.length + SONGS_ON_SCREEN; index++) {
			SongPanel panel;
			int i = (index) % songList.length;
			if (i >= songList.length) {
				continue;
			} else {
				panel = new SongPanel(Math.min(scaleFactor.getWidth(),
						scaleFactor.getHeight()), img, borderWidth,
						songList[i][0], false, songList[i], i % songList.length);
			}
			this.songPanel[index] = panel;
			c.gridwidth = 1;
			c.weightx = .05;
			c.weighty = 1.0;
			c.gridx = 0;
			c.gridy = index;
			this.add(new MarginPanel(), c);
			c.gridx = 1;
			c.weightx = .9;
			this.add(panel, c);
			c.gridx = 2;
			c.weightx = .05;
			this.add(new MarginPanel(), c);
		}
		this.selection = songList.length;
		this.addKeyListener(dancePanelInput);
		this.setFocusable(true);
	}

	public int getSelection() {
		return this.selection;
	}

	public String[] getSong(int finalSelection) {
		if (finalSelection > 0 && finalSelection < songPanel.length) {
			return this.songPanel[finalSelection].getData();
		} else {
			return null;
		}
	}

	public int[] getSongDifficulty() {
		return songPanel[this.selection].getDifficultyData();
	}

	public void setDifficulty(int difficulty) {
		songPanel[this.selection].setDifficulty(difficulty);
	}

	public int getDifficulty() {
		return songPanel[this.selection].getDifficulty();
	}

	public void increaseDifficulty() {
		this.songPanel[this.selection].increaseDifficulty();
	}

	public void decreaseDifficulty() {
		this.songPanel[this.selection].decreaseDifficulty();
	}

	public int setSelection(int id) {
		this.songPanel[this.selection].updateBackground(img, false);
		this.requestFocus();
		this.selection = id;
		this.selection = this.selection < SMALLEST_SELECT ? this.songList.length
				+ this.selection
				: this.selection;
		this.songPanel[this.selection].updateBackground(highlight, true);
		return this.selection;
	}

	public int update(int increment) {
		dancePanelInput.initialize();
		this.requestFocus();
		if (increment != 0) {
			this.songPanel[this.selection].updateBackground(img, false);
			this.selection += increment;
			if (this.selection > this.songList.length + SMALLEST_SELECT - 1) {
				this.selection = SMALLEST_SELECT;
			}
			setSelection(this.selection);
		}
		return this.selection;
	}

	// public int update(int increment){
	// this.requestFocus();
	// if(increment != 0){
	// this.songPanel[this.selection].updateBackground(img,false);
	// this.selection += increment;
	// if (this.selection > this.songList.length + SMALLEST_SELECT -1){
	// this.selection = SMALLEST_SELECT;
	// }
	// this.selection = this.selection < SMALLEST_SELECT ? this.songList.length
	// + this.selection : this.selection;
	// this.songPanel[this.selection].updateBackground(highlight,true);
	// }
	// return this.selection;
	// }
	/**
	 * returns the current selection's id in the songList
	 * 
	 * @return
	 */
	public int getSelectionId() {
		return this.selection % songList.length;
	}

	private class MarginPanel extends JPanel {

		public MarginPanel() {
			this.setOpaque(false);
		}
	}
}
