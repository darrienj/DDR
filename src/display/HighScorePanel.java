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
import javax.swing.border.LineBorder;

import control.Constants;
import control.Score;

public class HighScorePanel extends JPanel{

	Dimension scaleFactor;
	BufferedImage background;
	Score[][] scoreList;
	private int songId;
	public HighScorePanel(Dimension scaleFactor){
		this.scaleFactor = scaleFactor;
		this.setOpaque(false);
		try {
			background = ImageIO.read(new File(Constants.HIGH_SCORE_BACKGROUND));
			Image tmp = background.getScaledInstance((int)(scaleFactor.getWidth()*.5*1280) -24, (int)(scaleFactor.getHeight()*.5*800) -24, BufferedImage.SCALE_SMOOTH);
			background = new BufferedImage(tmp.getWidth(null),tmp.getHeight(null),BufferedImage.TYPE_INT_ARGB);
			Graphics g = background.getGraphics();
			g.drawImage(tmp, 0, 0, tmp.getWidth(null), tmp.getHeight(null),null);
			g.dispose();
			
			scoreList = Score.getHighScore();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void update(int index){
		this.songId = index;
		this.repaint();
	}
	@Override
	protected void paintComponent(Graphics g) {
		
		if(background != null){
			g.drawImage(background, 0, 0, null);
		}
		int fontSize = (int)(40*scaleFactor.getHeight());
		Font f = new Font("Monaco",Font.PLAIN,fontSize);
		g.setFont(f);
		g.setColor(Color.YELLOW);
		int fontWidth = g.getFontMetrics().stringWidth("HIGH SCORES");
		g.drawString("HIGH SCORES", getWidth()/2- fontWidth/2, fontSize);
		
		
		fontSize = (int)(25*scaleFactor.getHeight());
		g.setFont(new Font("Monaco",Font.PLAIN,fontSize));
		for(int i = 0;i < scoreList[songId].length;i++){
			g.setColor(new Color(255,255,255));
			int x = (int)(getWidth()*.15);
			int y = (int)(getHeight()*.1*i + getHeight()*.15 + fontSize);
			g.drawString(scoreList[songId][i].getName(), x, y);
			
			x = (int)(getWidth()*.55);
			y = (int)(getHeight()*.1*i + getHeight()*.15 + fontSize);
			g.drawString(scoreList[songId][i].getFormatedScore(), x, y);
		}
	}
}
