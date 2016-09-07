package display;

import java.util.LinkedList;
import java.util.List;

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

	/**
	 * sets up a dance chart for a song
	 * 
	 * @param song
	 *            the song that this dance chart represents
	 * @param scrollSpeed
	 *            the number of milliseconds that should be considered to be in
	 *            one beat
	 */
	public DanceChart(Song song) {
		arrowList = new LinkedList<Arrow>();
		Note[][] noteList = song.getNotes();
		for (Note[] beatNote : noteList) {
			if (beatNote != null) {
				for (Note note : beatNote) {
					if (note != null) {
						Arrow arrow = new Arrow(note.getBeat()*song.getBpm(), note.getHold(),note.getDirection(),
								note.getImage());
						arrowList.add(arrow);
					}
				}
			}

		}
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
		while (startIndex < arrowList.size() -1
				&& arrowList.get(startIndex).getTime() < startTime) {
			startIndex++;
		}
		int endIndex = startIndex;
		while (endIndex < arrowList.size() -1
				&& arrowList.get(endIndex).getTime() < endTime) {
			endIndex++;
		}
		if (arrowList.get(startIndex).getTime() <= endTime)
			return arrowList.subList(startIndex, endIndex);
		return new LinkedList<Arrow>();
	}

}
