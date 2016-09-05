package display;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

public class SelectorBackgroundPanel extends JPanel implements InputReceiver{

	BufferedImage background;
	Dimension scaleFactor; //laptop screen dimensions are 1280 x 800
	SongSelectorPanel songSelectorPanel;
	private JScrollBar vertical;
	double scrollOff;
	double SCROLL_SPEED = 300;
	private int oldSelection = 0;
	private int finalSelection = -1;
	private DifficultyPanel difficultyPanel;
	private HighScorePanel highScorePanel;
	private int startingOldSelection;
	private boolean hasStarted;
	public SelectorBackgroundPanel(Dimension screenSize, BufferedImage background,String[][] songList,int startingOldSelection){
		super();
		this.hasStarted = false;
		this.startingOldSelection = startingOldSelection;
		this.background = background;
		this.setLayout(new GridBagLayout());
		scaleFactor = new Dimension();
		scaleFactor.setSize(screenSize.getWidth()/1280,screenSize.getHeight()/1280);
		if(background == null)
			this.setBackground(Color.BLACK);
		else{
			Image scaledBackground = background.getScaledInstance((int)(screenSize.getWidth()), (int)(screenSize.getHeight()), BufferedImage.SCALE_SMOOTH);
			
			BufferedImage newBackground = new BufferedImage(scaledBackground.getWidth(null), scaledBackground.getHeight(null), BufferedImage.TYPE_INT_ARGB);
			Graphics g = newBackground.getGraphics();
			g.drawImage(scaledBackground, 0, 0, null);
			
			
			this.background = newBackground;
			g.dispose();
			GridBagConstraints c = new GridBagConstraints();
			this.songSelectorPanel = new SongSelectorPanel(scaleFactor,songList,this);
			JScrollPane scrollPane = new JScrollPane(this.songSelectorPanel);
			scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
			this.vertical = scrollPane.getVerticalScrollBar();
			this.vertical.setUnitIncrement(1);
			scrollPane.setOpaque(false);
			scrollPane.getViewport().setOpaque(false);
			scrollPane.setBorder(null);
			vertical.setVisible(false);
			vertical.setOpaque(false);
			c.fill = GridBagConstraints.BOTH;
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = .4;
			c.weighty = 1;
			c.gridheight = 2;
			this.add(scrollPane,c);
			c.gridx = 1;
			c.weightx = .1;
			c.gridheight = 2;
			this.add(new TransparentPanel(),c);
			c.gridx = 2;
			c.weightx = .5;
			c.gridheight = 1;
			c.weighty = .6;
			highScorePanel = new HighScorePanel(scaleFactor);
			JPanel tmp = new JPanel();
			tmp.setLayout(new GridBagLayout());
			tmp.setOpaque(false);
			tmp.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));
			tmp.add(highScorePanel,c);
			this.add(tmp,c);
			
			c.gridy =1;
			c.weighty = .4;
			difficultyPanel = new DifficultyPanel(scaleFactor);
			tmp = new JPanel();
			tmp.setLayout(new GridBagLayout());
			tmp.setOpaque(false);
			tmp.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));
			tmp.add(difficultyPanel,c);
			this.add(tmp,c);
			
			
			
		}
	}
	/**
	 * called ever frame, used to make a smooth transition in the panel when selecting songs
	 * @param deltaTime
	 */
	public void update(double deltaTime){
		int selection = songSelectorPanel.getSelection();
		int increment = songSelectorPanel.getHeight()/songSelectorPanel.TOTAL_SONGS_ON_SCREEN;
		int height = songSelectorPanel.getHeight();
		int goal = selection * increment - (increment*SongSelectorPanel.SONGS_ON_SCREEN/2);
		int difference = goal - this.vertical.getValue();
		if(oldSelection == songSelectorPanel.SMALLEST_SELECT || oldSelection == songSelectorPanel.BIGGEST_SELECT){
			//jump to the new scroll;
			vertical.setValue(goal);
		} else if(Math.abs(difference) > 5){
			//System.out.println(difference);
			scrollOff += Math.signum(difference)*SCROLL_SPEED*deltaTime;
			int newValue = (int)(scrollOff) + vertical.getValue();
			vertical.setValue(newValue);
			scrollOff = scrollOff - (int)(scrollOff);
		}
		if(hasStarted == false){
			hasStarted = true;
			songSelectorPanel.setSelection(startingOldSelection);
		}
		songSelectorPanel.update(0);
	}
	public String[] getSongSelection(){
		return songSelectorPanel.getSong(finalSelection);
	}
	@Override
	protected void paintComponent(Graphics g) {

	    super.paintComponent(g);
	    if(background != null)
	        g.drawImage(background, 0, 0, null);
	}
	
	private class TransparentPanel extends JPanel {
	    {
	        setOpaque(false);
	    }
	    public void paintComponent(Graphics g) {

	    }
	}

	private void updateDifficulty(){
		this.difficultyPanel.update(songSelectorPanel.getSongDifficulty(),0,0,songSelectorPanel.getDifficulty());
	}
	@Override
	public void pressLeft() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void pressRight() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void pressUp() {
		int oldDifficulty = this.songSelectorPanel.getDifficulty();
		this.oldSelection = this.songSelectorPanel.update(-1);
		this.songSelectorPanel.setDifficulty(oldDifficulty);
		this.highScorePanel.update(this.songSelectorPanel.getSelectionId());
		updateDifficulty();
	}
	@Override
	public void pressDown() {
		int oldDifficulty = this.songSelectorPanel.getDifficulty();
		this.oldSelection = this.songSelectorPanel.update(1);
		this.songSelectorPanel.setDifficulty(oldDifficulty);
		this.highScorePanel.update(this.songSelectorPanel.getSelectionId());
		updateDifficulty();
	}
	@Override
	public void pressEnter(){
		int oldDifficulty = this.songSelectorPanel.getDifficulty();
		this.finalSelection = songSelectorPanel.getSelection();
		this.songSelectorPanel.setDifficulty(oldDifficulty);
	}
	@Override
	public void releaseLeft() {
		this.songSelectorPanel.decreaseDifficulty();
		updateDifficulty();
	}
	@Override
	public void releaseRight() {
		this.songSelectorPanel.increaseDifficulty();
		updateDifficulty();
	}
	@Override
	public void releaseUp() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void releaseDown() {
		// TODO Auto-generated method stub
		
	}
	public void releaseEnter(){
		
	}
}
