package client;

import java.awt.Checkbox;

public class StripCheckBoxes extends Checkbox {
	
	private String name;
	
	private LEDStrip linkedStrip;
	
	public StripCheckBoxes(Arduino arduino) {
		this.linkedStrip = arduino;
		this.name = this.linkedStrip.getName();
		
		setLabel(this.name);
	}
	
	public LEDStrip getStrip() {
		return linkedStrip;
	}
	
	
}
