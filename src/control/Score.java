package control;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import javax.imageio.ImageIO;

import display.DancePanel;

public class Score {

	public final int MISS = 5;
	public final int ALMOST = 4;
	public final int GOOD = 3;
	public final int GREAT = 2;
	public final int PERFECT = 1;
	public final int MARVELOUS = 0;

	private int[] noteCount;
	private int baseHeight = 100;
	private int combo = 0;
	private int BASESCORE = 0;
	private int COMBOMULT = 0;
	private int MAX_SCORE = 0;
	public double NOTE_AMOUNT = 0;
	private final int FADE_TIME = 50;
	private double baseCombo = 0;
	private BufferedImage[][] description;
	private double score = 0;
	private int maxCombo;
	private String name = "COMPUTER";
	private int id;
	private int totalNotes;
	private static String[] descriptionName = { "Marvelous", "Perfect",
			"Great", "Good", "Almost", "Miss" };
	private Song song;
	private BufferedImage[] GRADE_S;
	private BufferedImage[] GRADE_A;
	private BufferedImage[] GRADE_B;
	private BufferedImage[] GRADE_C;
	private BufferedImage[] GRADE_D;
	private BufferedImage[] GRADE_F;
	private FadeImage MISS_IMAGE;
	private FadeImage ALMOST_IMAGE;
	private FadeImage GOOD_IMAGE;
	private FadeImage GREAT_IMAGE;
	private FadeImage PERFECT_IMAGE;
	private FadeImage MARVELOUS_IMAGE;
	private List<Arrow> holdList;
	private double lastUpdateTime;

