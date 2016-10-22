package control;

import java.awt.image.BufferedImage;

/**
 * this class holds the information for an arrow displayed on the screen
 * 
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
	private ArrowImage image;
	private boolean active;
	private boolean hasBeenPressed;

	/**
	 * 
	 * @param time
	 *            the time the note should be displayed at in milliseconds
	 * @param hold
	 *            the amount of time the note should be held for in milliseconds
	 * @param direction
	 *            the direction this arrow is facing, should be one of the
	 *            direction constants in this class
	 * @param image
	 *            the ArrowImage which holds the image to display. This
	 *            ArrowImage should already be built.
	 */
	public Arrow(int time, int hold, int direction, ArrowImage image) {
		this.time = time;
		this.hold = hold;
		this.image = image;
		this.active = false;
		this.direction = direction;
		this.hasBeenPressed = false;
	}

	public boolean hasBeenPressed(){
		return hasBeenPressed;
	}
	/**
	 * activate the note.
	 */
	public void activate() {
		this.active = true;
		hasBeenPressed = true;
	}

	/**
	 * deactivates this note.
	 * 
	 * @param time
	 *            the time in the song the arrow was deactivated at, measured in
	 *            milliseconds
	 */
	public void deactivate(int time) {
		if(Math.abs(time-(this.time + hold )) < 200){
			this.active = true;
		} else{
			this.active = false;
		}
	}

	/**
	 * gets whether this note is currently active or not. If the note is not
	 * active it will not give any value for score
	 * 
	 * @return active
	 */
	public boolean getActive() {
		return active;
	}

	/**
	 * gets the image which represents this arrow
	 * 
	 * @return image
	 */
	public BufferedImage getImage() {
		return this.image.getImage();
	}

	/**
	 * gets the time the arrow is set to. Measured in milliseconds
	 * 
	 * @return time
	 */
	public int getTime() {
		return this.time;
	}

	/**
	 * gets the direction of the arrow
	 * 
	 * @return
	 */
	public int getDirection() {
		return this.direction;
	}

	/**
	 * gets whether the note is finished being displayed or not. If it still
	 * needs to be displayed, then it will return true, otherwise false
	 * 
	 * @param songTime
	 *            the time in milliseconds that the song is at
	 * @return boolean whether the note is finished
	 */
	public boolean getFinished(int songTime) {
		if ((songTime > time + hold && active == true) || hold == 0
				&& active == true) {
			return true;
		}
		return false;
	}

	/**
	 * Gets the total amount of time this not is supposed to be held in
	 * milliseconds
	 * 
	 * @return
	 */
	public int getHold() {
		return this.hold;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Arrow) {
			Arrow arrow = (Arrow) obj;
			return arrow.getDirection() == getDirection()
					&& arrow.getHold() == getHold()
					&& arrow.getTime() == getTime();
		}
		return false;
	}
}
