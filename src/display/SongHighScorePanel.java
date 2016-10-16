package display;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import control.Constants;
import control.Score;

public class SongHighScorePanel extends JPanel  implements InputReceiver{

	Score[] scores;
	Score userScore;
	private BufferedImage background;
	Dimension scaleFactor;
	boolean submitted = false;
	BufferedImage grade;
	private NamePanel namePanel;
	private boolean isHighScore;
	public SongHighScorePanel(Dimension screenSize, Score userScore, Score[] scores) throws IOException{
		this.scores = scores;
		this.userScore = userScore;
		this.scaleFactor = new Dimension((int)(screenSize.getWidth()/1280),(int)(screenSize.getHeight()/800));
		this.submitted = false;
		this.grade = userScore.getGrade();
		background = ImageIO.read(new File(Constants.SELECT_GRADIENT));
		Image tmp = background.getScaledInstance((int)(scaleFactor.getWidth()*1280), (int)(scaleFactor.getHeight()*800), BufferedImage.SCALE_SMOOTH);
		background = new BufferedImage(tmp.getWidth(null),tmp.getHeight(null),BufferedImage.TYPE_INT_ARGB);
		Graphics g = background.getGraphics();
		g.drawImage(tmp, 0, 0, tmp.getWidth(null), tmp.getHeight(null),null);
		g.dispose();
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.weightx = 1;
		c.weighty = .2;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		c.gridheight = 1;
		c.fill = GridBagConstraints.BOTH;
		this.add(new GradePanel(), c);
		c.gridy = 1;
		c.gridx = 0;
		c.gridwidth = 1;
		c.weighty = .8;
		this.add(new DescriptionPanel(), c);
		c.gridx=1;
		namePanel = new NamePanel();
		this.add(namePanel, c);
		this.setFocusable(true);
		this.addKeyListener(new DancePanelInput(this));
		
		isHighScore = false;
		for(int i = 0;i<scores.length;i++){
			if(scores[i].getScore() < userScore.getScore()){
				isHighScore = true;
			}
		}
		if(!isHighScore){
			namePanel.setVisible(false);
		}
	}
	@Override
	protected void paintComponent(Graphics g) {
		if(background != null){
			g.drawImage(background, 0, 0, null);
		}
	}
	public boolean submittedScore(){
		this.requestFocus();
		String name = namePanel.getName();
		if( name != null){
			userScore.setName(name);
			this.submitted = true;
		}
		return this.submitted;
	}
	public void update(){
		namePanel.update();
	}
	
//////GRADE PANEL CLASS//////
	private class GradePanel extends JPanel{
		BufferedImage grade;
		String songName;
		public GradePanel(){
			grade = userScore.getGrade();
			this.setOpaque(false);
			this.songName = userScore.getSongName();
			
		}
		protected void paintComponent(Graphics g) {
			int x = (int)(this.getWidth()*.1);
			g.drawImage(grade,x,(int)(this.getHeight()*.05),null);
			g.setColor(new Color(240,240,10));
			int fontSize = (int)(100*scaleFactor.getHeight());
			Font f = new Font("Monaco",Font.PLAIN,fontSize);
			g.setFont(f);
			int stringWidth = g.getFontMetrics().stringWidth(songName);
			if(stringWidth + this.getWidth()*.15+ grade.getWidth() > getWidth()){
				fontSize = (int)(50*scaleFactor.getHeight());
				f = new Font("Monaco",Font.PLAIN,fontSize);
				g.setFont(f);
				String first = songName.substring(0, songName.length()/2)+"-";
				String second = songName.substring(songName.length()/2, songName.length());
				g.drawString(first,(int)(this.getWidth()*.15+ grade.getWidth()) , (int)(this.getHeight()*.05 +fontSize));
				g.drawString(second,(int)(this.getWidth()*.15+ grade.getWidth()) , (int)(this.getHeight()*.05 +fontSize+fontSize));
			} else{
				g.drawString(songName,(int)(this.getWidth()*.15+ grade.getWidth()) , (int)(this.getHeight()*.05 +fontSize));
			}
			
			g.setColor(new Color(60,50,150));
			g.fillRect(x, (int)(this.getHeight()*.05 + grade.getHeight()), this.getWidth()-x, 15);
		}
	}

