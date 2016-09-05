package control;
import java.awt.image.BufferedImage;


public class Note {
	private String name;
	private int beat;
	private int hold;
	private BufferedImage img;
	private boolean active;
	private boolean canBeActive;
	private boolean miss;
	
	public Note(String name,int beat,int hold,BufferedImage img){
		
		this.name = name;
		this.beat = beat;
		this.hold = hold;
		this.img = img;
		this.active = false;
		this.canBeActive = true;
		this.miss = false;
	}
	public BufferedImage getImage(){
		return img;
	}
	public int getBeat(){
		return beat;
	}
	public int getHold(){
		return hold;
	}
	public String getName(){
		return this.name;
	}
	public void breakHold(){
		this.canBeActive = false;
		this.active = false;
	}
	public boolean isBrokenHold(){
		return !this.canBeActive && !this.active;
	}
	public void setActive(boolean val){
		if(this.canBeActive == true && val == true){
			this.active = true;
			this.canBeActive = false;
		} else if(val == false){
			this.active = false;
		}
	}
	public void miss(){
		this.miss = true;
	}
	public boolean isMiss(){
		return this.miss;
	}
	public boolean isActive(){
		return active;
	}
}

