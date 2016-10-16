package display;

public interface InputReceiver {

	public void pressLeft();
	public void pressRight();
	public void pressUp();
	public void pressDown();
	
	public void releaseLeft();
	public void releaseRight();
	public void releaseUp();
	public void releaseDown();
	
	public void pressEnter();
	public void pressEscape();
	public void releaseEnter();
}