	public Score(int id, Dimension scaleFactor, Song song) throws IOException {
		this.id = id;
		this.song = song;
		this.holdList = new LinkedList<Arrow>();
		if (song.getDifficulty() == 0) {
			BASESCORE = 1200000;
			COMBOMULT = 600000;
		} else if (song.getDifficulty() == 1) {
			BASESCORE = 1400000;
			COMBOMULT = 700000;
		} else if (song.getDifficulty() == 2) {
			BASESCORE = 1600000;
			COMBOMULT = 800000;
		}
		MAX_SCORE = BASESCORE + COMBOMULT;
		this.totalNotes = 0;
		noteCount = new int[MISS + 1];
		description = new BufferedImage[6][];
		for (int i = 0; i < description.length; i++) {
			description[i] = new BufferedImage[15];
		}
		BufferedImage marvelous0 = ImageIO.read(new File(Constants.MARVELOUS0));
		BufferedImage marvelous1 = ImageIO.read(new File(Constants.MARVELOUS1));
		BufferedImage marvelous2 = ImageIO.read(new File(Constants.MARVELOUS2));

		BufferedImage perfect0 = ImageIO.read(new File(Constants.PERFECT0));
		BufferedImage perfect1 = ImageIO.read(new File(Constants.PERFECT1));
		BufferedImage perfect2 = ImageIO.read(new File(Constants.PERFECT2));

		BufferedImage great0 = ImageIO.read(new File(Constants.GREAT0));
		BufferedImage great1 = ImageIO.read(new File(Constants.GREAT1));
		BufferedImage great2 = ImageIO.read(new File(Constants.GREAT2));

		BufferedImage good0 = ImageIO.read(new File(Constants.GOOD0));
		BufferedImage good1 = ImageIO.read(new File(Constants.GOOD1));
		BufferedImage good2 = ImageIO.read(new File(Constants.GOOD2));

		BufferedImage almost0 = ImageIO.read(new File(Constants.ALMOST0));
		BufferedImage almost1 = ImageIO.read(new File(Constants.ALMOST1));
		BufferedImage almost2 = ImageIO.read(new File(Constants.ALMOST2));

		BufferedImage miss0 = ImageIO.read(new File(Constants.MISS0));
		BufferedImage miss1 = ImageIO.read(new File(Constants.MISS1));
		BufferedImage miss2 = ImageIO.read(new File(Constants.MISS2));

		GRADE_S = new BufferedImage[3];
		GRADE_S[0] = ImageIO.read(new File(Constants.GRADE_A_plus));
		GRADE_S[1] = ImageIO.read(new File(Constants.GRADE_A));
		GRADE_S[2] = ImageIO.read(new File(Constants.GRADE_A_minus));

		GRADE_A = new BufferedImage[3];
		GRADE_A[0] = ImageIO.read(new File(Constants.GRADE_A_plus));
		GRADE_A[1] = ImageIO.read(new File(Constants.GRADE_A));
		GRADE_A[2] = ImageIO.read(new File(Constants.GRADE_A_minus));

		GRADE_B = new BufferedImage[3];
		GRADE_B[0] = ImageIO.read(new File(Constants.GRADE_B_plus));
		GRADE_B[1] = ImageIO.read(new File(Constants.GRADE_B));
		GRADE_B[2] = ImageIO.read(new File(Constants.GRADE_B_minus));

		GRADE_C = new BufferedImage[3];
		GRADE_C[0] = ImageIO.read(new File(Constants.GRADE_C_plus));
		GRADE_C[1] = ImageIO.read(new File(Constants.GRADE_C));
		GRADE_C[2] = ImageIO.read(new File(Constants.GRADE_C_minus));

		GRADE_D = new BufferedImage[3];
		GRADE_D[0] = ImageIO.read(new File(Constants.GRADE_D_plus));
		GRADE_D[1] = ImageIO.read(new File(Constants.GRADE_D));
		GRADE_D[2] = ImageIO.read(new File(Constants.GRADE_D_minus));

		GRADE_F = new BufferedImage[1];
		GRADE_F[0] = ImageIO.read(new File(Constants.GRADE_F));

		description[MARVELOUS][0] = marvelous0;
		description[MARVELOUS][1] = marvelous1;
		description[MARVELOUS][2] = marvelous2;

		description[PERFECT][0] = perfect0;
		description[PERFECT][1] = perfect1;
		description[PERFECT][2] = perfect2;

		description[GREAT][0] = great0;
		description[GREAT][1] = great1;
		description[GREAT][2] = great2;

		description[GOOD][0] = good0;
		description[GOOD][1] = good1;
		description[GOOD][2] = good2;

		description[ALMOST][0] = almost0;
		description[ALMOST][1] = almost1;
		description[ALMOST][2] = almost2;

		description[MISS][0] = miss0;
		description[MISS][1] = miss1;
		description[MISS][2] = miss2;

		MISS_IMAGE = new FadeImage(description[MISS], FADE_TIME);
		ALMOST_IMAGE = new FadeImage(description[ALMOST], FADE_TIME);
		GOOD_IMAGE = new FadeImage(description[GOOD], FADE_TIME);
		GREAT_IMAGE = new FadeImage(description[GREAT], FADE_TIME);
		PERFECT_IMAGE = new FadeImage(description[PERFECT], FADE_TIME);
		MARVELOUS_IMAGE = new FadeImage(description[MARVELOUS], FADE_TIME);
		// scale description images
		for (int i = 0; i < description.length; i++) {
			for (int j = 0; j < 3; j++) {
				BufferedImage img = description[i][j];
				int height = (int) (baseHeight * scaleFactor.getHeight());
				int width = (int) ((height * 1.0 / img.getHeight()) * img
						.getWidth());
				Image scaledImage = img.getScaledInstance(width, height,
						BufferedImage.SCALE_SMOOTH);
				BufferedImage newImg = new BufferedImage(width, height,
						BufferedImage.TYPE_INT_ARGB);
				Graphics g = newImg.getGraphics();
				g.drawImage(scaledImage, 0, 0, width, height, null);
				g.dispose();
				description[i][j] = newImg;
			}
		}
		// fill other spaces
		for (int i = 0; i < description.length; i++) {
			BufferedImage base = description[i][2];
			for (int j = 3; j < description[i].length; j++) {
				description[i][j] = base;
			}
		}

		// scale grade images
		BufferedImage[][] grades = { GRADE_S, GRADE_A, GRADE_B, GRADE_C,
				GRADE_D, GRADE_F };
		for (int i = 0; i < grades.length; i++) {
			for (int j = 0; j < grades[i].length; j++) {
				BufferedImage img = grades[i][j];
				int height = (int) (baseHeight * 1.2 * scaleFactor.getHeight());
				int width = (int) ((height * 1.0 / img.getHeight()) * img
						.getWidth());
				Image scaledImage = img.getScaledInstance(width, height,
						BufferedImage.SCALE_SMOOTH);
				BufferedImage newImg = new BufferedImage(width, height,
						BufferedImage.TYPE_INT_ARGB);
				Graphics g = newImg.getGraphics();
				g.drawImage(scaledImage, 0, 0, width, height, null);
				g.dispose();
				grades[i][j] = newImg;
			}
		}

		double thirdNote = song.getNoteCount() / 2.0; // half
		double lowerThird = (thirdNote * 1 * (thirdNote + 1) / 2);
		double biggerTwoThirds = (thirdNote * 1 * thirdNote);
		double totalOfThirds = lowerThird + biggerTwoThirds;
		baseCombo = COMBOMULT / totalOfThirds;

		NOTE_AMOUNT = BASESCORE
				/ (song.getNoteCount() + song.getTotalHoldDuration() / 16.0);
	}

