package floor;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
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
	private List<FloorData> data = new ArrayList<FloorData>(); 
	
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
	
	private void readFile() {
		File file = new File("Data.txt");
        Scanner input;
		try {
			input = new Scanner(file);
			while (input.hasNextLine()) {
	            String line = input.nextLine();
	            
	            FloorData floorData = new FloorData(line.split(" ")[0], 
	            		Integer.parseInt(line.split(" ")[1]), 
	            		line.split(" ")[2].equalsIgnoreCase("Up") , 
	            		Integer.parseInt(line.split(" ")[3]));
	            data.add(floorData);
	            
	            System.out.println(line);
	        }
	        input.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	@Override
	public void run() {
		// Input file 
		readFile();
		
		// read input
		sendingPacket = this.createPacket(data.get(0).toString().getBytes(), 1);

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
