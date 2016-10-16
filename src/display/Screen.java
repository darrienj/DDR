package display;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;

import control.Arrow;
import control.Score;
import control.Song;
import control.Constants;

public class Screen {

	ScreenFrame frame;
	private String[] songSelection;

	public Screen() {
		this.frame = new ScreenFrame();
	}

	public void selectSong(int oldSelection) throws IOException {
		File file = new File(Constants.FILE_BASE + "DDR_Files/SongList.txt");
		Scanner scanner = new Scanner(file);
		scanner.useDelimiter(",");
		int count = 0;
		while (scanner.hasNextLine()) {
			scanner.nextLine();
			count++;
		}
		String[][] list = new String[count][]; // Name, musicname, easy, medium,
												// hard
		scanner.close();
		scanner = new Scanner(file);
		scanner.useDelimiter(",");
		for (int i = 0; i < list.length; i++) {
			list[i] = new String[5]; // Name, musicname, easy, medium, hard
			for (int j = 0; j < list[i].length; j++) {
				list[i][j] = scanner.next().replace("\n", "").replace("\r", "")
						.trim();
			}
		}
		scanner.close();

		BufferedImage background = ImageIO.read(new File(
				Constants.SELECT_BACKGROUND));
		this.frame.selectSong(background, list, oldSelection);
	}

	public String[] getSelection() {
		return this.frame.getSongSelection();
	}

