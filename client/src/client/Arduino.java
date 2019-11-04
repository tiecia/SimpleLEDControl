package client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import addDeviceDialog.AddDeviceDialog;

public class Arduino {
	
	private String name;
	private LEDPort port;
	private ArrayList<LEDStrip> strips;
	
	private AddDeviceDialog addDialog;
	
	public Arduino() {
		addDialog = new AddDeviceDialog();
		addDialog.setVisible(true);
		//Wait for user
		if(!addDialog.isCanceled()) {
			this.strips = addDialog.getStrips();
			this.name = addDialog.getDeviceName();
			this.port = addDialog.getDevicePort();
		}
	}
	
	public Arduino(String name, String portIdentity, ArrayList<LEDStrip> strips) {
		this.name = name;
		this.port = new LEDPort(portIdentity);
		this.strips = strips;
	}
		
//		Path newPath = Paths.get("/arduinos")
//		File textFile = new File("\\SimpleLEDControl\\arduinos\\lights\\infoText.txt");
//		textFile.getParentFile().mkdirs();
//		try {
//			textFile.createNewFile();
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
//		try {
//			PrintStream fileWrite = new PrintStream(textFile);
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
	
	public boolean isOpen() {
		return port.isOpen();
	}
	
	public String getName() {
		return name;
	}
	
	public String getPort() {
		return port.getPort();
	}
	
	public ArrayList<LEDStrip> getStrips(){
		return strips;
	}
	
	public String toString() {
		return "[" + name + "," + port.getPort() + "]";
	}
	
	public void addStrip(LEDStrip newStrip) {
		strips.add(newStrip);
	}
	
	public void sendData(int brightnessValue, int redValue, int greenValue, int blueValue) {
		port.sendData(brightnessValue, redValue, greenValue, blueValue);
	}
}
