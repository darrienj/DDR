package display;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import control.Constants;

public class DifficultyPanel extends JPanel {

	private int[] difficultyList;
	private Color[] difficultyColor;
	private BufferedImage[] difficultyImage;
	private BufferedImage[] difficultyBar;
	private int bpm;
	private int songLength; //in seconds;
	private BufferedImage background;
	private BufferedImage barImage;
	private Dimension scaleFactor;
	private int difficulty;
	public DifficultyPanel(Dimension scaleFactor){
		this.difficultyList = new int[3];
		this.scaleFactor = scaleFactor;
		this.difficultyImage = new BufferedImage[3];
		this.difficultyBar = new BufferedImage[3];
		this.setOpaque(false);
		try {
			background = ImageIO.read(new File(Constants.SELECT_GRADIENT));
			Image tmp = background.getScaledInstance((int)(scaleFactor.getWidth()*.5*1280) -24, (int)(scaleFactor.getHeight()*.4*800) -24, BufferedImage.SCALE_SMOOTH);
			background = new BufferedImage(tmp.getWidth(null),tmp.getHeight(null),BufferedImage.TYPE_INT_ARGB);
			Graphics g = background.getGraphics();
			g.drawImage(tmp, 0, 0, tmp.getWidth(null), tmp.getHeight(null),null);
			g.dispose();
			
			BufferedImage easy = ImageIO.read(new File(Constants.EASY_IMAGE));
			tmp = easy.getScaledInstance((int)(background.getWidth()*.15), (int)(background.getHeight()*.12), BufferedImage.SCALE_SMOOTH);
			easy = new BufferedImage(tmp.getWidth(null),tmp.getHeight(null),BufferedImage.TYPE_INT_ARGB);
			g = easy.getGraphics();
			g.drawImage(tmp, 0, 0, tmp.getWidth(null), tmp.getHeight(null),null);
			g.dispose();
			
			BufferedImage medium = ImageIO.read(new File(Constants.MEDIUM_IMAGE));
			tmp = medium.getScaledInstance((int)(background.getWidth()*.25), (int)(background.getHeight()*.12), BufferedImage.SCALE_SMOOTH);
			medium = new BufferedImage(tmp.getWidth(null),tmp.getHeight(null),BufferedImage.TYPE_INT_ARGB);
			g = medium.getGraphics();
			g.drawImage(tmp, 0, 0, tmp.getWidth(null), tmp.getHeight(null),null);
			g.dispose();
			
			BufferedImage hard = ImageIO.read(new File(Constants.HARD_IMAGE));
			tmp = hard.getScaledInstance((int)(background.getWidth()*.15), (int)(background.getHeight()*.12), BufferedImage.SCALE_SMOOTH);
			hard = new BufferedImage(tmp.getWidth(null),tmp.getHeight(null),BufferedImage.TYPE_INT_ARGB);
			g = hard.getGraphics();
			g.drawImage(tmp, 0, 0, tmp.getWidth(null), tmp.getHeight(null),null);
			g.dispose();
			
			barImage = ImageIO.read(new File(Constants.BAR_IMAGE));
			tmp = barImage.getScaledInstance((int)(background.getWidth()*.4), (int)(background.getHeight()*.1), BufferedImage.SCALE_SMOOTH);
			barImage = new BufferedImage(tmp.getWidth(null),tmp.getHeight(null),BufferedImage.TYPE_INT_ARGB);
			g = barImage.getGraphics();
			g.drawImage(tmp, 0, 0, tmp.getWidth(null), tmp.getHeight(null),null);
			g.dispose();
			
			
			BufferedImage easyBar = ImageIO.read(new File(Constants.EASY_BAR_IMAGE));
			tmp = easyBar.getScaledInstance((int)(barImage.getWidth()/5.0), (int)(background.getHeight()*.12), BufferedImage.SCALE_SMOOTH);
			easyBar = new BufferedImage(tmp.getWidth(null),tmp.getHeight(null),BufferedImage.TYPE_INT_ARGB);
			g = easyBar.getGraphics();
			g.drawImage(tmp, 0, 0, tmp.getWidth(null), tmp.getHeight(null),null);
			g.dispose();
			
			BufferedImage mediumBar = ImageIO.read(new File(Constants.MEDIUM_BAR_IMAGE));
			tmp = mediumBar.getScaledInstance((int)(barImage.getWidth()/5.0), (int)(background.getHeight()*.12), BufferedImage.SCALE_SMOOTH);
			mediumBar = new BufferedImage(tmp.getWidth(null),tmp.getHeight(null),BufferedImage.TYPE_INT_ARGB);
			g = mediumBar.getGraphics();
			g.drawImage(tmp, 0, 0, tmp.getWidth(null), tmp.getHeight(null),null);
			g.dispose();
			
			BufferedImage hardBar = ImageIO.read(new File(Constants.HARD_BAR_IMAGE));
			tmp = hardBar.getScaledInstance((int)(barImage.getWidth()/5.0), (int)(background.getHeight()*.12), BufferedImage.SCALE_SMOOTH);
			hardBar = new BufferedImage(tmp.getWidth(null),tmp.getHeight(null),BufferedImage.TYPE_INT_ARGB);
			g = hardBar.getGraphics();
			g.drawImage(tmp, 0, 0, tmp.getWidth(null), tmp.getHeight(null),null);
			g.dispose();
			
			difficultyImage[0] = easy;
			difficultyImage[1] = medium;
			difficultyImage[2] = hard;
			
			difficultyBar[0] = easyBar;
			difficultyBar[1] = mediumBar;
			difficultyBar[2] = hardBar;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		difficultyColor = new Color[3];
		difficultyColor[0] = Color.GREEN;
		difficultyColor[1] = Color.YELLOW;
		difficultyColor[2] = Color.RED;
	}
	public void update(int[] difficultyList,int bpm,int songLength,int difficultySelection){
		this.difficultyList = difficultyList;
		this.bpm = bpm;
		this.songLength = songLength;
		this.repaint();
		this.difficulty = difficultySelection;
	}
	@Override
	protected void paintComponent(Graphics g) {
		
		if(background != null){
			g.drawImage(background, 0, 0, null);
		}
		for(int i = 0;i<difficultyImage.length;i++){
			g.drawImage(difficultyImage[i],(int)(getWidth()*.05),(int)(i*.15*background.getHeight()) + 10,null);
			int fontSize = (int)(25*scaleFactor.getHeight());
			Font f = new Font("Monaco",Font.PLAIN,fontSize);
			g.setFont(f);
			g.setColor(difficultyColor[i]);
			g.drawString(difficultyList[i]+"", (int)(getWidth()*.4), (int)(i*.15*background.getHeight() + fontSize + .03*background.getHeight()) +5);
			for(int j = 0;j<difficultyList[i];j++){
				g.drawImage(difficultyBar[i],(int)(getWidth()*.5 + difficultyBar[i].getWidth()*j*.5) ,(int)(i*.15*background.getHeight()) +10,null);
			}
		}
		BufferedImage currentDifficulty = difficultyImage[difficulty];
		g.drawImage(currentDifficulty,(int)(getWidth()*.5 - currentDifficulty.getWidth()/2.0),(int)(background.getHeight()*.6) + 10,null);

		
	} 
	
}
