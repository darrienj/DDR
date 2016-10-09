package control;
import java.awt.FlowLayout;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;

import display.*;

public class Main {

	public static void main(String[] args){
		Screen screen = new Screen();
		int oldSelection = 1;
		while(true){
			try{
				String[] selection = selectSong(screen,oldSelection);
				String musicFile = Constants.FILE_BASE + "DDR_Files/Music/"+selection[1] + ".wav";
				String songFile = Constants.FILE_BASE + "DDR_Files/Song/"+selection[2] + "/"+selection[1]+".song";
				int songId = Integer.parseInt(selection[3]);
				Score score = playSong(screen,songId,musicFile,songFile,selection[2],selection[4]);
				showHighScore(screen,score);
				oldSelection = songId;
			} catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	private static void showHighScore(Screen screen,Score score) throws IOException, InterruptedException{
		screen.showScore(score);
		while(screen.submittedScore() == false){
			screen.updateScore();
		}
		score.saveHighScore();
	}
	private static String[] selectSong(Screen screen,int oldSelection) throws IOException{
		screen.selectSong(oldSelection);
		long current = System.currentTimeMillis();
		while(screen.getSelection() == null){
			long newTime = System.currentTimeMillis();
			screen.updateSelection((newTime - current)/1000.0);
			
			current = newTime;
		}
		return screen.getSelection();
	}
	private static Score playSong(Screen screen,int songId, String musicFile,String songFile,String difficulty,String songName){
		try {
			Music music = new Music(musicFile);
			Song song = new Song(songId,songFile,songName,difficulty);
			screen.playSong(song);
			music.play();
			while(music.isFinished() == false){
				screen.updateSong(music.getTime() + song.getOffset());
			}
			Score score = screen.endSong();
			return score;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
