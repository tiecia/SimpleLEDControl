package client;

import java.io.PrintWriter;
import java.util.Arrays;

import com.fazecast.jSerialComm.SerialPort;

public class LEDPort {

	private SerialPort port;
	
	private String portIdent;
	
	public void initialize(String comPort) {
		portIdent = comPort;
		port = SerialPort.getCommPort(comPort);
		port.openPort();
		port.setBaudRate(115200);
		port.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING| SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 1000);
	}
	
	public boolean isOpen() {
		return port.isOpen();
	}
	
	public boolean closePort() {
		return port.closePort();
	}
	
	public String getPort() {
		return port.getSystemPortName();
	}
	
	
	public void sendData(int brightnessValue, int redValue, int greenValue, int blueValue) {
		int numPix = 100;
		byte[] writeBuffer = new byte[3*numPix+1];
		writeBuffer[0] = (byte) brightnessValue;
		for(int i = 1; i<numPix*3; i+=3) {
			writeBuffer[i] = (byte) redValue;
			writeBuffer[i+1] = (byte) greenValue;
			writeBuffer[i+2] = (byte) blueValue;
		}
		System.out.println(Arrays.toString(writeBuffer));
		port.writeBytes(writeBuffer, writeBuffer.length);
	}
	
	
//	public void sendData(int brightnessValue, int redValue, int greenValue, int blueValue) {
//		byte[] writeBuffer = new byte[4];
//		writeBuffer[0] = (byte) brightnessValue;
//		writeBuffer[1] = (byte) redValue;
//		writeBuffer[2] = (byte) greenValue;
//		writeBuffer[3] = (byte) blueValue;
//		System.out.println(Arrays.toString(writeBuffer));
//		port.writeBytes(writeBuffer, writeBuffer.length);
//	}
}
