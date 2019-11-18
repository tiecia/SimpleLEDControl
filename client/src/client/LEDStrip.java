package client;

public class LEDStrip {
	
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
}