	//////DESCRIPTION PANEL CLASS//////
	private class DescriptionPanel extends JPanel{
		BufferedImage descriptionBackground;
		private String[] descriptionCount;
		private String[] descriptionName;
		private String finalScore;
		private String maxCombo;
		private Color lightBlue;
		private Color blue;
		private Color turquoise;
		private Color yellow;
		private Color red;
		public DescriptionPanel() throws IOException{
			int[] count = userScore.getDescriptionCount();
			this.descriptionCount = new String[count.length];
			for(int i = 0;i<count.length;i++){
				descriptionCount[i] = String.format("%04d", (int)count[i]);
			}
			this.maxCombo = userScore.getMaxCombo()+"";
			this.finalScore = userScore.getFormatedScore();
			this.lightBlue = new Color(230,230,255);
			this.blue = new Color(150,140,255);
			this.turquoise = new Color(180,240,230);
			this.yellow = new Color(250,250,110);
			this.red = new Color(255,100,100);
			this.descriptionName = userScore.getDescriptionNames();
			//this.setBackground(Color.BLUE);
			this.setOpaque(false);
			int width = (int)(scaleFactor.getWidth()*1280*.5);
			int height = (int)(scaleFactor.getHeight()*800*.8);
			descriptionBackground = ImageIO.read(new File(Constants.DESCRIPTION_BACKGROUND));
			Image tmp = descriptionBackground.getScaledInstance((int)(width*.5), (int)(height/(descriptionCount.length+5)), BufferedImage.SCALE_SMOOTH);
			descriptionBackground = new BufferedImage(tmp.getWidth(null),tmp.getHeight(null),BufferedImage.TYPE_INT_ARGB);
			Graphics g = descriptionBackground.getGraphics();
			g.drawImage(tmp, 0, 0, tmp.getWidth(null), tmp.getHeight(null),null);
			g.dispose();
		}
		
		protected void paintComponent(Graphics g) {
			int fontSize = (int)(40*scaleFactor.getHeight());
			Font f = new Font("Monaco",Font.PLAIN,fontSize);
			g.setFont(f);
			
			int stringWidth = g.getFontMetrics().stringWidth(finalScore);
			for(int i = 0;i<descriptionCount.length;i++){
				int y = (int)(getHeight()*.1 + descriptionBackground.getHeight()*i);
				int x = (int)(.1*getWidth());
				g.setColor(turquoise);
				g.drawString(descriptionCount[i], x+10, y + fontSize);
				x += (int)(stringWidth + getWidth()*.05);
				g.drawImage(descriptionBackground, x, y, null);
				g.setColor(lightBlue);
				g.drawString(descriptionName[i], x+10, y + fontSize);
			}
			int y = (int)(getHeight()*.1 + descriptionBackground.getHeight()*descriptionCount.length+10);
			int x = (int)(.1*getWidth()+10);
			g.setColor(turquoise);
			g.drawString(maxCombo,x,y+fontSize);
			x += (int)(stringWidth + getWidth()*.05);
			g.setColor(blue);
			fontSize = (int)(20*scaleFactor.getHeight());
			f = new Font("Monaco",Font.PLAIN,fontSize);
			g.setFont(f);
			g.drawString("MAX", x, y+fontSize);
			g.drawString("COMBO", x, y+fontSize+fontSize);
			
			g.setColor(yellow);
			fontSize = (int)(40*scaleFactor.getHeight());
			f = new Font("Monaco",Font.PLAIN,fontSize);
			g.setFont(f);
			y = (int)(getHeight()*.1 + descriptionBackground.getHeight()*(descriptionCount.length+1));
			x = (int)(.1*getWidth()+10);
			g.drawString(finalScore, x, y+fontSize);
			g.setColor(red);
			x += (int)(stringWidth + getWidth()*.05);
			g.drawString("SCORE", x, y+fontSize);
		}
	}
	
