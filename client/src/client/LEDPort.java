package client;

import java.io.PrintWriter;
import java.util.Arrays;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

public class LEDPort {

	private SerialPort port;
	
	private String portIdent;
	
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
			      byte[] newData = new byte[port.bytesAvailable()];
			      int numRead = port.readBytes(newData, newData.length);
			      System.out.println("New Data: " + Arrays.toString(newData));
			      System.out.println("Read " + numRead + " bytes.");
			      System.out.println();
			   }
		});
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
	
	
	public void sendData(int stripPin, int brightnessValue, int redValue, int greenValue, int blueValue) {
		byte[] writeBuffer = new byte[4];
		writeBuffer[0] = (byte) brightnessValue;
		writeBuffer[1] = (byte) redValue;
		writeBuffer[2] = (byte) greenValue;
		writeBuffer[3] = (byte) blueValue;
		System.out.println(Arrays.toString(writeBuffer));
		port.writeBytes(writeBuffer, writeBuffer.length);
		
		try {
			Thread.sleep(5);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
