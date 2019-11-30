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
import structures.DeviceComponent;

public class Arduino implements Device,DeviceComponent {
	
	private String name;
	private LEDPort port;
	private ArrayList<LEDStrip> strips;
	
	private AddDeviceDialog addDialog;
	
	public final static int MAX_STRIPS = 2;
	
	public Arduino() {
		addDialog = new AddDeviceDialog();
		addDialog.setVisible(true);
		//Wait for user
		if(!addDialog.isCanceled()) {
			this.strips = addDialog.getStrips();
			this.name = addDialog.getDeviceName();
			this.port = addDialog.getDevicePort();
			for(LEDStrip strip : strips) {
				strip.setParentArduino(this);
			}
		}
	}
	
	public Arduino(String name, String portIdentity, ArrayList<LEDStrip> strips) {
		this.name = name;
		this.port = new LEDPort(portIdentity);
		this.strips = strips;
		for(LEDStrip strip : strips) {
			strip.setParentArduino(this);
		}
		addDialog = new AddDeviceDialog(this);
	}
	
	public boolean isOpen() {
		if(port == null) {
			return false;
		}
		return port.isOpen();
	}
	
	public String getName() {
		return name;
	}
	
	public LEDPort getPort() {
		return port;
	}
	
	public ArrayList<LEDStrip> getStrips(){
		return strips;
	}
	
	public String toString() {
		if(!isOpen()) {
			return name + "*";
		}
		return name;
	}
	
	public String printInfo() {
		return "Name: " + name + "\r\n" +
				"Type: Arduino Uno/Nano\r\n" +  
				"Port: " + port.getPortName() + "\r\n" + 
				"Number of Strips: " + strips.size();
	}
	
	public void addStrip(LEDStrip newStrip) {
		strips.add(newStrip);
	}
	
	public void edit() {
		port.stopListener();
		port.closePort();
		addDialog.setVisible(true);
		//Wait for user
		if(!addDialog.isCanceled()) {
			this.strips = addDialog.getStrips();
			this.name = addDialog.getDeviceName();
			this.port = addDialog.getDevicePort();
			for(LEDStrip strip : strips) {
				strip.setParentArduino(this);
			}
		}
		port.openPort();
		port.startListener();
	}

	public int maxStrips() {
		return MAX_STRIPS;
	}
	
	private void updateStrips(ArrayList<LEDStrip> newStrips) {
		for(LEDStrip strip : newStrips) {
			
		}
	}
}
