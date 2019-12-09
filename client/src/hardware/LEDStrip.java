package hardware;

import structures.DeviceComponent;

public class LEDStrip implements DeviceComponent {
	
	private String name;
	private int pin;
	private int numOfLEDs;
	private Arduino parentArduino;
	
	
	public LEDStrip(String name, int numOfLEDs, int pin) {
		this.name = name;
		this.numOfLEDs = numOfLEDs;
		this.pin = pin;
	}
	
	public String toString() {
		if(!parentArduino.isOpen()) {
			return name + "*";
		}
		return name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setParentArduino(Arduino parent) {
		this.parentArduino = parent;
	}
	
	public Arduino getParentArduino() {
		return parentArduino;
	}
	
	public int getPin() {
		return this.pin;
	}
	
	public int getNumOfLEDs() {
		return numOfLEDs;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setPin(int pin) {
		this.pin = pin;
	}
	
	public void setNumOfLEDs(int num) {
		this.numOfLEDs = num;
	}

	public String printInfo() {
		return "Name: " + name + "\r\n" +
				"Type: LED Strip\r\n" +  
				"Parent Device: " + parentArduino + "\r\n" +
				"Port: " + parentArduino.getPort().getPortName() + "\r\n" + 
				"Number of LEDs: " + numOfLEDs + "\r\n" +
				"Pin: " + pin;
	}
	
	
}
