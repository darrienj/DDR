package display;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;

import sun.awt.X11.Screen;
import control.*;
public class DancePanel extends JPanel implements InputReceiver{

	private final static Color PURPLE = new Color(188,69,247);
	private final static Color BLUE = new Color(40,219,255);
	private Dimension scaleFactor;
	public static final int BEATS_ON_SCREEN = 60; //configurable
	public static final int MILLISECONDS_PER_BEAT = 60; 
	private static final int MILLISECONDS_ON_SCREEN = BEATS_ON_SCREEN*MILLISECONDS_PER_BEAT;
	private static final int MILLISECONDS_PRESS_RANGE = MILLISECONDS_ON_SCREEN/5;
	private ReceiveArrow receiveArrow;
	private BufferedImage topBackground; 
	private DanceChart danceChart;
	private int currentTime;
	private Score score;
	private FadeImage description;
	private List<Arrow> arrowList = new LinkedList<Arrow>();
	public DancePanel(Dimension scaleFactor,ReceiveArrow receiveArrows,DanceChart danceChart,Score score, BufferedImage background){
		super();
		this.danceChart = danceChart;
		this.scaleFactor = scaleFactor;
 		this.receiveArrow = receiveArrows;
		this.score = score;
		this.topBackground = background.getSubimage(0, 0, background.getWidth(), (int)(35*scaleFactor.getHeight())+ (int)(receiveArrows.getLeft().getHeight()/2));
		this.setOpaque(false);
	}
	
	/**
	 * Updates the time in seconds
	 * @param time
	 */
	public void update(double time){
		score.update(time);
		receiveArrow.update((int)(time*1000));
		this.currentTime = (int)(time*1000);
		List<Arrow> newArrowList = danceChart.getArrowInRangeKeepHold((int)(currentTime-MILLISECONDS_ON_SCREEN/5.0), (int)currentTime+MILLISECONDS_ON_SCREEN);
		checkMissedNotes(currentTime,newArrowList);
		arrowList = newArrowList;
		this.repaint();
		
	}
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		//combo
		g.setFont(new Font("waseem", Font.PLAIN, (int)(150*scaleFactor.getHeight())));
		g.setColor(PURPLE);
		g.drawString(score.getCombo()+"",(int) (288*scaleFactor.getWidth()), (int)(400*scaleFactor.getHeight()));//9
		g.setFont(new Font("Baoli SC", Font.BOLD, (int)(45*scaleFactor.getHeight())));
		g.setColor(BLUE);
		g.drawString("Combo",(int) (430*scaleFactor.getWidth()), (int)(400*scaleFactor.getHeight()));//9
		//descriptions
		if(description != null && description.getImage(currentTime) != null){
			BufferedImage img = description.getImage(currentTime);
			g.drawImage(img,(int)(288*scaleFactor.getWidth() - img.getWidth()/2), (int)(200*scaleFactor.getHeight()), null);
		}
		//pressed holds
		for(Arrow arrow : arrowList){
	    	if(arrow.getHold() >0){
	    		drawArrow(arrow,g);
	    	}
	    }
	    //top corner
		g.drawImage(topBackground, 0, 0, null);
	    //receive arrows
		drawArrow(this.receiveArrow.getUp(),currentTime,Arrow.UP,g);
		drawArrow(this.receiveArrow.getDown(),currentTime,Arrow.DOWN,g);
		drawArrow(this.receiveArrow.getRight(),currentTime,Arrow.RIGHT,g);
		drawArrow(this.receiveArrow.getLeft(),currentTime,Arrow.LEFT,g);
		//unpressed holds and notes
	    for(Arrow arrow : arrowList){
	    	if(arrow.getActive() == false){
	    		drawArrow(arrow,g);
	    	}
	    }
	    g.setFont(new Font("Courier", Font.BOLD, (int)(30*scaleFactor.getHeight())));
	    g.setColor(Color.YELLOW);
	    g.drawString(String.format("%09d",score.getScore()),30, (int)(650*scaleFactor.getHeight()));//9
	    
	}
	private void drawArrow(Arrow arrow,Graphics g){
		drawArrow(arrow.getImage(),arrow.getTime(),arrow.getDirection(),g);
	}
	private void drawArrow(BufferedImage img, int arrowTime, int arrowDirection, Graphics g){
		try{
			int direction = 0;
			switch(arrowDirection){
			case(Arrow.LEFT):
				direction = 0;
				break;
			case(Arrow.DOWN):
				direction = 1;
				break;
			case(Arrow.UP):
				direction = 2;
				break;
			case(Arrow.RIGHT):
				direction = 3;
				break;
			}
			double beat = (arrowTime-currentTime)*1.0/MILLISECONDS_PER_BEAT;
			int y = (int)((beat*scaleFactor.getHeight()*800/BEATS_ON_SCREEN) +35*scaleFactor.getHeight()); //the 35/2 is a hacky way to compensate for the delay between the time we have in code, and displaying it on screen
			int x = (int)(direction*(img.getWidth()) + (10*direction+35)*scaleFactor.getWidth());
			g.drawImage(img, x, y, null);
		} catch(Exception e){
			e.printStackTrace(); //probably a fluke...ignore it
		}
	}
	public Score getScore(){
		return score;
	}
	public void pressLeft(){
		receiveArrow.pressLeft();
		pressArrow(Arrow.LEFT);
	}
	public void pressRight(){
		receiveArrow.pressRight();
		pressArrow(Arrow.RIGHT);
	}
	public void pressUp(){
		receiveArrow.pressUp();
		pressArrow(Arrow.UP);
	}
	public void pressDown(){
		receiveArrow.pressDown();
		pressArrow(Arrow.DOWN);
	}
	public void pressEnter(){
		
	}
	public void releaseLeft(){
		receiveArrow.releaseLeft();
		releaseArrow(Arrow.LEFT);
	}
	public void releaseRight(){
		receiveArrow.releaseRight();
		releaseArrow(Arrow.RIGHT);
	}
	public void releaseUp(){
		receiveArrow.releaseUp();
		releaseArrow(Arrow.UP);
	}
	public void releaseDown(){
		receiveArrow.releaseDown();
		releaseArrow(Arrow.DOWN);
	}
	public void releaseEnter(){
		
	}
	public void checkMissedNotes(int time, List<Arrow> newArrowList){
		if(newArrowList == null || arrowList == null){
			return;
		}
		for(Arrow arrow : arrowList){
			if(newArrowList.contains(arrow) == false){
				if(arrow.getActive() == false){
					arrow.activate();
					description = score.missNote();
					description.start(currentTime);
				}
			}
		}
	}
	private void pressArrow(int direction){
		Arrow arrow = danceChart.pressArrow(direction, currentTime,MILLISECONDS_PRESS_RANGE);
		if(arrow != null){
			description = score.addNote(arrow,currentTime,MILLISECONDS_PRESS_RANGE/2);
			description.start(currentTime);
		}
	}
	private void releaseArrow(int direction){
		Arrow arrow = danceChart.releaseArrow(direction, currentTime);
		score.releaseNote(arrow);
	}
}
