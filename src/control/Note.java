package control;
import java.awt.image.BufferedImage;

/**
 * This class is an information holder for a song note.
 * @author drichmond
 *
 */
public class Note {
	
	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	public static final int UP = 2;
	public static final int DOWN = 3;
	private int direction;
	private int beat;
	private int hold;
	private BufferedImage img;
	private boolean active;
	private boolean canBeActive;
	private boolean miss;
	
	public Note(int direction,int beat,int hold,BufferedImage img){
		
		this.direction  = direction;
		this.beat = beat;
		this.hold = hold;
		this.img = img;  //TODO image should not be stored in note.
		this.active = false;
		this.canBeActive = true; //TODO information on whether the note is active should not be here
		this.miss = false; //TODO whether the user missed the note should not be stored here
	}
	public BufferedImage getImage(){
		return img;
	}
	public int getBeat(){
		return beat;
	}
	public int getHold(){
		return hold;
	}
	public int getDirection(){
		return this.direction;
	}
	public void breakHold(){
		this.canBeActive = false;
		this.active = false;
	}
	public boolean isBrokenHold(){
		return !this.canBeActive && !this.active;
	}
	public void setActive(boolean val){
		if(this.canBeActive == true && val == true){
			this.active = true;
			this.canBeActive = false;
		} else if(val == false){
			this.active = false;
		}
	}
	public void miss(){
		this.miss = true;
	}
	public boolean isMiss(){
		return this.miss;
	}
	public boolean isActive(){
		return active;
	}
}

