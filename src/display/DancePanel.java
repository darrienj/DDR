package display;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JPanel;

import control.*;
public class DancePanel extends JPanel implements InputReceiver{

	private final static Color PURPLE = new Color(188,69,247);
	private final static Color BLUE = new Color(40,219,255);
	private BufferedImage[][] newReceiveArrows;
	private Dimension scaleFactor;
	private int FORCED_BEATS_ON_SCREEN = 14;  //MUST CHANGE IN in MUSIC AS WELL
	private int BEATS_ON_SCREEN = 16; //goal: 90 bpm should be 4(16) and 180 should be 8(32)
	private Song song; //4 beats on screen at a time.  That's 16 16th notes
	private Note[][] notes;
	private double currentBeat;
	private int oldBeat;
	private boolean pressed[];
	private double initialPressTimer[];
	private final double INITIAL_PRESS = 1; //1 sixteenth note's length
	
	private final double PRESS_THRESHOLD = 1.5;
	private final float pressedRelaxRate = 32;
	private double[] pressedRelax;
	private Score score;
	private double descriptionBeatCount;
	private BufferedImage[] description;
	private double descriptionRate = 40;
	private double holdScore[];
	BufferedImage topCorner; //top corner of background image.  Redrawn every frame
	//draw holds, draw topCorner, draw non-holds.  Also, remove notes from song when they have been
	//pressed, unless they're holds.
	private double oldTime = 0;
	public DancePanel(Dimension scaleFactor,BufferedImage[][] receiveArrows,Song song,BufferedImage background){
		super();
		this.scaleFactor = scaleFactor;
		this.song = song;
		try {
			this.score = new Score(song.getId(),scaleFactor,song);
		} catch (IOException e) {
			//can't display description images
			e.printStackTrace();
		}
		this.notes = new Note[35][];
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
		this.holdScore = new double[4];
		this.initialPressTimer = new double[4];
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
		currentBeat = (time/60)*this.song.getBpm()*4 + 1;
		if(oldBeat != (int)currentBeat){
			for(int i = 0;i<notes.length;i++){
				notes[i] = song.getNotes(i+(int)currentBeat-3);
			}
		}
		if(notes == null || notes[0] == null){
			return;
		}
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
		///Press the note
		for(int i = 0;i<initialPressTimer.length;i++){
			if(initialPressTimer[i] > 0 && pressed[i]){
				for(int j = 0;j<notes.length;j++){
					if(notes[j][i] != null && notes[j][i].isActive() == false){
						double difference = Math.abs(notes[j][i].getBeat() - currentBeat);
						if(difference < PRESS_THRESHOLD){
							initialPressTimer[i] = 0;
							song.activateNote((int)notes[j][i].getBeat(),i);
							description = score.addNote(difference);
							descriptionBeatCount = 0.1;
							if(notes[j][i].getHold() > 0){
								holdScore[i] = notes[j][i].getHold();
							}
							break;
						}
					}
				}
			}
			//release the note
			if(initialPressTimer[i] != INITIAL_PRESS && pressed[i] == false){
				for(int j = 0;j<notes.length;j++){
					if(notes[j][i] != null && notes[j][i].isActive() == true && notes[j][i].getHold() > 0 && notes[j][i].getHold()-(currentBeat - notes[j][i].getBeat()) > 1){
						song.updateHold(notes[j][i].getBeat(),(int)(currentBeat),notes[j][i].getHold()- (int)(currentBeat - notes[j][i].getBeat()),i);
						holdScore[i] = 0;
					}
				}
			}
			if(pressed[i]){
				initialPressTimer[i] -= (deltaTime/60)*this.song.getBpm()*4;
			} else{
				initialPressTimer[i] = INITIAL_PRESS;
			}
		}
		for(int i = 0;i<notes[0].length;i++){
			if(notes[0][i] != null && notes[0][i].isActive() == false && notes[0][i].isMiss() == false && notes[0][i].isBrokenHold() == false){
				description = score.missNote();
				song.missNote(notes[0][i].getBeat(),i);
				descriptionBeatCount = 0.1;
			}
		}
		oldBeat = (int)currentBeat;
		this.repaint();
		oldTime = time;
		if(descriptionBeatCount != 0){
			double descriptionTmp = descriptionBeatCount + deltaTime*descriptionRate;
			if(description != null && descriptionTmp < description.length){
				this.descriptionBeatCount = descriptionTmp;
			} else{
				this.descriptionBeatCount = 0;
			}
		}
		for(int i = 0;i<holdScore.length;i++){
			if(holdScore[i] > 0){
				if(holdScore[i] > (deltaTime/60)*this.song.getBpm()*4){
					score.addHold((deltaTime/60)*this.song.getBpm()*4);
					holdScore[i] -= (deltaTime/60)*this.song.getBpm()*4;
				} else{
					score.addHold(holdScore[i]);
					holdScore[i] = 0;
				}
				
			}
		}
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
		if(notes == null || notes[0] == null){
			return;
		}
		if(description != null && descriptionBeatCount != 0){
			BufferedImage img = description[(int)(descriptionBeatCount)];
			g.drawImage(img,(int) (288*scaleFactor.getWidth()) - img.getWidth()/2, (int)(200*scaleFactor.getHeight()),null);
		}
		//active holds
	    for(int i = 0;i<notes.length;i++){
	    	for(int j = 0;j<notes[i].length;j++){
	    		if(notes[i][j] != null && notes[i][j].isActive() && notes[i][j].getHold() > 0){
	    			if(notes[i][j].getImage() != null){
		    			BufferedImage img = notes[i][j].getImage();
		    			drawNote(img,g,i,j);
	    			}
	    		}
	    	}	
	    }  
	    //top corner
	    g.drawImage(topCorner, 0, 0, null);
	    //receive arrows
	    for(int i = 0;i<newReceiveArrows.length;i++){
	    	BufferedImage receive;
	    	if(pressed[i]){
	    		receive = newReceiveArrows[i][1];
	    	}else if(pressedRelax[i] != 0){
	    		receive = newReceiveArrows[i][(int)(pressedRelax[i])];
	    	} else{
	    		receive = newReceiveArrows[i][0];
	    	}
	    	g.drawImage(receive,(int) (i*receive.getWidth()+(35+i*10)*scaleFactor.getWidth()), (int)(35*scaleFactor.getHeight()),null);
	    }
		//inactive holds and notes
	    for(int i = 0;i<notes.length;i++){
	    	for(int j = 0;j<notes[i].length;j++){
	    		if(notes[i][j] != null && notes[i][j].isActive() == false){
	    			if(notes[i][j].getImage() != null){
	    				BufferedImage img = notes[i][j].getImage();
	    				drawNote(img,g,i,j);
	    			}
	    		}
	    	}	
	    }  
	    g.setFont(new Font("Courier", Font.BOLD, (int)(30*scaleFactor.getHeight())));
	    g.setColor(Color.YELLOW);
	    g.drawString(String.format("%09d",score.getScore()),30, (int)(650*scaleFactor.getHeight()));//9
	    
	}
	public void drawNote(BufferedImage img,Graphics g,int i,int j){
		try{
			int y = (int)(((notes[i][j].getBeat()-currentBeat)*scaleFactor.getHeight()*800/BEATS_ON_SCREEN) +35*scaleFactor.getHeight()); //the 35/2 is a hacky way to compensate for the delay between the time we have in code, and displaying it on screen
			int x = (int)(j*(newReceiveArrows[0][0].getWidth()) + (10*j+35)*scaleFactor.getWidth());
			g.drawImage(img, x, y, null);
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
