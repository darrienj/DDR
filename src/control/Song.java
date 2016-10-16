package control;

public interface Song {

	/**
	 * Gets the offset each note should have from the current time in the music,
	 * measured in milliseconds
	 * 
	 * @return offset
	 */
	public double getOffset();
	
	/**
	 * Gets the difficulty level of the song
	 * @return difficulty
	 */
	public int getDifficulty();
	
	/**
	 * Gets the total number of notes in the song
	 * @return noteCount
	 */
	public int getNoteCount();
	
	/**
	 * Gets the total amount of time in milliseconds that notes are held in this song.
	 * @return total hold time
	 */
	public int getTotalHoldDuration();
	
	/**
	 * Gets the name of the song
	 * @return
	 */
	public String getSongName();
	
	/**
	 * Gets the id of the song
	 * @return int id
	 */
	public int getId();
	
}
