package client;

public class LEDStrip {
	
	private String name;
	private int pin;
	private int numOfLEDs;
	
	
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
}
