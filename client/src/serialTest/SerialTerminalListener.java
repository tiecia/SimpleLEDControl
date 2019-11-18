package serialTest;

import java.util.Arrays;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

public class SerialTerminalListener {

	public static void main(String[] args) {
		SerialPort port = SerialPort.getCommPort("COM5");
		port.openPort();
		port.setBaudRate(115200);
		
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

}