	/**
	 * Starts the display for the specified song
	 * 
	 * @param song
	 * @param danceChartBuilder
	 *            a partially complete danceChart builder that only needs the
	 *            setHeightOfScreen and rawImage set
	 */
	public void playSong(Song song, DanceChartBuilder danceChartBuilder) {
		try {
			BufferedImage background = ImageIO.read(new File(
					Constants.DDR_BACKGROUND));
			Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
			Dimension scaleFactor = new Dimension();
			scaleFactor.setSize(dimension.getWidth() / 1280,
					dimension.getHeight() / 1280);
			Score score = new Score(song.getId(), scaleFactor, song);
			this.frame.playSong(background, getReceiveArrow(),
					getDanceChart(danceChartBuilder), score);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void updateSong(double time) {
		frame.updateSong(time);
	}

	public void updateSelection(double time) {
		frame.updateSelection(time);
	}

	public Score endSong() {
		return frame.endSong();
	}

	public void showScore(Score score) throws IOException {
		frame.showScore(score);
	}

	public void updateScore() {
		frame.updateScore();
	}

	public boolean submittedScore() {
		return frame.submittedScore();
	}

	private DanceChart getDanceChart(DanceChartBuilder danceChartBuilder)
			throws IOException {
		BufferedImage[][] noteArrows = new BufferedImage[4][];
		for (int i = 0; i < noteArrows.length; i++) {
			noteArrows[i] = new BufferedImage[2];
		}
		noteArrows[0][0] = ImageIO.read(new File(Constants.LEFT_QUARTER_ARROW));
		noteArrows[0][1] = ImageIO.read(new File(
				Constants.LEFT_QUARTER_HOLD_ARROW));
		noteArrows[1][0] = ImageIO
				.read(new File(Constants.RIGHT_QUARTER_ARROW));
		noteArrows[1][1] = ImageIO.read(new File(
				Constants.RIGHT_QUARTER_HOLD_ARROW));
		noteArrows[2][0] = ImageIO.read(new File(Constants.UP_QUARTER_ARROW));
		noteArrows[2][1] = ImageIO.read(new File(
				Constants.UP_QUARTER_HOLD_ARROW));
		noteArrows[3][0] = ImageIO.read(new File(Constants.DOWN_QUARTER_ARROW));
		noteArrows[3][1] = ImageIO.read(new File(
				Constants.DOWN_QUARTER_HOLD_ARROW));
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		danceChartBuilder.setHeightOfScreen((int) (dimension.getHeight()));
		danceChartBuilder.setRawImage(noteArrows);
		return danceChartBuilder.build();
	}

	private ReceiveArrow getReceiveArrow() throws IOException {
		BufferedImage[][] receiveArrows = new BufferedImage[4][];
		for (int i = 0; i < receiveArrows.length; i++) {
			receiveArrows[i] = new BufferedImage[7];
		}

		receiveArrows[Arrow.LEFT][0] = ImageIO.read(new File(
				Constants.RECEIVE_LEFT_ARROW));
		receiveArrows[Arrow.DOWN][0] = ImageIO.read(new File(
				Constants.RECEIVE_DOWN_ARROW));
		receiveArrows[Arrow.UP][0] = ImageIO.read(new File(
				Constants.RECEIVE_UP_ARROW));
		receiveArrows[Arrow.RIGHT][0] = ImageIO.read(new File(
				Constants.RECEIVE_RIGHT_ARROW));

		receiveArrows[Arrow.LEFT][1] = ImageIO.read(new File(
				Constants.RECEIVE_LEFT_ARROW_PRESSED_5));
		receiveArrows[Arrow.DOWN][1] = ImageIO.read(new File(
				Constants.RECEIVE_DOWN_ARROW_PRESSED_5));
		receiveArrows[Arrow.UP][1] = ImageIO.read(new File(
				Constants.RECEIVE_UP_ARROW_PRESSED_5));
		receiveArrows[Arrow.RIGHT][1] = ImageIO.read(new File(
				Constants.RECEIVE_RIGHT_ARROW_PRESSED_5));

		receiveArrows[Arrow.LEFT][2] = ImageIO.read(new File(
				Constants.RECEIVE_LEFT_ARROW_PRESSED_4));
		receiveArrows[Arrow.DOWN][2] = ImageIO.read(new File(
				Constants.RECEIVE_DOWN_ARROW_PRESSED_4));
		receiveArrows[Arrow.UP][2] = ImageIO.read(new File(
				Constants.RECEIVE_UP_ARROW_PRESSED_4));
		receiveArrows[Arrow.RIGHT][2] = ImageIO.read(new File(
				Constants.RECEIVE_RIGHT_ARROW_PRESSED_4));

		receiveArrows[Arrow.LEFT][3] = ImageIO.read(new File(
				Constants.RECEIVE_LEFT_ARROW_PRESSED_3));
		receiveArrows[Arrow.DOWN][3] = ImageIO.read(new File(
				Constants.RECEIVE_DOWN_ARROW_PRESSED_3));
		receiveArrows[Arrow.UP][3] = ImageIO.read(new File(
				Constants.RECEIVE_UP_ARROW_PRESSED_3));
		receiveArrows[Arrow.RIGHT][3] = ImageIO.read(new File(
				Constants.RECEIVE_RIGHT_ARROW_PRESSED_3));

		receiveArrows[Arrow.LEFT][4] = ImageIO.read(new File(
				Constants.RECEIVE_LEFT_ARROW_PRESSED_2));
		receiveArrows[Arrow.DOWN][4] = ImageIO.read(new File(
				Constants.RECEIVE_DOWN_ARROW_PRESSED_2));
		receiveArrows[Arrow.UP][4] = ImageIO.read(new File(
				Constants.RECEIVE_UP_ARROW_PRESSED_2));
		receiveArrows[Arrow.RIGHT][4] = ImageIO.read(new File(
				Constants.RECEIVE_RIGHT_ARROW_PRESSED_2));

		receiveArrows[Arrow.LEFT][5] = ImageIO.read(new File(
				Constants.RECEIVE_LEFT_ARROW_PRESSED_1));
		receiveArrows[Arrow.DOWN][5] = ImageIO.read(new File(
				Constants.RECEIVE_DOWN_ARROW_PRESSED_1));
		receiveArrows[Arrow.UP][5] = ImageIO.read(new File(
				Constants.RECEIVE_UP_ARROW_PRESSED_1));
		receiveArrows[Arrow.RIGHT][5] = ImageIO.read(new File(
				Constants.RECEIVE_RIGHT_ARROW_PRESSED_1));

		receiveArrows[Arrow.LEFT][6] = ImageIO.read(new File(
				Constants.RECEIVE_LEFT_ARROW_PRESSED_0));
		receiveArrows[Arrow.DOWN][6] = ImageIO.read(new File(
				Constants.RECEIVE_DOWN_ARROW_PRESSED_0));
		receiveArrows[Arrow.UP][6] = ImageIO.read(new File(
				Constants.RECEIVE_UP_ARROW_PRESSED_0));
		receiveArrows[Arrow.RIGHT][6] = ImageIO.read(new File(
				Constants.RECEIVE_RIGHT_ARROW_PRESSED_0));

		ReceiveArrow receiveArrow = new ReceiveArrow(receiveArrows);
		return receiveArrow;
	}
}
