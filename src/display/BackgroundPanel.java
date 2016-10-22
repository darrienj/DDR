package display;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JLabel;
import javax.swing.JPanel;

import control.*;

public class BackgroundPanel extends JPanel{

	Image background;
	Dimension scaleFactor;
	private DancePanel dancePanel;
	private DancePanelInput dancePanelInput;
	//laptop screen dimensions are 1280 x 800
	public BackgroundPanel(Dimension dimension,BufferedImage background,ReceiveArrow receiveArrows,DanceChart danceChart,Score score){
		super();
		this.setLayout(new GridBagLayout());
		scaleFactor = new Dimension();
		scaleFactor.setSize(dimension.getWidth()/1280,dimension.getHeight()/1280);
		if(background == null)
			this.setBackground(Color.BLACK);
		else{
			Image scaledBackground = background.getScaledInstance((int)(dimension.getWidth()), (int)(dimension.getHeight()), BufferedImage.SCALE_SMOOTH);
			
			BufferedImage newBackground = new BufferedImage(scaledBackground.getWidth(null), scaledBackground.getHeight(null), BufferedImage.TYPE_INT_ARGB);
			Graphics g = newBackground.getGraphics();
			g.drawImage(scaledBackground, 0, 0, null);
			
			
			this.background = newBackground;
			g.dispose();
			GridBagConstraints c = new GridBagConstraints();
			this.dancePanel = new DancePanel(scaleFactor,receiveArrows,danceChart,score,newBackground);
			dancePanelInput = new DancePanelInput(dancePanel);
			this.addKeyListener(dancePanelInput);
			this.setFocusable(true);
			c.fill = GridBagConstraints.BOTH;
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = .45;
			c.weighty = 1;
			this.add(this.dancePanel,c);
			c.gridx = 1;
			c.weightx = .55;
			this.add(new TransparentPanel(),c);
		}
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
	public void updateSong(double time){
		this.requestFocus();
		this.dancePanel.update(time);
		this.dancePanelInput.initialize();
	}
	public Score getScore(){
		return this.dancePanel.getScore();
	}
}
