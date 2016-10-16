package display;

import java.awt.image.BufferedImage;
import java.util.List;

import control.Arrow;

public interface DanceChartBuilder {
//	List<Arrow> originalArrowList,BufferedImage[][] rawImage, int timePerBeat, int heightOfScreen

	public DanceChart build();
	public void setRawImage(BufferedImage[][] rawImage);
	public void setHeightOfScreen(int heightOfScreen);
	public void setTimeOnScreen(int timeOnScreen);
}
