
package elevator;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.stream.IntStream;

import elevator.common.Subsystem;

/**
 * @author Fareed Ahmad Tareq Hanafi Jaskaran Singh Shivanshi Sharma
 *
 */
public class Elevator extends Subsystem implements Runnable {

	/**
	 * @param args
	 */
	private int currFloor;
	private boolean door;
	private int[] floors = new int[SIZE];
	private static final int SIZE = 10;
	private int index;
	private boolean goingUp;
	private boolean isWaiting;
	private int targetFloor;

	private DatagramPacket elevatorDataPacket, instructionPacket;
	private DatagramSocket sendReceiveSocket;

	public Elevator() {
		this.floors = IntStream.range(1, 10).toArray();
		this.index = 0;
		this.isWaiting = true;
		this.goingUp = true;
		this.currFloor = 0;
		this.targetFloor = 0;

		try {
			// Construct a datagram socket to send and receive
			sendReceiveSocket = new DatagramSocket();
		} catch (SocketException se) { // Can't create the socket.
			se.printStackTrace();
			System.exit(1);
		}
	}
	
	public String toString() {
		return this.isWaiting + " " + this.currFloor + " "  + this.targetFloor + " " + this.goingUp;
	}

	@Override
	public void run() {
		// Create datagram packet
		elevatorDataPacket = this.createPacket(this.toString().getBytes(), 1);

		// Print out info that is in the packet before sending
		this.printPacket(elevatorDataPacket);

		// Send the datagram packet to the host on port 23
		this.sendPacket(sendReceiveSocket, elevatorDataPacket, "Elevator");

		// Receive response packet
		byte data[] = new byte[100];
		instructionPacket = new DatagramPacket(data, data.length);
		this.receivePacket(sendReceiveSocket, instructionPacket, "Elevator");

		// Print the received datagram.
		this.printPacket(instructionPacket);

		// We're finished, so close the socket.
		sendReceiveSocket.close();

	}

	public static void main(String[] args) {
		Thread elevator1 = new Thread(new Elevator());
		elevator1.start();
		
	}
}
