package display;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import javax.swing.JPanel;

import control.*;
public class DancePanel extends JPanel implements InputReceiver{

	private final static Color PURPLE = new Color(188,69,247);
	private final static Color BLUE = new Color(40,219,255);
	private BufferedImage[][] newReceiveArrows;
	private Dimension scaleFactor;
	private int FORCED_BEATS_ON_SCREEN = 14; 
	private int BEATS_ON_SCREEN = 16; 
	private double currentBeat;
	private boolean pressed[];
	
	private final float pressedRelaxRate = 32;
	private double[] pressedRelax;
	private Score score;
	private BufferedImage topCorner; 
	private DanceChart danceChart;
	
	private double oldTime = 0;
	public DancePanel(Dimension scaleFactor,BufferedImage[][] receiveArrows,Song song,BufferedImage background){
		super();
		danceChart = new DanceChart(song);
		this.scaleFactor = scaleFactor;
		try {
			this.score = new Score(song.getId(),scaleFactor,song);
		} catch (IOException e) {
			//can't display description images
			e.printStackTrace();
		}
 		this.newReceiveArrows = new BufferedImage[4][];
		for(int i = 0;i<newReceiveArrows.length;i++){
			this.newReceiveArrows[i] = new BufferedImage[7];
		}
		this.pressed = new boolean[4];
		for(int i = 0;i<pressed.length;i++){
			pressed[i] = false;
		}
		BEATS_ON_SCREEN = (int)(22*song.getBpm()/140.0);
		if(FORCED_BEATS_ON_SCREEN != -1){
			BEATS_ON_SCREEN = FORCED_BEATS_ON_SCREEN;
		}
		this.pressedRelax = new double[4];
		for(int i = 0;i<receiveArrows.length;i++){
			for(int j = 0;j<receiveArrows[i].length;j++){
				Image scaledReceiveArrows = receiveArrows[i][j].getScaledInstance((int)(scaleFactor.getWidth()*125), (int)(scaleFactor.getHeight()*125), BufferedImage.SCALE_SMOOTH);
				newReceiveArrows[i][j] = new BufferedImage(scaledReceiveArrows.getWidth(null), scaledReceiveArrows.getHeight(null), BufferedImage.TYPE_INT_ARGB);
				Graphics g2 = newReceiveArrows[i][j].getGraphics();
				g2.drawImage(scaledReceiveArrows, 0, 0, null);
				g2.dispose();
			}
		}
		this.topCorner = background.getSubimage(0, 0, (int)(4*(newReceiveArrows[0][0].getWidth()+35+(10*scaleFactor.getWidth()))), (int)(35*scaleFactor.getHeight()+newReceiveArrows[0][0].getHeight()/2));
		this.setOpaque(false);
	}
	
	public void update(double time){
		
		double deltaTime = time - oldTime;
		for(int i = 0;i<pressedRelax.length;i++){
			if(pressedRelax[i] != 0 && pressed[i] == false){
				double tmp = pressedRelax[i] + pressedRelaxRate*deltaTime;
				if(tmp >= newReceiveArrows[0].length-1){
					pressedRelax[i] = 0;
				} else{
					pressedRelax[i] = tmp;
				}
			}
		}
		
		this.repaint();
		
	}
	@Override
	protected void paintComponent(Graphics g) {

		super.paintComponent(g);
		//combo
		if(score.getCombo() > 10){//waseem  Athelas Baoli SC
			g.setFont(new Font("waseem", Font.PLAIN, (int)(150*scaleFactor.getHeight())));
			g.setColor(PURPLE);
			g.drawString(score.getCombo()+"",(int) (288*scaleFactor.getWidth()), (int)(400*scaleFactor.getHeight()));//9
			g.setFont(new Font("Baoli SC", Font.BOLD, (int)(45*scaleFactor.getHeight())));
			g.setColor(BLUE);
			g.drawString("Combo",(int) (430*scaleFactor.getWidth()), (int)(400*scaleFactor.getHeight()));//9
		}
		//descriptions

		//active holds

	    //top corner
	    g.drawImage(topCorner, 0, 0, null);
	    //receive arrows
	    
		//inactive holds and notes
	    List<Arrow> arrowList = danceChart.getArrowInRange((int)currentBeat, (int)currentBeat+20);
	    for(Arrow arrow : arrowList){
	    	drawArrow(arrow,g);
	    }
	    g.setFont(new Font("Courier", Font.BOLD, (int)(30*scaleFactor.getHeight())));
	    g.setColor(Color.YELLOW);
	    g.drawString(String.format("%09d",score.getScore()),30, (int)(650*scaleFactor.getHeight()));//9
	    
	}
	private void drawArrow(Arrow arrow,Graphics g){
		try{
			int direction = 0;
			switch(arrow.getDirection()){
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
			int y = (int)(((arrow.getTime()-currentBeat)*scaleFactor.getHeight()*800/BEATS_ON_SCREEN) +35*scaleFactor.getHeight()); //the 35/2 is a hacky way to compensate for the delay between the time we have in code, and displaying it on screen
			int x = (int)(direction*(newReceiveArrows[0][0].getWidth()) + (10*direction+35)*scaleFactor.getWidth());
			g.drawImage(arrow.getImage(), x, y, null);
		} catch(Exception e){
			e.printStackTrace(); //probably a fluke...ignore it
		}
	}
	public Score getScore(){
		return score;
	}
	public void pressLeft(){
		pressed[0] = true;
	}
	public void pressRight(){
		pressed[3] = true;
	}
	public void pressUp(){
		pressed[2] = true;
	}
	public void pressDown(){
		pressed[1] = true;
	}
	public void pressEnter(){
		
	}
	public void releaseLeft(){
		pressed[0] = false;
		pressedRelax[0] = 2f;
	}
	public void releaseRight(){
		pressed[3] = false;
		pressedRelax[3] = 2f;
	}
	public void releaseUp(){
		pressed[2] = false;
		pressedRelax[2] = 2f;
	}
	public void releaseDown(){
		pressed[1] = false;
		pressedRelax[1] = 2f;
	}
	public void releaseEnter(){
		
	}
}
