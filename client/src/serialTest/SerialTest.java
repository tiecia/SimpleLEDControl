package serialTest;

import java.util.Arrays;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

public class SerialTest {

	public static void main(String[] args) throws InterruptedException {
		SerialPort port = SerialPort.getCommPort("COM6");
		port.openPort();
		port.setBaudRate(115200);
		if(!port.isOpen()) {
			System.out.println("Not Connected");
		} else {
			System.out.println("Connected");
		}
		
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
		
		Thread.sleep(3000);
		
		byte[] initializeBuffer = new byte[5];
		initializeBuffer[0] = (byte) 200;
		initializeBuffer[1] = (byte) 1;
		initializeBuffer[2] = (byte) 1;
		initializeBuffer[3] = (byte) 1;
		initializeBuffer[4] = (byte) 1;
		port.writeBytes(initializeBuffer, initializeBuffer.length);
		System.out.println(Arrays.toString(initializeBuffer));
		

//		Thread.sleep(30);

		
		
		int red = 0;
		int incrementFactor = 1;
		
		int time = 0;
		while (time < 300) {
			System.out.println("Time: " + time);
			byte[] writeBuffer = new byte[5];
			writeBuffer[0] = (byte) 2;
			writeBuffer[1] = (byte) 100;
			writeBuffer[2] = (byte) red;
			writeBuffer[3] = (byte) 255;
			writeBuffer[4] = (byte) 0;
			System.out.println(Arrays.toString(writeBuffer));
			port.writeBytes(writeBuffer, writeBuffer.length);
			
//			writeBuffer[0] = (byte) 5;
//			writeBuffer[1] = (byte) 100;
//			writeBuffer[2] = (byte) 0;
//			writeBuffer[3] = (byte) 255;
//			writeBuffer[4] = (byte) red;
//			System.out.println(Arrays.toString(writeBuffer));
//			port.writeBytes(writeBuffer, writeBuffer.length);
			
			if (red == 0) {
				incrementFactor = 1;
			} else if (red == 255) {
				incrementFactor = -1;
			}
			red += incrementFactor;

			Thread.sleep(20);
			

			time++;
		}
		
		
		//CHANGE PIN #
		
		initializeBuffer = new byte[5];
		initializeBuffer[0] = (byte) 200;
		initializeBuffer[1] = (byte) 2;
		initializeBuffer[2] = (byte) 1;
		initializeBuffer[3] = (byte) 1;
		initializeBuffer[4] = (byte) 1;
		port.writeBytes(initializeBuffer, initializeBuffer.length);
		System.out.println(Arrays.toString(initializeBuffer));
		

//		Thread.sleep(30);

		
		
		red = 0;
		incrementFactor = 1;
		
		time = 0;
		while (time < 300) {
			System.out.println("Time: " + time);
			byte[] writeBuffer = new byte[5];
			writeBuffer[0] = (byte) 2;
			writeBuffer[1] = (byte) 100;
			writeBuffer[2] = (byte) red;
			writeBuffer[3] = (byte) 255;
			writeBuffer[4] = (byte) 0;
			System.out.println(Arrays.toString(writeBuffer));
			port.writeBytes(writeBuffer, writeBuffer.length);
			
			writeBuffer[0] = (byte) 6;
			writeBuffer[1] = (byte) 100;
			writeBuffer[2] = (byte) 0;
			writeBuffer[3] = (byte) 255;
			writeBuffer[4] = (byte) red;
			System.out.println(Arrays.toString(writeBuffer));
			port.writeBytes(writeBuffer, writeBuffer.length);
			
			if (red == 0) {
				incrementFactor = 1;
			} else if (red == 255) {
				incrementFactor = -1;
			}
			red += incrementFactor;

			Thread.sleep(20);
			

			time++;
		}
	}
}