	//////NAME PANEL CLASS//////
	private class NamePanel extends JPanel{
		String name = "";
		String[] abc = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z", "_"};
		String[] oldName;
		private final int MAX_LENGTH = 10;
		private Color yellow;
		private Color lightBlue;
		private Color red;
		private int activeIndex;
		private boolean left;
		private boolean right;
		private boolean up;
		private boolean down;
		private boolean enter;
		private final int ROW_LENGTH = 8;
		boolean activeBlink = true;
		long oldTime = System.currentTimeMillis();
		private final long BLINK_TIME = 300;
		private boolean finished = false;
		private String[] nameList;
		public NamePanel() throws IOException{
			this.yellow = new Color(250,250,110);
			this.lightBlue = new Color(200,200,255);
			this.red = new Color(255,100,100);
			this.nameList = Score.loadNames();
			//this.setBackground(Color.GREEN);
			this.setOpaque(false);
		}
		public void update(){
			if(System.currentTimeMillis() - oldTime > BLINK_TIME){
				activeBlink = !activeBlink;
				oldTime = System.currentTimeMillis();
				this.repaint();
			}
			if(left){
				activeIndex -=1;
			}
			if(right){
				activeIndex +=1;
			}
			if(up){
				activeIndex -=ROW_LENGTH;
			}
			if(down){
				activeIndex += ROW_LENGTH;
			}
			if(up || down || left || right){
				this.repaint();
				up=false;
				down=false;
				left=false;
				right=false;
			}
			activeIndex = activeIndex > (abc.length+1) ? abc.length+1: activeIndex; 
			activeIndex = activeIndex < -1*nameList.length ? -1*nameList.length : activeIndex;
			if(enter){
				if(name.length() < MAX_LENGTH && activeIndex < abc.length && activeIndex >= 0){
					this.repaint();
					name+=abc[activeIndex];
				} else if(activeIndex == abc.length){
					name = "";
				} else if(activeIndex == abc.length+1){
					finished = true;
				} else if(activeIndex < 0){
					int index = activeIndex + nameList.length;
					name = nameList[index];
				}
				enter = false;
			}
			
		}
		public String getName(){
			if(this.finished){
				return this.name;
			} else{
				return null;
			}
		}
		public void pressLeft(){
			this.left = true;
		}
		public void pressRight(){
			this.right = true;
		}
		public void pressUp(){
			this.up = true;
		}
		public void pressDown(){
			this.down = true;
		}
		public void pressEnter(){
			this.enter = true;
		}
		protected void paintComponent(Graphics g) {
			int fontSize = (int)(getHeight()*.08*scaleFactor.getHeight());
			int x = (int)(getWidth()*.1);
			int y = (int)(getHeight()*.1)+fontSize;
			int margin = (int)(getHeight()*.03);
			Font f = new Font("Monospaced",Font.PLAIN,fontSize);
			g.setFont(f);
			int characterWidth = g.getFontMetrics(f).stringWidth(abc[0]) + (int)(getWidth()*.01);
			g.setColor(yellow);
			for(int i = 0;i<name.length();i++){
				g.drawString(name.charAt(i)+"", x, y);
				x+=characterWidth;
			}
			x = (int)(getWidth()*.1);
			for(int i = 0;i<MAX_LENGTH;i++){
				if(i >= name.length()){
					g.drawString("_", x, y);
				} else{
					//do nothing.  name is already drawn here
				}
				x+=characterWidth;
			}
			x = (int)(getWidth()*.1);
			y += fontSize + margin+margin;
			int miniFontSize = (int)(getHeight()*.05*scaleFactor.getHeight());
			f = new Font("Monospaced",Font.PLAIN,miniFontSize);
			g.setFont(f);
			int miniWidth = g.getFontMetrics(f).stringWidth("abcdefghij") + (int)(getWidth()*.02);
			int nameListIndex = activeIndex + nameList.length;
			for(int i = 0;i<nameList.length;i++){
				if(nameListIndex == i){
					g.setColor(red);
					if(activeBlink){
						g.drawString(nameList[i], x, y);
					}
				} else{
					g.setColor(lightBlue);
					g.drawString(nameList[i], x, y);
				}
				x+=miniWidth;
				if(i == 2){
					x = (int)(getWidth()*.1);
					y+=miniFontSize;
				}
			}
			f = new Font("Monospaced",Font.PLAIN,fontSize);
			g.setFont(f);
			y+= miniFontSize + margin;
			for(int i = 0;i<4;i++){
				x = (int)(getWidth()*.1);
				for(int j = 0;j<ROW_LENGTH;j++){
					int index = i*ROW_LENGTH+j;
					if(index < abc.length){
						if(index == activeIndex){
							if(activeBlink){
								g.setColor(red);
								g.drawString(abc[index], x, y);
							}
						} else{
							g.setColor(lightBlue);
							g.drawString(abc[index], x, y);
						}
					}
					x+=characterWidth;
				}
				y+=margin+fontSize;
			}
			
			x = (int)(getWidth()*.1);
			g.setColor(lightBlue);
			if(abc.length == activeIndex){
				if(activeBlink){
					g.setColor(red);
					g.drawString("CLEAR", x, y);
				}				
			} else{
				g.setColor(lightBlue);
				g.drawString("CLEAR", x, y);
			}
			x += g.getFontMetrics(f).stringWidth("CLEAR") + (int)(getWidth()*.02);
			if(abc.length +1 == activeIndex){
				if(activeBlink){
					g.setColor(red);
					g.drawString("DONE", x, y);
				}				
			} else{
				g.setColor(lightBlue);
				g.drawString("DONE", x, y);
			}
			
		}
		
	}

	@Override
	public void pressLeft() {
		namePanel.pressLeft();
		
	}
	@Override
	public void pressRight() {
		namePanel.pressRight();
		
	}
	@Override
	public void pressUp() {
		namePanel.pressUp();
		
	}
	@Override
	public void pressDown() {
		namePanel.pressDown();
		
	}
	@Override
	public void releaseLeft() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void releaseRight() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void releaseUp() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void releaseDown() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void pressEnter() {
		if(isHighScore){
			namePanel.pressEnter();
		} else{
			this.submitted = true;
		}
		
	}
	@Override
	public void releaseEnter() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void pressEscape(){
		
	}
	
}
