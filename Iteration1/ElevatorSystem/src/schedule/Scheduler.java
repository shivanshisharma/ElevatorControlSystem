
package schedule;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
/**
 * 
 */

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
	private int index;
	
	//If false, it heads down, if true, it heads up.
	private Boolean DirEl;
	//If stopped
	private Boolean stop;
	
	private DatagramPacket floorPacket, elevatorPacket;
	private DatagramSocket receiveSocket, sendReceiveSocket;
	
	
	public Scheduler() {
		try {
			receiveSocket = new DatagramSocket(1);
		} catch (SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}
	}
	
	/*
	 * This function adds a 
	 */
	public void addElevatorRequest(Integer item) {
		if(schedulerElevator.contains(item)){
			 System.out.println("Request already received");
			 return;
		}
		
		schedulerElevator.add(item);
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
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Thread schedular = new  Thread(new Scheduler());
		schedular.start();
	}
}