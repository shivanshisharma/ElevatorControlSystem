package floor;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.stream.IntStream;

import floor.common.FloorData;
import floor.common.Subsystem;

/**
 * @author Fareed Ahmad 
 * Tareq Hanafi 
 * Jaskaran Singh 
 * Shivanshi Sharma
 *
 */
public class Floor extends Subsystem implements Runnable {
	
	private Boolean upButton;
	private Boolean downButton;
	private int[] floors = new int[SIZE];
	private static final int SIZE = 10;
	
	private DatagramPacket sendingPacket, receivingPacket;
	private DatagramSocket sendReceiveSocket;
	
	public Floor() {
		
		floors = IntStream.range(1,10).toArray();
		
		try {
			// Construct a datagram socket to send and receive
			sendReceiveSocket = new DatagramSocket();
		} catch (SocketException se) { // Can't create the socket.
			se.printStackTrace();
			System.exit(1);
		}
	}

	@Override
	public void run() {
		// read input
		FloorData data = new FloorData("14:05:15.0", 1, true, 4);
		
		sendingPacket = this.createPacket(data.toString().getBytes(), 1);

		// Print out info that is in the packet before sending
		this.printPacket(sendingPacket);

		// Send the datagram packet to the Scheduler on port 1
		this.sendPacket(sendReceiveSocket, sendingPacket, "Floor");
		
		
		byte elevatorData[] = new byte[100];
		receivingPacket = new DatagramPacket(elevatorData, elevatorData.length);
		this.receivePacket(sendReceiveSocket, receivingPacket, "Floor");
		
		// Print out received packet
		this.printPacket(receivingPacket);

		// We're finished, so close the socket.
		sendReceiveSocket.close();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Thread floor1 = new Thread(new Floor());
		floor1.start();
	}
}
