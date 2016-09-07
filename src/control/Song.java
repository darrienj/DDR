package control;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.imageio.ImageIO;


public class Song {

	private String name;
	private Note[][] noteList;
	private BufferedImage quarterNoteImage[][];
	private BufferedImage eighthNoteImage[][];
	private BufferedImage sixteenthNoteImage[][];
	private int bpm;
	private int id;
	private int holdCount = 0;
	private Note[] emptyNoteList;
	double offset;
	private final int BEATS_ON_SCREEN = 14;
	private int noteCount;
	private String readableName;
	private String difficulty;
	public Song(int id, String name, String readableName,String difficulty) throws Exception{
		this.id = id;
		this.name = name;
		this.difficulty = difficulty;
		this.readableName = readableName;
		emptyNoteList = new Note[4];
		loadImages();
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
					this.offset += (60.0/bpm/4)*-1;
				}
			}
			reader.close();
			noteList = new Note[beatCount][4];
			
			reader = new BufferedReader(new FileReader(file));
			reader.readLine(); //get rid of bpm
			beatCount = 1;
			while(reader.ready()){
				String line = reader.readLine();
				if(!line.contains("==================") && !line.contains("*")){
					while(line.length() > 1){
						String type = line.substring(0, 1);
						int duration = Integer.parseInt(line.substring(1,4));
						if(type.equals("L")){
							BufferedImage img = getNoteImage("LEFT",false,beatCount);
							if(duration > 0){
								int endY = (int)(duration*800/BEATS_ON_SCREEN);
								int height = endY+getNoteImage("LEFT",false,beatCount).getHeight();
								img = new BufferedImage(getNoteImage("LEFT",false,beatCount).getWidth(),height,BufferedImage.TYPE_INT_ARGB);
								Graphics g = img.getGraphics();
				    			for(int currentY = 0;currentY<endY;currentY+=800/BEATS_ON_SCREEN/2){
				    				g.drawImage(getNoteImage("LEFT",true,beatCount), 0, currentY, null);
				    			}
				    			g.drawImage(getNoteImage("LEFT",false,beatCount), 0, 0, null);
				    			g.dispose();
							}
								
							Note note = new Note(Note.LEFT,beatCount,duration,img);
							Note emptyNote = new Note(Note.LEFT,beatCount,duration,null);
							noteList[beatCount][0] = new Note(Note.LEFT,beatCount,duration,img);
							note.miss();
							emptyNote.miss();
							for(int i = 1;i<duration;i++){
								if(i%15 == 0 || i == duration -1){
									noteList[beatCount+i][0]=note;
								} else{
									noteList[beatCount+i][0]=emptyNote;
								}
							}
						}
						if(type.equals("D")){
							BufferedImage img = getNoteImage("DOWN",false,beatCount);
							if(duration > 0){
								int endY = (int)(duration*800/BEATS_ON_SCREEN);
								int height = endY+getNoteImage("DOWN",false,beatCount).getHeight();
								img = new BufferedImage(getNoteImage("DOWN",false,beatCount).getWidth(),height,BufferedImage.TYPE_INT_ARGB);
								Graphics g = img.getGraphics();
				    			for(int currentY = 0;currentY<endY;currentY+=800/BEATS_ON_SCREEN/2){
				    				g.drawImage(getNoteImage("DOWN",true,beatCount), 0, currentY, null);
				    			}
				    			g.drawImage(getNoteImage("DOWN",false,beatCount), 0, 0, null);
				    			g.dispose();
							}
							
							Note note = new Note(Note.DOWN,beatCount,duration,img);
							Note emptyNote = new Note(Note.DOWN,beatCount,duration,null);
							noteList[beatCount][1] = new Note(Note.DOWN,beatCount,duration,img);
							note.miss();
							emptyNote.miss();
							for(int i = 0;i<duration;i++){
								if(i%15 == 0 || i == duration -1){
									noteList[beatCount+i][1]=note;
								} else{
									noteList[beatCount+i][0]=emptyNote;
								}
							}
						}
						if(type.equals("U")){
							BufferedImage img = getNoteImage("UP",false,beatCount);
							if(duration > 0){
								int endY = (int)(duration*800/BEATS_ON_SCREEN);
								int height = endY+getNoteImage("UP",false,beatCount).getHeight();
								img = new BufferedImage(getNoteImage("UP",false,beatCount).getWidth(),height,BufferedImage.TYPE_INT_ARGB);
								Graphics g = img.getGraphics();
				    			for(int currentY = 0;currentY<endY;currentY+=800/BEATS_ON_SCREEN/2){
				    				g.drawImage(getNoteImage("UP",true,beatCount), 0, currentY, null);
				    			}
				    			g.drawImage(getNoteImage("UP",false,beatCount), 0, 0, null);
				    			g.dispose();
							}
							
							
							Note note = new Note(Note.UP,beatCount,duration,img);
							Note emptyNote = new Note(Note.UP,beatCount,duration,null);
							noteList[beatCount][2] = new Note(Note.UP,beatCount,duration,img);
							note.miss();
							emptyNote.miss();
							for(int i = 0;i<duration;i++){
								if(i%15 == 0 || i == duration -1){
									noteList[beatCount+i][2]=note;
								} else{
									noteList[beatCount + i][2] = emptyNote;
								}
							}
						}
						if(type.equals("R")){
							BufferedImage img = getNoteImage("RIGHT",false,beatCount);
							if(duration > 0){
								int endY = (int)(duration*800/BEATS_ON_SCREEN);
								int height = endY+getNoteImage("RIGHT",false,beatCount).getHeight();
								img = new BufferedImage(getNoteImage("RIGHT",false,beatCount).getWidth(),height,BufferedImage.TYPE_INT_ARGB);
								Graphics g = img.getGraphics();
				    			for(int currentY = 0;currentY<endY;currentY+=800/BEATS_ON_SCREEN/2){
				    				g.drawImage(getNoteImage("RIGHT",true,beatCount), 0, currentY, null);
				    			}
				    			g.drawImage(getNoteImage("RIGHT",false,beatCount), 0, 0, null);
				    			g.dispose();
							}
							
							
							Note note = new Note(Note.RIGHT,beatCount,duration,img);
							Note emptyNote = new Note(Note.RIGHT,beatCount,duration,null);
							noteList[beatCount][3] = new Note(Note.RIGHT,beatCount,duration,img);
							note.miss();
							emptyNote.miss();
							for(int i = 0;i<duration;i++){
								if(i%15 == 0 || i == duration -1){
									noteList[beatCount+i][3]=note;
								} else{
									noteList[beatCount + i][2] = emptyNote;
								}
							}
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
					this.holdCount += noteList[i][j].getHold();
					this.noteCount++;
				}
			}
		}
		
	}
	private void loadImages() throws IOException{
		BufferedImage right = ImageIO.read(new File(Constants.RIGHT_QUARTER_ARROW));
		BufferedImage left = ImageIO.read(new File(Constants.LEFT_QUARTER_ARROW));
		BufferedImage up = ImageIO.read(new File(Constants.UP_QUARTER_ARROW));
		BufferedImage down = ImageIO.read(new File(Constants.DOWN_QUARTER_ARROW));
		BufferedImage holdRight = ImageIO.read(new File(Constants.RIGHT_QUARTER_HOLD_ARROW));
		BufferedImage holdLeft = ImageIO.read(new File(Constants.LEFT_QUARTER_HOLD_ARROW));
		BufferedImage holdUp = ImageIO.read(new File(Constants.UP_QUARTER_HOLD_ARROW));
		BufferedImage holdDown = ImageIO.read(new File(Constants.DOWN_QUARTER_HOLD_ARROW));
		quarterNoteImage = new BufferedImage[4][];
		for(int i = 0;i<quarterNoteImage.length;i++){
			quarterNoteImage[i] = new BufferedImage[2];
		}
		quarterNoteImage[0][0] = left;
		quarterNoteImage[0][1] = holdLeft;
		quarterNoteImage[1][0] = down;
		quarterNoteImage[1][1] = holdDown;
		quarterNoteImage[2][0] = up;
		quarterNoteImage[2][1] = holdUp;
		quarterNoteImage[3][0] = right;
		quarterNoteImage[3][1] = holdRight;
		
		
		
		BufferedImage rightE = ImageIO.read(new File(Constants.RIGHT_EIGHTH_ARROW));
		BufferedImage leftE = ImageIO.read(new File(Constants.LEFT_EIGHTH_ARROW));
		BufferedImage upE = ImageIO.read(new File(Constants.UP_EIGHTH_ARROW));
		BufferedImage downE = ImageIO.read(new File(Constants.DOWN_EIGHTH_ARROW));
		BufferedImage holdRightE = ImageIO.read(new File(Constants.RIGHT_EIGHTH_HOLD_ARROW));
		BufferedImage holdLeftE = ImageIO.read(new File(Constants.LEFT_EIGHTH_HOLD_ARROW));
		BufferedImage holdUpE = ImageIO.read(new File(Constants.UP_EIGHTH_HOLD_ARROW));
		BufferedImage holdDownE = ImageIO.read(new File(Constants.DOWN_EIGHTH_HOLD_ARROW));
		eighthNoteImage = new BufferedImage[4][];
		for(int i = 0;i<eighthNoteImage.length;i++){
			eighthNoteImage[i] = new BufferedImage[2];
		}
		eighthNoteImage[0][0] = leftE;
		eighthNoteImage[0][1] = holdLeftE;
		eighthNoteImage[1][0] = downE;
		eighthNoteImage[1][1] = holdDownE;
		eighthNoteImage[2][0] = upE;
		eighthNoteImage[2][1] = holdUpE;
		eighthNoteImage[3][0] = rightE;
		eighthNoteImage[3][1] = holdRightE;
		
		
		
		BufferedImage rightS = ImageIO.read(new File(Constants.RIGHT_SIXTEENTH_ARROW));
		BufferedImage leftS = ImageIO.read(new File(Constants.LEFT_SIXTEENTH_ARROW));
		BufferedImage upS = ImageIO.read(new File(Constants.UP_SIXTEENTH_ARROW));
		BufferedImage downS = ImageIO.read(new File(Constants.DOWN_SIXTEENTH_ARROW));
		BufferedImage holdRightS = ImageIO.read(new File(Constants.RIGHT_SIXTEENTH_HOLD_ARROW));
		BufferedImage holdLeftS = ImageIO.read(new File(Constants.LEFT_SIXTEENTH_HOLD_ARROW));
		BufferedImage holdUpS = ImageIO.read(new File(Constants.UP_SIXTEENTH_HOLD_ARROW));
		BufferedImage holdDownS = ImageIO.read(new File(Constants.DOWN_SIXTEENTH_HOLD_ARROW));
		sixteenthNoteImage = new BufferedImage[4][];
		for(int i = 0;i<sixteenthNoteImage.length;i++){
			sixteenthNoteImage[i] = new BufferedImage[2];
		}
		sixteenthNoteImage[0][0] = leftS;
		sixteenthNoteImage[0][1] = holdLeftS;
		sixteenthNoteImage[1][0] = downS;
		sixteenthNoteImage[1][1] = holdDownS;
		sixteenthNoteImage[2][0] = upS;
		sixteenthNoteImage[2][1] = holdUpS;
		sixteenthNoteImage[3][0] = rightS;
		sixteenthNoteImage[3][1] = holdRightS;
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
	
	public void activateNote(int beat, int index){
		if(noteList[beat][index] != null){
			noteList[beat][index].setActive(true);
			if(noteList[beat][index].getHold() > 0){
				for(int i = beat;i<= beat + noteList[beat][index].getHold();i++){
					if(noteList[i][index] != null){
						noteList[i][index].setActive(true);
					}
				}
			}
		}
	}
	public void missNote(int beat,int direction){
		if(noteList[beat][direction] != null){
			noteList[beat][direction].miss();
		}
	}
	private BufferedImage getNoteImage(int index,boolean isHold,int beat){
		if(index == 0){
			return getNoteImage("LEFT",isHold,beat);
		} else if(index == 1){
			return getNoteImage("DOWN",isHold,beat);
		} else if(index == 2){
			return getNoteImage("UP",isHold,beat);
		} else{
			return getNoteImage("RIGHT",isHold,beat);
		} 
	}
	private BufferedImage getNoteImage(String name,boolean isHold,int beat){
		int holdIndex = isHold? 1 : 0;
		int direction = 0;
		beat-=1;
		if(name.equals("LEFT")){
			direction = 0;
		} else if(name.equals("DOWN")){
			direction = 1;
		} else if(name.equals("UP")){
			direction = 2;
		} else{
			direction = 3;
		}
		if(beat%4 == 0){
			return quarterNoteImage[direction][holdIndex];
		} else if(beat%2 == 0){
			return eighthNoteImage[direction][holdIndex];
		} else {
			return sixteenthNoteImage[direction][holdIndex];
		}
	}
	public void updateHold(int beat,int newBeat,int hold,int index){
		int oldHold = 1;
		if(noteList[beat][index].getHold() > 0){
			oldHold = noteList[beat][index].getHold();
			BufferedImage img = getNoteImage(index,false,beat);
			if(hold > 0){
				int endY = (int)(hold*800/BEATS_ON_SCREEN);
				int height = endY+getNoteImage(index,false,beat).getHeight();
				img = new BufferedImage(getNoteImage(index,false,beat).getWidth(),height,BufferedImage.TYPE_INT_ARGB);
				Graphics g = img.getGraphics();
    			for(int currentY = 0;currentY<endY;currentY+=800/BEATS_ON_SCREEN/2){
    				g.drawImage(getNoteImage(index,true,beat), 0, currentY, null);
    			}
    			g.drawImage(getNoteImage(index,false,beat), 0, 0, null);
    			g.dispose();
			}
			for(int i = beat;i<oldHold+beat;i++){
				if(i%15 == 0 || i == hold+beat){
					Note note = new Note(noteList[beat][index].getDirection(),newBeat,hold,img);
					note.breakHold();
					noteList[i][index] = note;
				} else if(noteList[i][index] != null){
					Note note = new Note(noteList[beat][index].getDirection(),newBeat,hold,img);
					note.breakHold();
					noteList[i][index] = note;
				} else{
					Note note = new Note(noteList[beat][index].getDirection(),newBeat,hold,null);
					note.breakHold();
					noteList[i][index] = note;
				}
			}
		}
	}
	public String getSongName(){
		return readableName;
	}
	public double getOffset(){
		return this.offset;
	}
	public int getBpm(){
		return this.bpm;
	}
	public int getNoteCount(){
		return this.noteCount;
	}
	public int getHoldCount(){
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
}
