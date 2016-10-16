package display;

import java.awt.image.BufferedImage;
import java.util.List;

import control.Arrow;
import control.CustomSong;

public class CustomDanceChartBuilder implements DanceChartBuilder{

	private List<Arrow> arrowList;
	private BufferedImage[][] rawImage;
	private int heightOfScreen;
	private int timeOnScreen;

	@Override
	public DanceChart build() {
		if(arrowList == null){
			throw new IllegalArgumentException("Arrow List cannot be null");
		}
		if(rawImage == null){
			throw new IllegalArgumentException("Raw Image cannot be null");
		}
		return new DanceChart(arrowList,rawImage, timeOnScreen, heightOfScreen);
	}

	@Override
	public void setRawImage(java.awt.image.BufferedImage[][] rawImage) {
		this.rawImage = rawImage;
	}

	@Override
	public void setHeightOfScreen(int heightOfScreen) {
		this.heightOfScreen = heightOfScreen;
	}
	
	/**
	 * Sets the amount of time in milliseconds that fits on the screen
	 * @param timeOnScreen time in milliseconds
	 */
	public void setTimeOnScreen(int timeOnScreen){
		this.timeOnScreen = timeOnScreen;
	}
	
	public void setSong(CustomSong song){
		this.arrowList = song.getArrowList();
	}

}
