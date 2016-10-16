package display;

import java.awt.image.BufferedImage;

import control.Arrow;

public class ReceiveArrow {

	private final int CHANGE_TIME = 20; // Time in milliseconds
	private BufferedImage[][] image;
	private int currentTime;
	private int leftTime, leftDirection, leftIndex;
	private int rightTime, rightDirection, rightIndex;
	private int upTime, upDirection, upIndex;
	private int downTime, downDirection, downIndex;

	/**
	 * A class which represents all directions of receive arrows and updates the
	 * image it should be on based on how much time has passed
	 * 
	 * @param image
	 *            array of array of images representing the arrows
	 */
	public ReceiveArrow(BufferedImage[][] image) {
		this.image = image;
	}

	/**
	 * updates the time in milliseconds and index of all the direction arrows
	 * 
	 * @param time
	 *            the current time in milliseconds in the song
	 */
	public void update(int time) {
		if (leftTime > CHANGE_TIME) {
			leftTime = 0;
			if (leftDirection == +1) {
				leftIndex = Math.min(leftIndex + 1,
						image[Arrow.LEFT].length - 1);
			} else if (leftDirection == -1) {
				leftIndex = Math.max(0, leftIndex - 1);
			}
		}
		if (rightTime > CHANGE_TIME) {
			rightTime = 0;
			if (rightDirection == +1) {
				rightIndex = Math.min(rightIndex + 1,
						image[Arrow.LEFT].length - 1);
			} else if (rightDirection == -1) {
				rightIndex = Math.max(0, rightIndex - 1);
			}
		}
		if (downTime > CHANGE_TIME) {
			downTime = 0;
			if (downDirection == +1) {
				downIndex = Math.min(downIndex + 1,
						image[Arrow.LEFT].length - 1);
			} else if (downDirection == -1) {
				downIndex = Math.max(0, downIndex - 1);
			}
		}
		if (upTime > CHANGE_TIME) {
			upTime = 0;
			if (upDirection == +1) {
				upIndex = Math.min(upIndex + 1, image[Arrow.LEFT].length - 1);
			} else if (upDirection == -1) {
				upIndex = Math.max(0, upIndex - 1);
			}
		}
		leftTime += time - currentTime;
		rightTime += time - currentTime;
		upTime += time - currentTime;
		downTime += time - currentTime;
		currentTime = time;
	}

	/**
	 * gets the current image for the left receive arrow
	 * 
	 * @return BufferedImage for the left receive arrow
	 */
	public BufferedImage getLeft() {
		return image[Arrow.LEFT][leftIndex];
	}

	/**
	 * gets the current image for the right receive arrow
	 * 
	 * @return BufferedImage for the right receive arrow
	 */
	public BufferedImage getRight() {
		return image[Arrow.RIGHT][rightIndex];
	}

	/**
	 * gets the current image for the up receive arrow
	 * 
	 * @return BufferedImage for the up receive arrow
	 */
	public BufferedImage getUp() {
		return image[Arrow.UP][upIndex];
	}

	/**
	 * gets the current image for the down receive arrow
	 * 
	 * @return BufferedImage for the down receive arrow
	 */
	public BufferedImage getDown() {
		return image[Arrow.DOWN][downIndex];
	}

	/**
	 * sets the direction for the left arrow for when it is pressed
	 */
	public void pressLeft() {
		leftDirection = 1;
	}

	/**
	 * sets the direction for the right arrow for when it is pressed
	 */
	public void pressRight() {
		rightDirection = 1;
	}

	/**
	 * sets the direction for the up arrow for when it is pressed
	 */
	public void pressUp() {
		upDirection = 1;
	}

	/**
	 * sets the direction for the down arrow for when it is pressed
	 */
	public void pressDown() {
		downDirection = 1;
	}

	/**
	 * sets the direction for the left arrow for when it is released
	 */
	public void releaseLeft() {
		leftDirection = -1;
	}

	/**
	 * sets the direction for the right arrow for when it is released
	 */
	public void releaseRight() {
		rightDirection = -1;
	}

	/**
	 * sets the direction for the up arrow for when it is released
	 */
	public void releaseUp() {
		upDirection = -1;
	}

	/**
	 * sets the direction for the down arrow for when it is released
	 */
	public void releaseDown() {
		downDirection = -1;
	}
}
