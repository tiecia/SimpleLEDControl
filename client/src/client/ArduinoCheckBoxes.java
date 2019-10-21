package client;

import java.awt.Checkbox;

public class ArduinoCheckBoxes extends Checkbox {
	
	private String name;
	private String port;
	
	private Arduino linkedArduino;
	
	public ArduinoCheckBoxes(Arduino linkedArduino) {
		this.linkedArduino = linkedArduino;
		this.name = this.linkedArduino.getName();
		this.port = this.linkedArduino.getPort();
		
		setLabel(this.name + "(" + this.port + ")");
	}
	
	public Arduino getArduino() {
		return linkedArduino;
	}
	
	
}
