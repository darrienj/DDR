package control;
import java.applet.AudioClip;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
/**
 * 
 * @author drichmond
 * This class represents the music being played.
 * Can tell the length of the song, 
 */
public class Music implements LineListener{
	
	Clip clip;
	boolean isDone;
	File audioFile;
	public Music(String fileName){
		this.audioFile = new File(fileName);
		
	}
	
	public void play() throws LineUnavailableException, UnsupportedAudioFileException, IOException{
		AudioInputStream audioIn = AudioSystem.getAudioInputStream(audioFile.getAbsoluteFile());
		clip = AudioSystem.getClip();
		clip.addLineListener(this);
		clip.open(audioIn);
		isDone = false;
		clip.start();
	}
	public void stop(){
		clip.stop();
		clip.close();
	}
	public boolean isFinished(){
		return isDone;
	}
	public void update(LineEvent le) {
	    LineEvent.Type type = le.getType();
	    if (type == LineEvent.Type.OPEN) {
	      
	    } else if (type == LineEvent.Type.CLOSE) {
	      isDone = true;
	    } else if (type == LineEvent.Type.START) {
	      
	    } else if (type == LineEvent.Type.STOP) {
	      isDone = true;
	      clip.close();
	    }
	 }
	public double getTime(){
		return clip.getMicrosecondPosition()/1000000.0;
	}
}
