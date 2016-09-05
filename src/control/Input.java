package control;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Input extends KeyAdapter{

	
	public void keyPressed(KeyEvent evt) {
		if (evt.getKeyChar() == 'a') {
			System.out.println("Check for key characters: " + evt.getKeyChar());
		}
		if (evt.getKeyCode() == KeyEvent.VK_HOME) {
			System.out.println("Check for key codes: " + evt.getKeyCode());
		}
	}
}
