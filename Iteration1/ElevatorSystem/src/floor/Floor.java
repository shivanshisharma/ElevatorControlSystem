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

import floor.common.Subsystem;

/**
 * @author Fareed Ahmad 
 * Tareq Hanafi 
 * Jaskaran Singh 
 * Shivanshi Sharma
 *
 */
public class Floor extends Subsystem implements Runnable {


	private int[] floors = new int[SIZE];
	private static final int SIZE = 10;

	private FloorButton upFloorButton, downFloorButton;
	private FloorLamp upLamp, downLamp;
	private DirectionLamp upDirectionLamp, downDirectionLamp;
	private List<FloorData> data = new ArrayList<FloorData>();

	private DatagramPacket sendingPacket, receivingPacket;
	private DatagramSocket sendReceiveSocket;

	public Floor() {
		upFloorButton = new FloorButton(Constants.UP);
		downFloorButton = new FloorButton(Constants.DOWN);

		upLamp = new FloorLamp(Constants.UP);
		downLamp = new FloorLamp(Constants.DOWN);

		upDirectionLamp = new DirectionLamp(Constants.UP);
		downDirectionLamp = new DirectionLamp(Constants.DOWN);

		floors = IntStream.range(1,10).toArray();

		try {
			// Construct a datagram socket to send and receive
			sendReceiveSocket = new DatagramSocket();
		} catch (SocketException se) { // Can't create the socket.
			se.printStackTrace();
			System.exit(1);
		}
	}

	public void readFile() {
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
		sendingPacket = this.createPacket(data.get(0).toBytes(), 1);

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

	public List<FloorData> getData() {
		return data;
	}

	public FloorButton getUpFloorButton() {
		return upFloorButton;
	}

	public void setUpFloorButton(FloorButton upFloorButton) {
		this.upFloorButton = upFloorButton;
	}

	public FloorButton getDownFloorButton() {
		return downFloorButton;
	}

	public void setDownFloorButton(FloorButton downFloorButton) {
		this.downFloorButton = downFloorButton;
	}

	public FloorLamp getUpLamp() {
		return upLamp;
	}

	public void setUpLamp(FloorLamp upLamp) {
		this.upLamp = upLamp;
	}

	public FloorLamp getDownLamp() {
		return downLamp;
	}

	public void setDownLamp(FloorLamp downLamp) {
		this.downLamp = downLamp;
	}

	public DirectionLamp getUpDirectionLamp() {
		return upDirectionLamp;
	}

	public void setUpDirectionLamp(DirectionLamp upDirectionLamp) {
		this.upDirectionLamp = upDirectionLamp;
	}

	public DirectionLamp getDownDirectionLamp() {
		return downDirectionLamp;
	}

	public void setDownDirectionLamp(DirectionLamp downDirectionLamp) {
		this.downDirectionLamp = downDirectionLamp;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Thread floor1 = new Thread(new Floor());
		floor1.start();
	}
}
