package display;

import java.awt.event.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;

import gnu.io.CommPortIdentifier; 
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent; 
import gnu.io.SerialPortEventListener; 

import java.util.Enumeration;
import java.util.Set;
import java.util.TreeSet;

public class DancePanelInput implements KeyListener, SerialPortEventListener{

	private Set<Integer> pressedKeys = new TreeSet<Integer>();
	InputReceiver panel;
	static SerialPort serialPort;
	long time = 0;
	 /** The port we're normally going to use. */
	private static final String PORT_NAMES[] = { 
			"/dev/tty.usbserial", // Mac OS X
			"/dev/cu.usbmodem", //Mac OS X
			"COM3", // Windows
	};
	/**
	* A BufferedReader which will be fed by a InputStreamReader 
	* converting the bytes into characters 
	* making the displayed results codepage independent
	*/
	private static BufferedReader input;
	/** The output stream to the port */
	private static OutputStream output;
	/** Milliseconds to block while waiting for port open */
	private static final int TIME_OUT = 2000;
	/** Default bits per second for COM port. */
	private static final int DATA_RATE = 9600;
	
	public DancePanelInput(InputReceiver panel){
		this.panel = panel;
		initialize();
	}
	

	public void initialize() {

		CommPortIdentifier portId = null;
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();
		if(serialPort == null){
			//First, Find an instance of serial port as set in PORT_NAMES.
			while (portEnum.hasMoreElements()) {
				CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
				for (String portName : PORT_NAMES) {
					if (currPortId.getName().startsWith(portName)) {
						portId = currPortId;
						break;
					} else{
						//System.out.println(currPortId.getName());
					}
				}
			}
			if (portId == null) {
				//System.out.println("Could not find ARDUINO port.");
				return;
			}
			try {
				if(serialPort == null){
					// open serial port, and use class name for the appName.
					serialPort = (SerialPort) portId.open(this.getClass().getName(),
							TIME_OUT);
		
					// set port parameters
					serialPort.setSerialPortParams(DATA_RATE,
							SerialPort.DATABITS_8,
							SerialPort.STOPBITS_1,
							SerialPort.PARITY_NONE);
		
					// open the streams
					input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
					output = serialPort.getOutputStream();
				}
				// add event listeners
				serialPort.removeEventListener();
				serialPort.addEventListener(this);
				serialPort.notifyOnDataAvailable(true);
			} catch (Exception e) {
				System.err.println(e.toString());
			}
		}
	}
//	public void update(long deltaTime){
//		if(time > 1500){
//			time = 0;
//			if(serialPort == null){
//				initialize();
//			}
//		}
//		time += deltaTime;
//	}
	/**
	 * This should be called when you stop using the port.
	 * This will prevent port locking on platforms like Linux.
	 */
	public synchronized void close() {
		if (serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();
		}
	}

	/**
	 * Handle an event on the serial port. Read the data and print it.
	 */
	public synchronized void serialEvent(SerialPortEvent oEvent) {
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				String inputLine=input.readLine();
				if(inputLine.equals("R0")){
					panel.releaseRight();
				} else if(inputLine.equals("R1")){
					panel.pressRight();
				}
				if(inputLine.equals("L0")){
					panel.releaseLeft();
				} else if(inputLine.equals("L1")){
					panel.pressLeft();
				}
				if(inputLine.equals("U0")){
					panel.releaseUp();
				} else if(inputLine.equals("U1")){
					panel.pressUp();
				}
				if(inputLine.equals("D0")){
					panel.releaseDown();
				} else if(inputLine.equals("D1")){
					panel.pressDown();
				}
				if(inputLine.equals("S0")){
					panel.releaseEnter();
				} else if(inputLine.equals("S1")){
					panel.pressEnter();
				}
			} catch (Exception e) {
				System.err.println(e.toString());
			}
		}
		// Ignore all the other eventTypes, but you should consider the other ones.
	}
////////////////////////////////
	
	
	@Override
	public void keyPressed(KeyEvent e) {
//		if(pressedKeys.contains(e.getKeyCode())){
//			return;
//		}
		if(e.getKeyCode() == KeyEvent.VK_LEFT){
			panel.pressLeft();
		} else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
			panel.pressRight();
		} else if(e.getKeyCode() == KeyEvent.VK_UP){
			panel.pressUp();
		} else if(e.getKeyCode() == KeyEvent.VK_DOWN){
			panel.pressDown();
		} else if(e.getKeyCode() == KeyEvent.VK_ENTER){
			panel.pressEnter();
		} else if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
			panel.pressEscape();
		}
		//pressedKeys.add(e.getKeyCode());
	}
	@Override
	public void keyTyped(KeyEvent e) {

	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_LEFT){
			panel.releaseLeft();
		} else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
			panel.releaseRight();
		} else if(e.getKeyCode() == KeyEvent.VK_UP){
			panel.releaseUp();
		} else if(e.getKeyCode() == KeyEvent.VK_DOWN){
			panel.releaseDown();
		}else if(e.getKeyCode() == KeyEvent.VK_ENTER){
			panel.releaseEnter();
		}
		//pressedKeys.remove(e.getKeyCode());
	}
}
