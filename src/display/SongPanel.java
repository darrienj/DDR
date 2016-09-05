package display;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class SongPanel extends JPanel{
	
	BufferedImage background;
	private int borderWidth;
	private double scaleFactor;
	private String name;
	private boolean selected;
	private String[] data;
	private final static String[] difficultyList = {"Easy","Medium","Hard"};
	private int difficulty = 2;
	private int songId;
	private int[] difficultyData;
	public SongPanel (double scaleFactor,BufferedImage background,int borderWidth,String name,boolean selected,String[] data,int songId){
		System.out.println(data[0] + ", " + data[1] + ", " + data[2] + "," + data[3] + ", " + data[4]);

		this.background = background;
		this.selected = selected;
		this.borderWidth = borderWidth;
		this.data = new String[data.length];
		for(int i = 0;i<data.length;i++){
			this.data[i] = data[i];
		}
		this.setOpaque(false);
		this.name = name;
		this.scaleFactor = scaleFactor;
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = .95;
		c.fill = GridBagConstraints.BOTH;
		this.add(new ActualSongPanel(),c);
		setBorder(BorderFactory.createEmptyBorder(2,0,2,0));
		difficultyData = new int[3];
		difficultyData[0] = Integer.parseInt(data[2]);
		difficultyData[1] = Integer.parseInt(data[3]);
		difficultyData[2] = Integer.parseInt(data[4]);
		this.songId = songId;
		this.data[3] = songId+"";
		this.data[4] = name;
	}
	public int[] getDifficultyData(){
		return this.difficultyData;
	}
	public int getDifficulty(){
		return difficulty;
	}
	public void setDifficulty(int difficulty){
		this.difficulty = difficulty;
	}
	public void increaseDifficulty(){
		int tmp = difficulty + 1;
		if(tmp >= difficultyList.length){
			tmp =  difficultyList.length-1;
		}
		this.difficulty = tmp;
	}
	public void decreaseDifficulty(){
		int tmp = difficulty -1;
		if(tmp <0){
			tmp = 0;
		}
		this.difficulty = tmp;
	}
	public String[] getData(){
		this.data[2] = difficultyList[difficulty];
		return this.data;
	}
	public void updateBackground(BufferedImage background,boolean selected){
		this.selected = selected;
		this.background = background;
		repaint();
	}
	private class ActualSongPanel extends JPanel{
		private ActualSongPanel(){
		
			this.setBorder(new LineBorder(Color.CYAN, borderWidth, true));
			this.setOpaque(false);
			
		}
		@Override
		protected void paintComponent(Graphics g) {
			if(background != null){
				g.drawImage(background, borderWidth, borderWidth, null);
			int fontSize = (int)(20*scaleFactor);
			Font f = new Font("Monaco",Font.PLAIN,fontSize);
			g.setFont(f);
			int fontWidth = g.getFontMetrics().stringWidth(name);
			if(selected){
				g.setColor(Color.DARK_GRAY);
			} else{
				g.setColor(Color.WHITE);
			}
			g.drawString(name, getWidth()/2 - fontWidth/2, getHeight()/2 +6);
			}
		}
	}

	private class MarginPanel extends JPanel{

		public MarginPanel (){
			this.setOpaque(false);
			this.setMaximumSize(new Dimension(5,5));
		}
	}
}
