package client;

public class LEDStrip {
	
	private String name;
	private int pin;
	private int numOfLEDs;
	private LEDPort port;
	
	
	public LEDStrip(String name, int numOfLEDs, int pin) {
		this.name = name;
		this.numOfLEDs = numOfLEDs;
		this.pin = pin;
	}
	
	public void setPort(LEDPort port) {
		this.port = port;
	}
	
	public String toString() {
		return name;
	}
	
	public String getName() {
		return name;
	}
	
	public LEDPort getPort() {
		return port;
	}
}
