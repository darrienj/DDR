package control;
import java.awt.FlowLayout;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;

import display.*;

public class Main {

	private static Music music;
	
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
			music = new Music(musicFile);
			Song song = null;
			DanceChartBuilder builder = null;
			if(PremadeSong.verifyExists(songFile)){
				song = new PremadeSong(songId,songFile,songName,difficulty);
				PremadeDanceChartBuilder tmpBuilder = new PremadeDanceChartBuilder();
				tmpBuilder.setSong((PremadeSong)(song));
				tmpBuilder.setTimePerBeat(DancePanel.MILLISECONDS_PER_BEAT);
				builder = tmpBuilder;
			} else{
				song = new CustomSong(songId, songFile.substring(0, songFile.length()-5)+".csong",difficulty,songName);
				((CustomSong)song).build();
				CustomDanceChartBuilder tmpBuilder = new CustomDanceChartBuilder();
				tmpBuilder.setSong((CustomSong)(song));
				builder = tmpBuilder;
			}			
			builder.setTimeOnScreen(DancePanel.MILLISECONDS_ON_SCREEN);
			screen.playSong(song,builder);
			music.play();
			
			while(music.isFinished() == false){
				screen.updateSong(Math.max(0, music.getTime() + song.getOffset()/1000.0));
			}
			Score score = screen.endSong();
			return score;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	public static void endSong(){
		if(music != null){
			music.stop();
		}
	}
}
