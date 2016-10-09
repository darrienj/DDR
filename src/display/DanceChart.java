package display;

import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

import control.Arrow;
import control.ArrowImage;
import control.Note;
import control.Song;

/**
 * this class holds the arrows to be displayed for a song
 * 
 * @author drichmond
 *
 */
public class DanceChart {

	private List<Arrow> arrowList;
	private List<Arrow> holdList = new LinkedList<Arrow>(); // keeps all of the
															// notes that are in
															// the middle of a
															// hold

	/**
	 * sets up a dance chart for a song
	 * 
	 * @param song
	 *            the song that this dance chart represents
	 * @param rawImage
	 *            the images to be used for arrows. Each array should have 2
	 *            elements, one for the note, and one for the hold
	 * @param timePerBeat
	 *            the number of milliseconds that should be considered to be in
	 *            one beat
	 *
	 */
	public DanceChart(Song song, BufferedImage[][] rawImage, int timePerBeat,
			int heightOfScreen) {
		arrowList = new LinkedList<Arrow>();
		Note[][] noteList = song.getNotes();
		for (Note[] beatNote : noteList) {
			if (beatNote != null) {
				for (Note note : beatNote) {
					if (note != null) {
						ArrowImage image = new ArrowImage(
								rawImage[note.getDirection()][0],
								rawImage[note.getDirection()][1], beatToTime(
										note.getHold(), song.getBpm()),
								timePerBeat,heightOfScreen);
						image.build();
						Arrow arrow = new Arrow(beatToTime(note.getBeat(),
								song.getBpm()), note.getHold(),
								note.getDirection(), image);
						arrowList.add(arrow);
					}
				}
			}

		}
	}

	/**
	 * this method processes a button press in the dance chart. If an arrow
	 * matches the direction and time entered, that arrow will be set to
	 * inactive and the score from the arrow will be returned
	 * 
	 * @param direction
	 *            the direction the arrow is facing
	 * @param time
	 *            the time of the song in milliseconds
	 * @param range
	 *            the range of time to consider an arrow being pressed for. If
	 *            the arrow falls out of this range from the time entered, it
	 *            will not be considered "pressed"
	 * @return the arrow which was pressed. Null if no arrow in the song matched
	 *         the timing of the press
	 */
	public Arrow pressArrow(int direction, int time, int range) {
		List<Arrow> arrowList = getArrowInRange(time - range / 2, time + range
				/ 2);
		int index = 0;
		while (index < arrowList.size()) {
			Arrow arrow = arrowList.get(index);
			if (arrow.getActive() == false && arrow.getDirection() == direction) {
				arrow.activate();
				if (arrow.getHold() > 0)
					holdList.add(arrow);
				return arrow;
			}
			index++;
		}
		return null;
	}

	/**
	 * this method processes a button release in the dance chart. If an arrow
	 * matches the direction and time entered, that arrow will be set to
	 * inactive and the score from the arrow will be returned
	 * 
	 * @param direction
	 *            the direction the arrow is facing
	 * @param time
	 *            the time of the song in milliseconds
	 */
	public Arrow releaseArrow(int direction, int time) {
		int i = 0;
		while (i < holdList.size()) {
			Arrow arrow = holdList.get(i);
			if (arrow.getHold() + arrow.getTime() > time) {
				arrow.deactivate(time);
				holdList.remove(i);
			}
			i++;
		}
		i = 0;
		while (i < holdList.size()) {
			Arrow arrow = holdList.get(i);
			if (arrow.getDirection() == direction) {
				arrow.deactivate(time);
				holdList.remove(arrow);
				return arrow;
			}
			i++;
		}
		return null;
	}

	/**
	 * Gets a list of arrows from the start time to the end time, ordered in
	 * ascending order by time.
	 * 
	 * @param startTime
	 *            the time the result list should start from
	 * @param endTime
	 *            the time the result list should end at
	 * @return List of Arrows that are between the specified time frame. If no
	 *         arrows exist, it will return an empty list
	 */
	public List<Arrow> getArrowInRange(int startTime, int endTime) {

		int startIndex = 0;
		while (startIndex < arrowList.size() - 1
				&& arrowList.get(startIndex).getTime() < startTime) {
			startIndex++;
		}
		int endIndex = startIndex;
		while (endIndex < arrowList.size() - 1
				&& arrowList.get(endIndex).getTime() < endTime) {
			endIndex++;
		}
		if (arrowList.get(startIndex).getTime() <= endTime) {
			List<Arrow> result = arrowList.subList(startIndex, endIndex);
			return result;
		}
		return new LinkedList<Arrow>();
	}

	public List<Arrow> getArrowInRangeKeepHold(int startTime, int endTime){
		List<Arrow> result = getArrowInRange(startTime,endTime);
		//add to holdList if not already there
		for(int i = 0;i<result.size();i++){
			if(result.get(i).getHold() > 0){
				if(holdList.contains(result.get(i)) == false){
					holdList.add(result.get(i));
				}
			}
		}
		//drop irrelevant stuff from holdList, add relevant to result
		List<Arrow> removeList = new LinkedList<Arrow>();
		for(int i = 0;i<holdList.size();i++){
			Arrow holdArrow = holdList.get(i);
			if(holdArrow.getHold() + holdArrow.getTime() > endTime){
				removeList.add(holdArrow);
			} else if(result.contains(holdList.get(i)) == false){
				result.add(holdList.get(i));
			}
		}
		for(int i = 0;i<removeList.size();i++){
			holdList.remove(removeList.get(i));
		}
		return result;
	}

	/**
	 * converts from beat units, where there's technical 4 "beats" in a quarter
	 * note, to milliseconds
	 * 
	 * @param beat
	 * @return time in milliseconds
	 */
	private int beatToTime(int beat, int bpm) {
		return (int) ((beat + 0.0) * 60 * 1000 / (bpm * 4));
	}
}
