package hardware;

import java.util.Arrays;

import javax.swing.JOptionPane;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

public class LEDPort  {

	private SerialPort port;
	
	private String portIdent;
	
	private PortListener listen;
	
	public LEDPort(String comPort) {
		portIdent = comPort;
		port = SerialPort.getCommPort(comPort);
		port.openPort();
		port.setBaudRate(115200);
		port.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING| SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 1000);
		
		port.addDataListener(new SerialPortDataListener() {
			   @Override
			   public int getListeningEvents() { return SerialPort.LISTENING_EVENT_DATA_AVAILABLE; }
			   @Override
			   public void serialEvent(SerialPortEvent event)
			   {
			      if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
			         return;
			      byte[] newData = new byte[5];
			      int numRead = port.readBytes(newData, newData.length);
			      System.out.println("New Data: " + Arrays.toString(newData));
			      System.out.println("Read " + numRead + " bytes.");
			      System.out.println();
			   }
		});
		listen = new PortListener();
		listen.start();
	}
	
	public boolean isOpen() {
		return port.isOpen();
	}
	
	public boolean closePort() {
		stopListener();
		return port.closePort();
	}
	
	public String getPortName() {
		return port.getSystemPortName();
	}
	
	public boolean openPort() {
		return port.openPort();
	}
	
	@SuppressWarnings("deprecation")
	public void stopListener() {
		listen.stop();
	}
	
	@SuppressWarnings("deprecation")
	public void startListener() {
		listen.resume();
	}
	
	
	public void sendData(int stripPin, int brightnessValue, int redValue, int greenValue, int blueValue) {
		byte[] writeBuffer = new byte[5];
		writeBuffer[0] = (byte) stripPin;
		writeBuffer[1] = (byte) brightnessValue;
		writeBuffer[2] = (byte) redValue;
		writeBuffer[3] = (byte) greenValue;
		writeBuffer[4] = (byte) blueValue;
		System.out.println(Arrays.toString(writeBuffer));
		port.writeBytes(writeBuffer, writeBuffer.length);
		
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private class PortListener extends Thread {
		public void run() {
			boolean state = port.isOpen();
			while(state == port.isOpen()) {
				if(state == false) {
					port.openPort();
				}
			}
			if(state == true) {
				JOptionPane.showMessageDialog(null,"Device on port " + portIdent + " has failed to connect." , "Error", JOptionPane.ERROR_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(null,"Device on port " + portIdent + " has been connected." , "Connected", JOptionPane.INFORMATION_MESSAGE);
			}
			state = port.isOpen();
			run();
		}
	}
}