	public Score() {

	}

	public BufferedImage getGrade() {
		if (this.score > MAX_SCORE * .80) {
			return GRADE_S[0];// S+
		} else if (this.score > MAX_SCORE * .75) {
			return GRADE_S[1];// S
		} else if (this.score > MAX_SCORE * .70) {
			return GRADE_S[2];// S-
		} else if (this.score > MAX_SCORE * .67) {
			return GRADE_A[0];// A+
		} else if (this.score > MAX_SCORE * .63) {
			return GRADE_A[1];// A
		} else if (this.score > MAX_SCORE * .59) {
			return GRADE_A[2];// A-
		} else if (this.score > MAX_SCORE * .55) {
			return GRADE_B[0];// B+
		} else if (this.score > MAX_SCORE * .50) {
			return GRADE_B[1];// B
		} else if (this.score > MAX_SCORE * .46) {
			return GRADE_B[2];// B-
		} else if (this.score > MAX_SCORE * .42) {
			return GRADE_B[0];// C+
		} else if (this.score > MAX_SCORE * .38) {
			return GRADE_B[1];// C
		} else if (this.score > MAX_SCORE * .33) {
			return GRADE_B[2];// C-
		} else if (this.score > MAX_SCORE * .30) {
			return GRADE_D[0];// D+
		} else if (this.score > MAX_SCORE * .26) {
			return GRADE_D[1];// D
		} else if (this.score > MAX_SCORE * .22) {
			return GRADE_D[2];// D-
		} else {
			return GRADE_F[0];// F
		}
	}
	public FadeImage missNote() {
		return addNote(100);
	}

	/**
	 * Updates score's internal clock, which is how it adds the hold scores to
	 * the score amount
	 * 
	 * @param time
	 *            the current time in seconds
	 */
	public void update(double time) {
		List<Arrow> removeList = new LinkedList<Arrow>();
		for (int index = 0; index < holdList.size(); index++) {
			Arrow arrow = holdList.get(index);
			if(arrow.getActive()){
				double difference = time - lastUpdateTime;
				if (time > arrow.getTime()/1000.0 + arrow.getHold()/1000.0) {
					removeList.add(arrow);
					difference -= time
							- ((arrow.getTime() + arrow.getHold()) / 1000.0);
				}
				this.score += difference * NOTE_AMOUNT;
			} else{
				removeList.add(arrow);
			}
		}
		for (Arrow arrow : removeList) {
			holdList.remove(arrow);
		}
		this.lastUpdateTime = time;
	}

	/**
	 * 
	 * @param arrow
	 * @param time
	 * @param pressRange
	 *            the amount of time in milliseconds away from a note's goal
	 *            time a press can occur for a note to still
	 *            count in this calculation. Scores will be based on a
	 *            percentage of this press range value to see how close to 0 the
	 *            actual press was from the goal press time of the arrow
	 * @return
	 */
	public FadeImage addNote(Arrow arrow, double time, int pressRange) {
		double difference = (time - (arrow.getTime()));
		difference = Math.abs(difference);
		double percentageOfPressRange = difference / pressRange;
		if (arrow.getHold() > 0) {
			this.holdList.add(arrow);
		}
		return addNote(percentageOfPressRange);
	}

	public void releaseNote(Arrow arrow) {
		
	}

	/**
	 * 
	 * @param percentage
	 *            the percentage of difference there was between the actual
	 *            press and when the note was supposed to be pressed. 1.00 means
	 *            the delay was the maximum amount of allowable delay so the
	 *            note press was very far off. 0.00 means the delay was
	 *            nonexistent, meaning the press was perfectly timed
	 * @return
	 */
	public FadeImage addNote(double percentage) {
		combo++;
		this.totalNotes++;
		double cMult = 1;
		if (percentage > 1) {
			combo = 0;
			cMult = 0;
			noteCount[MISS]++;
		} else if (percentage > .55) {
			cMult = .3;
			noteCount[ALMOST]++;
		} else if (percentage > .35) {
			cMult = .5;
			noteCount[GOOD]++;
		} else if (percentage > .15) {
			cMult = .7;
			noteCount[GREAT]++;
		} else if (percentage > .07) {
			cMult = .85;
			noteCount[PERFECT]++;
		} else {
			cMult = 1.0;
			noteCount[MARVELOUS]++;
		}
		int newCombo = Math.min(combo, song.getNoteCount() / 2);
		score += cMult * NOTE_AMOUNT + newCombo * baseCombo;
		if (combo > maxCombo) {
			maxCombo = combo;
		}
		return getDescription(percentage);
	}

