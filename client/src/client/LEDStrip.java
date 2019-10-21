package client;

public class LEDStrip {
	
	private Arduino arduino;
	private String name;
	private int pin;
	private int numOfLEDs;
	
	
	public LEDStrip(Arduino arduino, String name, int numOfLEDs, int pin) {
		this.arduino = arduino;
		this.name = name;
		this.numOfLEDs = numOfLEDs;
		this.pin = pin;
		arduino.initializeStrip(pin, numOfLEDs);
	}
	
	public String toString() {
		return "[" + name + "," + numOfLEDs + "," + arduino + "," + pin +"]";
	}
	
	public String getName() {
		return name;
	}
}
