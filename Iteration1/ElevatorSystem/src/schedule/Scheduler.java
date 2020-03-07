
package schedule;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
/**
 * 
 */
import java.util.List;

import schedule.common.ElevatorData;
import schedule.common.FloorData;
import schedule.common.Subsystem;

/**
 * @author Fareed Ahmad 
 * Tareq Hanafi - 101036095
 * Jaskaran Singh 
 * Shivanshi Sharma
 * @param <Objects>
 *
 */
public class Scheduler extends Subsystem implements Runnable {

	private ArrayList<Integer> schedulerElevator = new ArrayList<>();
	private List<FloorData> floorRequests = new ArrayList<>();
	private List<ElevatorData> elevatorRequests =  new ArrayList<>();
	
	private DatagramPacket floorPacket, elevatorPacket, instructionPacket;
	private DatagramSocket receiveSocket, sendSocket;
	
	public Scheduler() {
		try {
			receiveSocket = new DatagramSocket(1);
		} catch (SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}
		
		try {
			sendSocket = new DatagramSocket();
		} catch (SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}
	}
	
	public synchronized void addElevatorRequest(ElevatorData item) {
		elevatorRequests.add(item);
		notifyAll();
	}
	
	public boolean checkRequests(Integer item) {
		if(schedulerElevator.contains(item)) {
			System.out.println("");
			return true;
		}
		return false;
	}
	
	public ArrayList<Integer> sortAscending(){
		Collections.sort(schedulerElevator);
		return schedulerElevator;
	}

	@Override
	public void run() {
		byte floorData[] = new byte[100];
		floorPacket = new DatagramPacket(floorData, floorData.length);
		this.receivePacket(receiveSocket, floorPacket, "Scheduler");

		// Print out received packet
		this.printPacket(floorPacket);
		
		byte elevatorData[] = new byte[100];
		elevatorPacket = new DatagramPacket(elevatorData, elevatorData.length);
		this.receivePacket(receiveSocket, elevatorPacket, "Scheduler");

		// Print out received packet
		this.printPacket(elevatorPacket);
		
		instructionPacket = this.createPacket(floorPacket.getData(), elevatorPacket.getPort());

		// Print out info that is in the packet before sending
		this.printPacket(instructionPacket);

		// Send the datagram packet to the Elevator
		this.sendPacket(sendSocket, instructionPacket, "Scheduler");
		
		elevatorPacket = new DatagramPacket(elevatorData, elevatorData.length);
		this.receivePacket(receiveSocket, elevatorPacket, "Scheduler");

		// Print out received packet
		this.printPacket(elevatorPacket);
		
		floorPacket = this.createPacket(elevatorPacket.getData(), floorPacket.getPort());

		// Print out info that is in the packet before sending
		this.printPacket(floorPacket);

		// Send the datagram packet to the Elevator
		this.sendPacket(sendSocket, floorPacket, "Scheduler");
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Thread schedular = new  Thread(new Scheduler());
		schedular.start();
	}
}
