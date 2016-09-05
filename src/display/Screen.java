package display;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;

import control.Song;
import control.Constants;
public class Screen {

	ScreenFrame frame;
	private String[] songSelection;
	public Screen(){
		try{
			BufferedImage[][] receiveArrows = new BufferedImage[4][];
			for(int i = 0;i<receiveArrows.length;i++){
				receiveArrows[i] = new BufferedImage[7];
			}
			receiveArrows[0][0] = ImageIO.read(new File(Constants.RECEIVE_LEFT_ARROW));
			receiveArrows[1][0] = ImageIO.read(new File(Constants.RECEIVE_DOWN_ARROW));
			receiveArrows[2][0] = ImageIO.read(new File(Constants.RECEIVE_UP_ARROW));
			receiveArrows[3][0] = ImageIO.read(new File(Constants.RECEIVE_RIGHT_ARROW));
			
			receiveArrows[0][1] = ImageIO.read(new File(Constants.RECEIVE_LEFT_ARROW_PRESSED_0));
			receiveArrows[1][1] = ImageIO.read(new File(Constants.RECEIVE_DOWN_ARROW_PRESSED_0));
			receiveArrows[2][1] = ImageIO.read(new File(Constants.RECEIVE_UP_ARROW_PRESSED_0));
			receiveArrows[3][1] = ImageIO.read(new File(Constants.RECEIVE_RIGHT_ARROW_PRESSED_0));
			
			receiveArrows[0][2] = ImageIO.read(new File(Constants.RECEIVE_LEFT_ARROW_PRESSED_1));
			receiveArrows[1][2] = ImageIO.read(new File(Constants.RECEIVE_DOWN_ARROW_PRESSED_1));
			receiveArrows[2][2] = ImageIO.read(new File(Constants.RECEIVE_UP_ARROW_PRESSED_1));
			receiveArrows[3][2] = ImageIO.read(new File(Constants.RECEIVE_RIGHT_ARROW_PRESSED_1));
			
			receiveArrows[0][3] = ImageIO.read(new File(Constants.RECEIVE_LEFT_ARROW_PRESSED_2));
			receiveArrows[1][3] = ImageIO.read(new File(Constants.RECEIVE_DOWN_ARROW_PRESSED_2));
			receiveArrows[2][3] = ImageIO.read(new File(Constants.RECEIVE_UP_ARROW_PRESSED_2));
			receiveArrows[3][3] = ImageIO.read(new File(Constants.RECEIVE_RIGHT_ARROW_PRESSED_2));
			
			receiveArrows[0][4] = ImageIO.read(new File(Constants.RECEIVE_LEFT_ARROW_PRESSED_3));
			receiveArrows[1][4] = ImageIO.read(new File(Constants.RECEIVE_DOWN_ARROW_PRESSED_3));
			receiveArrows[2][4] = ImageIO.read(new File(Constants.RECEIVE_UP_ARROW_PRESSED_3));
			receiveArrows[3][4] = ImageIO.read(new File(Constants.RECEIVE_RIGHT_ARROW_PRESSED_3));
			
			receiveArrows[0][5] = ImageIO.read(new File(Constants.RECEIVE_LEFT_ARROW_PRESSED_4));
			receiveArrows[1][5] = ImageIO.read(new File(Constants.RECEIVE_DOWN_ARROW_PRESSED_4));
			receiveArrows[2][5] = ImageIO.read(new File(Constants.RECEIVE_UP_ARROW_PRESSED_4));
			receiveArrows[3][5] = ImageIO.read(new File(Constants.RECEIVE_RIGHT_ARROW_PRESSED_4));
			
			receiveArrows[0][6] = ImageIO.read(new File(Constants.RECEIVE_LEFT_ARROW_PRESSED_5));
			receiveArrows[1][6] = ImageIO.read(new File(Constants.RECEIVE_DOWN_ARROW_PRESSED_5));
			receiveArrows[2][6] = ImageIO.read(new File(Constants.RECEIVE_UP_ARROW_PRESSED_5));
			receiveArrows[3][6] = ImageIO.read(new File(Constants.RECEIVE_RIGHT_ARROW_PRESSED_5));
			this.frame = new ScreenFrame(receiveArrows);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void selectSong(int oldSelection) throws IOException{
		File file = new File(Constants.FILE_BASE + "DDR_Files/SongList.txt");
		Scanner scanner = new Scanner(file);
	    scanner.useDelimiter(",");
		int count = 0;
		while(scanner.hasNextLine()){
			scanner.nextLine();
			count++;
		}
		String[][] list = new String[count][]; //Name, musicname, easy, medium, hard
		scanner.close();
		scanner = new Scanner(file);
		scanner.useDelimiter(",");
		for(int i = 0;i<list.length;i++){
			list[i] = new String[5]; //Name, musicname, easy, medium, hard
			for(int j = 0;j<list[i].length;j++){
				list[i][j] = scanner.next().replace("\n", "").replace("\r", "").trim();
			}
		}
		scanner.close();
		
		BufferedImage background = ImageIO.read(new File(Constants.SELECT_BACKGROUND));
		this.frame.selectSong(background,list,oldSelection);
	}
	public String[] getSelection(){
		return this.frame.getSongSelection();
	}
	public void playSong(Song song){
		try {
			BufferedImage background  = ImageIO.read(new File("/Users/drichmond/documents/DDR Music/ddr-background.jpg"));
			this.frame.playSong(background, song);
		} catch(Exception e){
			e.printStackTrace();
		}
		
	}
	public void updateSong(double time){
		frame.updateSong(time);
	}
	public void updateSelection(double time){
		frame.updateSelection(time);
	}
	public Score endSong(){
		return frame.endSong();
	}
	public void showScore(Score score) throws IOException{
		frame.showScore(score);
	}
	public void updateScore(){
		frame.updateScore();
	}
	public boolean submittedScore() {
		return frame.submittedScore();
	}
}
