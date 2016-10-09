package display;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JFrame;

import control.*;

public class ScreenFrame {

	private BackgroundPanel backgroundPanel;
	private JFrame frame;
	private Dimension screenSize;
	private SelectorBackgroundPanel selectorPanel;
	private SongHighScorePanel scorePanel;
	
	public ScreenFrame(){
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		int width = (int)(screenSize.getWidth());
		int height = (int)(screenSize.getHeight());
		frame.setSize(width, height);
	}
	public void playSong(BufferedImage background,ReceiveArrow receiveArrow,DanceChart danceChart,Score score){
		dropAllPanels();
		this.backgroundPanel = new BackgroundPanel(screenSize,background,receiveArrow,danceChart,score);	
		frame.add(this.backgroundPanel);	
		frame.setVisible(true);
	}
	public void updateSong(double time){
		this.backgroundPanel.updateSong(time);
	}
	public Score endSong(){
		if(frame != null && this.backgroundPanel != null){
			Score score = backgroundPanel.getScore();
			dropAllPanels();
			return score;
		} else{
			return null;
		}
	}
	public void updateSelection(double time){
		this.selectorPanel.update(time);
	}
	public void selectSong(BufferedImage background, String[][] songList,int oldSelection){ //name,filename,easy,medium,hard
		dropAllPanels();
		this.selectorPanel = new SelectorBackgroundPanel(screenSize,background,songList,oldSelection);
		frame.add(this.selectorPanel);
		frame.setVisible(true);
	}
	public String[] getSongSelection(){
		return selectorPanel.getSongSelection();
	}
	public void showScore(Score score) throws IOException{
		dropAllPanels();
		this.scorePanel = new SongHighScorePanel(screenSize,score,Score.getHighScore()[score.getSongId()]);
		frame.add(this.scorePanel);
		frame.setVisible(true);
	}
	public void updateScore(){
		scorePanel.update();
	}
	public boolean submittedScore(){
		if(this.scorePanel != null){
			return this.scorePanel.submittedScore();
		} else{
			return true;
		}
	}
	private void dropAllPanels(){
		if(backgroundPanel != null)
			frame.remove(backgroundPanel);
		if(selectorPanel != null)
			frame.remove(selectorPanel);
		this.selectorPanel = null;
		this.backgroundPanel = null;
	}
}
