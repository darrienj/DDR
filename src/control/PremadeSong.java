package control;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


public class PremadeSong implements Song{

	private String name;
	private Note[][] noteList;
	private BufferedImage quarterNoteImage[][];
	private BufferedImage eighthNoteImage[][];
	private BufferedImage sixteenthNoteImage[][];
	private int bpm;
	private int id;
	private int holdCount = 0;
	private Note[] emptyNoteList;
	int offset;
	private final int BEATS_ON_SCREEN = 14;
	private int noteCount;
	private String readableName;
	private String difficulty;
	public PremadeSong(int id, String name, String readableName,String difficulty) throws Exception{
		this.id = id;
		this.name = name;
		this.difficulty = difficulty;
		this.readableName = readableName;
		emptyNoteList = new Note[4];
		File file = new File(name);
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			this.bpm = Integer.parseInt(reader.readLine());
			int beatCount = 0;
			this.offset = 0;
			while(reader.ready()){
				String line = reader.readLine();
				if(!line.contains("==================")){
					beatCount++;
				}
				if(line.contains("*")){
					this.offset += (60.0/bpm)*-1;
				}
			}
			reader.close();
			noteList = new Note[beatCount][4];
			
			reader = new BufferedReader(new FileReader(file));
			reader.readLine(); //get rid of bpm
			beatCount = 1 + offset;
			while(reader.ready()){
				String line = reader.readLine();
				if(!line.contains("==================") && !line.contains("*")){
					while(line.length() > 1){
						String type = line.substring(0, 1);
						int duration = Integer.parseInt(line.substring(1,4));
						if(type.equals("L")){
							noteList[beatCount][Note.LEFT] = new Note(Note.LEFT,beatCount,duration);
						}
						if(type.equals("D")){
							noteList[beatCount][Note.DOWN] = new Note(Note.DOWN,beatCount,duration);
						}
						if(type.equals("U")){
							noteList[beatCount][Note.UP] = new Note(Note.UP,beatCount,duration);
						}
						if(type.equals("R")){
							noteList[beatCount][Note.RIGHT] = new Note(Note.RIGHT,beatCount,duration);
						}
						if(line.length() > 4){
							line = line.substring(4, line.length());
						} else{
							line = "";
						}
					}
					beatCount++;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new Exception("Failed to load song");
		}
		
		for(int i = 0;i < noteList.length;i++){
			for(int j = 0;j<noteList[i].length;j++){
				if(noteList[i][j] != null){
					double minutesPerBeat = 1.0/bpm/4;
					double minutes = minutesPerBeat*noteList[i][j].getHold();
					double seconds = minutes*60.0;
					int milliseconds = (int)(seconds * 1000);
					this.holdCount += milliseconds;
					this.noteCount++;
				}
			}
		}
		
	}
	/**
	 * this will return the notes on the beat you entered.  If you've reached the end of the
	 * song it will return null
	 * @param beat
	 * @return
	 */
	public Note[] getNotes(int beat){
		if(beat < noteList.length && beat > 0){
			return noteList[beat];
		} else{
			return emptyNoteList;
		}
	}
	
	public Note[][] getNotes(){
		return noteList;
	}
	public String getSongName(){
		return readableName;
	}
	/**
	 * Gets the offset of the songs notes, measured in seconds
	 */
	public double getOffset(){
		return this.offset;
	}
	public int getBpm(){
		return this.bpm;
	}
	public int getNoteCount(){
		return this.noteCount;
	}
	public int getTotalHoldDuration(){
		return this.holdCount;
	}
	public int getId(){
		return this.id;
	}
	public int getDifficulty(){
		if(difficulty.equals("Hard")){
			return 2;
		} else if(difficulty.equals("Medium")){
			return 1;
		} else{
			return 0;
		}
	}
	
	/**
	 * Returns true if a premadeSong file exists with the specified file name
	 * @param name the name of the file to look in
	 * @return
	 */
	public static boolean verifyExists(String name){
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(new File(name)));
			if(reader.ready() == false){
				reader.close();
				return false;
			}
			String line = reader.readLine();
			if(isNumeric(line)){
				reader.close();
				return true;
			}
		} catch (IOException e) {
			if(reader != null)
				try {
					reader.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			return false;
		}
		return false;
	}
	public static boolean isNumeric(String str)  
	{  
	  try  
	  {  
	    double d = Double.parseDouble(str);  
	  }  
	  catch(NumberFormatException nfe)  
	  {  
	    return false;  
	  }  
	  return true;  
	}
}
