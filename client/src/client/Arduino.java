package client;

import javax.swing.JOptionPane;

public class Arduino {
	
	private String name;
	private LEDPort port;
	
	
	public Arduino(String name, String portIdentity) {
		this.name = name;
		connect(portIdentity);
	}
	
	public void initializeStrip(int pin, int num) {
		String stringvalue = "189" + String.format("%03d", pin) + String.format("%03d", num) + "000" + "000";
		port.sendData(stringvalue);
	}
	
	private boolean connect(String port) {
		this.port = new LEDPort();
		this.port.initialize(port);
		return this.port.isOpen();
	}
	
	public boolean isOpen() {
		return port.isOpen();
	}
	
	public String getName() {
		return name;
	}
	
	public String getPort() {
		return port.getPort();
	}
	
	public String toString() {
		return "[" + name + "," + port.getPort() + "]";
	}
	
	public void sendData(int brightnessValue, int redValue, int greenValue, int blueValue) {
		port.sendData(brightnessValue, redValue, greenValue, blueValue);
	}
}
