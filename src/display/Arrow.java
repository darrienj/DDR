package display;

import java.awt.image.BufferedImage;

/**
 * this class holds the information for an arrow displayed on the screen
 * @author drichmond
 *
 */
public class Arrow {

	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	public static final int UP = 2;
	public static final int DOWN = 3;
	
	private int time;
	private int hold;
	private int direction;
	private BufferedImage image;
	private boolean active;
	
	/**
	 * 
	 * @param time the time the note should be displayed at in milliseconds
	 * @param hold the amount of time the note should be held for in milliseconds
	 * @param image the image to be displayed for the arrow.
	 */
	public Arrow(int time,int hold,int direction,BufferedImage image){
		this.time = time;
		this.hold = hold;
		this.image = image;
		this.active = false;
		this.direction = direction;
	}
	
	/**
	 * activate the note.
	 */
	public void activate(){
		this.active = true;
	}
	
	/**
	 * deactivates this note.
	 * @param time the time in the song the arrow was deactivated at, measured in milliseconds
	 */
	public void deactivate(int time){
		this.active = false;
		this.hold = Math.max(0, this.hold - (time - this.time));
	}
	
	/**
	 * gets whether this note is currently active or not
	 * @return active
	 */
	public boolean getActive(){
		return active;
	}
	
	/**
	 * gets the image which represents this arrow
	 * @return image
	 */
	public BufferedImage getImage(){
		return this.image;
	}
	
	/**
	 * gets the time the arrow is set to. Measured in milliseconds
	 * @return time
	 */
	public int getTime(){
		return this.time;
	}

	public int getDirection() {
		return this.direction;
	}
}
