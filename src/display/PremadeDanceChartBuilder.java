package display;

import java.awt.image.BufferedImage;

import control.PremadeSong;
import control.Song;

public class PremadeDanceChartBuilder implements DanceChartBuilder{

	private PremadeSong song;
	private BufferedImage[][] rawImage;
	private int timePerBeat;
	private int heightOfScreen;
	private int timeOnScreen;

	@Override
	public DanceChart build() {
		if(song == null){
			throw new IllegalArgumentException("Song cannot be null");
		}
		if(rawImage == null){
			throw new IllegalArgumentException("Raw Image cannot be null");
		}
		if(timePerBeat == 0){
			throw new IllegalArgumentException("Time Per Beat must be positive");
		}
		if(heightOfScreen == 0){
			throw new IllegalArgumentException("Height of Screen must be set");
		}
		if(timeOnScreen == 0){
			throw new IllegalArgumentException("Time on Screen must be set");
		}
		return new DanceChart(song,rawImage,timePerBeat,timeOnScreen, heightOfScreen);
	}

	@Override
	public void setRawImage(BufferedImage[][] rawImage) {
		this.rawImage = rawImage;
	}

	@Override
	public void setHeightOfScreen(int heightOfScreen) {
		this.heightOfScreen = heightOfScreen;
	}
	
	@Override
	public void setTimeOnScreen(int timeOnScreen){
		this.timeOnScreen = timeOnScreen;
	}
	
	public void setSong(PremadeSong song){
		this.song = song;
	}
	
	public void setTimePerBeat(int timePerBeat){
		this.timePerBeat = timePerBeat;
	}

}
