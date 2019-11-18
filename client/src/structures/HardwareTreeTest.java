package structures;

import java.util.ArrayList;

import client.Arduino;
import client.LEDStrip;
import createZoneDialog.CreateZoneDialog;

public class HardwareTreeTest {

	public static void main(String[] args) {
		ArrayList<LEDStrip> strips = new ArrayList<>();
		strips.add(new LEDStrip("Front",  3, 5));
		strips.add(new LEDStrip("Back",  3, 5));
		strips.add(new LEDStrip("Left",  3, 5));
		
		ArrayList<Arduino> input = new ArrayList<>();
		Arduino one = new Arduino("Name" , "Port" , strips);
		Arduino two = new Arduino("Ard" , "COM" , strips);
		Arduino three = new Arduino("Name" , "Port" , strips);
		Arduino four = new Arduino("Ard" , "COM" , strips);
		input.add(one);
		input.add(two);
		input.add(three);
		input.add(four);
		
		CreateZoneDialog dialog = new CreateZoneDialog(input);
		dialog.setVisible(true);
	}

}
