package control;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class ArrowImage {

	private BufferedImage image;
	private BufferedImage baseImage;
	private BufferedImage holdImage;
	private int holdTime;
	private int timeOnScreen;
	private int heightOfScreen;

	/**
	 * This method creates a new ArrowImage
	 * 
	 * @param baseImage
	 *            the image to be used for the arrow
	 * @param holdImage
	 *            the image to be repeated if the arrow is held
	 * @param holdTime
	 *            the amount of time in milliseconds the arrow is held
	 * @param timePerBeat
	 *            the amount of time in milliseconds that is considered to be in
	 *            one beat
	 */
	public ArrowImage(BufferedImage baseImage, BufferedImage holdImage,
			int holdTime, int timeOnScreen,int heightOfScreen) {
		this.baseImage = baseImage;
		this.holdImage = holdImage;
		this.holdTime = holdTime;
		this.timeOnScreen = timeOnScreen;
		this.heightOfScreen = heightOfScreen;
	}

	/**
	 * create the image that this object will house. Must be called before
	 * getImage is called
	 */
	public void build() {
		if (holdTime == 0) {
			image = baseImage;
			return;
		}
		int height = baseImage.getHeight();
		double offsetHeight = height/5.0;
		double offsetTime = (offsetHeight*1.0/heightOfScreen)*timeOnScreen;
		System.out.println("Offset Height: " + offsetHeight);
		System.out.println("Offset Time: " + offsetTime);
		int holdRepeat = (int)Math.round(holdTime/offsetTime);
		height += (holdRepeat) * (offsetHeight);
		image = new BufferedImage(baseImage.getWidth(), height,
				BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.getGraphics();
		g.drawImage(baseImage, 0, 0, null);
		for (int i = 0; i < holdRepeat; i++) {
			g.drawImage(holdImage, 0, (int)((i) * offsetHeight), null);
		}
		g.dispose();
	}

	/**
	 * gets the image that this object built given the specifications
	 * 
	 * @return BufferedImage
	 */
	public BufferedImage getImage() {
		return this.image;
	}
}