	public FadeImage getDescription(double difference) {
		if (difference > 1) {
			return MISS_IMAGE;
		} else if (difference > .55) {
			return ALMOST_IMAGE;
		} else if (difference > .35) {
			return GOOD_IMAGE;
		} else if (difference > .15) {
			return GREAT_IMAGE;
		} else if (difference > .07) {
			return PERFECT_IMAGE;
		} else {
			return MARVELOUS_IMAGE;
		}
	}

	public int getScore() {
		return (int) score;
	}

	public int getCombo() {
		return combo;
	}

	public String getMaxCombo() {
		return String.format("%04d", maxCombo);
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getSongId() {
		return this.id;
	}

	public String getSongName() {
		return song.getSongName();
	}

	public String[] getDescriptionNames() {
		return descriptionName;
	}

	public int[] getDescriptionCount() {
		return noteCount;
	}

	public static Score[][] getHighScore() throws IOException {
		File file = new File(Constants.HIGH_SCORES);
		Scanner scanner = new Scanner(new FileReader(file));
		scanner.useDelimiter(",");
		LinkedList<Score[]> scoreList = new LinkedList<Score[]>();
		while (scanner.hasNext()) {
			Score[] tmp = new Score[8];
			for (int i = 0; i < 8; i++) {
				Score score = new Score();
				score.setName(scanner.next().trim());
				score.setScore(Integer.parseInt(scanner.next().trim()));
				tmp[i] = score;
			}
			scoreList.add(tmp);
		}
		Score[][] ans = new Score[scoreList.size()][];
		for (int i = 0; i < scoreList.size(); i++) {
			ans[i] = scoreList.get(i);
		}
		scanner.close();
		return ans;

	}

	public String getFormatedScore() {
		return String.format("%09d", (int) score);
	}

	public void saveHighScore() throws IOException {
		Score[][] highScore = getHighScore();
		int i = highScore[id].length - 1;
		while (i >= 0 && score > highScore[id][i].getScore()) {
			i--;
		}
		i++;
		if (i < highScore[id].length) {
			Score current = this;
			for (int j = i; j < highScore[id].length; j++) {
				Score old = highScore[id][j];
				highScore[id][j] = current;
				current = old;
			}
		}
		String ans = "";
		for (int line = 0; line < highScore.length; line++) {
			String tmp = "";
			for (int index = 0; index < highScore[line].length; index++) {
				tmp += highScore[line][index].getName() + ", "
						+ highScore[line][index].getScore() + ", ";
			}
			ans += tmp;
			if (line != highScore.length - 1) {
				ans += "\n";
			}
		}
		ans = ans.substring(0, ans.length() - 2);
		File file = new File(Constants.HIGH_SCORES);
		FileWriter writer = new FileWriter(file);
		writer.write(ans);
		writer.close();
		saveName();
	}

	public void saveName() throws IOException {
		String[] names = loadNames();
		for (int i = 0; i < names.length; i++) {
			if (names[i].equals(name)) {
				return;
			}
		}
		for (int i = names.length - 1; i > 0; i--) {
			names[i] = names[i - 1];
		}
		names[0] = name;
		String ans = "";
		for (int i = 0; i < names.length; i++) {
			ans += names[i] + ",";
		}
		ans = ans.substring(0, ans.length() - 1);
		File file = new File(Constants.SCORE_NAMES);
		FileWriter writer = new FileWriter(file);
		writer.write(ans);
		writer.close();
	}

	public static String[] loadNames() throws IOException {
		File file = new File(Constants.SCORE_NAMES);
		Scanner scanner = new Scanner(new FileReader(file));
		scanner.useDelimiter(",");
		int count = 0;
		while (scanner.hasNext()) {
			scanner.next();
			count++;
		}
		String[] ans = new String[count];
		scanner.close();
		scanner = new Scanner(new FileReader(file));
		scanner.useDelimiter(",");
		for (int i = 0; i < ans.length; i++) {
			ans[i] = scanner.next();
		}
		scanner.close();
		return ans;
	}
}
