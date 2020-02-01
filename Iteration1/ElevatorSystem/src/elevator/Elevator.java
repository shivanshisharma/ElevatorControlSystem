
package elevator;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.stream.IntStream;

import elevator.common.Subsystem;

/**
 * @author Fareed Ahmad 
 * Tareq Hanafi 
 * Jaskaran Singh 
 * Shivanshi Sharma
 *
 */
public class Elevator extends Subsystem implements Runnable {

	/**
	 * @param args
	 */
	private int position;
	private Boolean door;
	private int[] floors = new int[SIZE];
	private static final int SIZE = 10;
	private int index;
	private Boolean dir;
	
	private DatagramSocket sendReceiveSocket;
	
	public Elevator() {
		floors = IntStream.range(1,10).toArray();
		index = 0;
		
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
		

	}
	
	private static void main(String[] args) {
		Elevator elevator = new Elevator();
	}
}
