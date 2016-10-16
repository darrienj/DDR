package control;

import java.awt.image.BufferedImage;

/**
 * This class represents an image that will change images over time at a set
 * rate.
 * 
 * @author drichmond
 *
 */
public class FadeImage {

	private final BufferedImage[] image;
	private final int changeTime;
	private int startTime;

	/**
	 * Creates a new FadeImage object
	 * 
	 * @param image
	 * @param changeTime
	 */
	public FadeImage(BufferedImage[] image, int changeTime) {
		this.image = image;
		this.changeTime = changeTime;
	}

	/**
	 * start the image fading with this specified time. All subsequent times
	 * passed into update will be compared to this time
	 * 
	 * @param time
	 *            the time in milliseconds that this image will begin fading
	 */
	public void start(int time) {
		this.startTime = time;
	}

	/**
	 * gets the current BufferedImage this fading image is on. The object must
	 * have been started before calling this method
	 * 
	 * @param time
	 *            the current time in milliseconds
	 * @return BufferedImage, or null if it has finished fading and there are no
	 *         more images to display
	 */
	public BufferedImage getImage(int time) {
		if(time < startTime){
			throw new IllegalArgumentException("time cannot be less than start time");
		}
		int index = (time - startTime) / changeTime;
		if (index < image.length) {
			return image[index];
		} else {
			return null;
		}
	}
}
