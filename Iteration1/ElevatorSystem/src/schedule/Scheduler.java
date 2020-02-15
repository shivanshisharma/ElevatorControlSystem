
package schedule;
import java.io.*;
import java.net.*;
import java.util.*;

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

	private ArrayList<Integer> elevator1Request, elevator2Request;
	
	//Gives the position of the elevators and its direction, every position in the arrays means one specific elevator.
	int[] position;
	int[] direction;
	
	Map<Integer, ArrayList<Integer>> requestMap;
	
	private DatagramPacket floorPacket, elevatorPacket, instructionPacket;
	private DatagramSocket receiveSocket, sendSocket;
	
	
	public Scheduler() {
		
		elevator1Request = new ArrayList<>();
		elevator2Request = new ArrayList<>();
		
		//This will change depending on the amount of elevators in the subsystem running
		position = new int[1];
		position[0] = 1;
		position[1] = 1;
		
		direction = new int[1];
		/*
		 * 0 : means that its going down
		 * 1 : means that its going up
		 * 2 : means that its stopped
		 */
		direction[0] = 2;
		direction[1] = 2;
		
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
	
	/*
	 * OLD
	 
	public void sendElevator(int port, ArrayList<Integer> dropoff )
	{
		//Send to Elevator
		byte[] floor = decideDestination(direction[port-1], port, dropoff);
		
		
		try {																			//elevator id to send to 
			floorPacket = new DatagramPacket(floor, floor.length, InetAddress.getLocalHost(), port);
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}

		System.out.println("Scheduler: Sending packet to elevator " + port);
		System.out.println("To host:   " + sendPacket.getAddress());
		System.out.println("Destination host port:   " + sendPacket.getPort());
		int len = sendPacket.getLength();
		System.out.println("Length: " + len);
		System.out.println("Containing: ");
		String msgSendElevatorStr = new String(sendPacket.getData(),0,len);
		System.out.println("Packet data in bytes: " + Arrays.toString(msgSendElevatorStr.getBytes()));
		System.out.println("Packet data as a string: " + msgSendElevatorStr);

		try {
			sendSocket.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("Scheduler: Packet sent\n");
	}
	*/
	public byte[] decideDestination(int direc, int port, ArrayList<Integer> request){

		int destinationFloor = 8;
		int temp = 0;
		int up = direc;
		int list = 0;
		boolean negative = false;
		
		int first = 2;
		int third = 2;

		System.out.println("List of requests: "+ request + "\nOriginal Position" + position[port-1]);

		for(int i : request){
			if(up == 0 || up == 2){
				if(i > 0){
					temp = Math.abs(position[port-1] - i);
					if(temp < destinationFloor){
						destinationFloor = i;
						list = 1;
					}
				}
			}else{
				if(i < 0){
					temp = position[port-1] - Math.abs(i);
					if(temp < destinationFloor){
						destinationFloor = Math.abs(i);
						list = 1;
						negative = true;
					}
				}
			}    
		}

		if(list < 0){
			request.remove(request.indexOf(destinationFloor));
			first = 1;
		}else if(list > 0){
			if(negative){
				request.remove(request.indexOf(destinationFloor*-1));
				first = 0;
			}else{
				request.remove(request.indexOf(destinationFloor));
				 first = 0;
			}
		}

		negative = false;
		
		if(destinationFloor > position[port-1]) {
			third = 0;
		}
		else if(destinationFloor < position[port-1]) {
			third = 1;
		}
		else {
			third = 2;
		}
		
		System.out.println("After postion: " + position[port-1]);
		
		if(first == 0 || first == 2) {
			requestMap.remove(destinationFloor);
		}
		
		byte[] ret = new byte[3];
		
		ret[0] = (byte)first;
		ret[1] = (byte)destinationFloor;
		ret[2] = (byte)third;
		
		return ret;
	} 

	
	/*
	public void receiveFloor() {
		byte[] data = new byte[1];
		receivePacket = new DatagramPacket(data, data.length);
		
		System.out.println("Scheduler: Waiting for Packet.\n");
		//Block until a datagram packet is received.
		try {
			System.out.println("Waiting for floor... 1 second timeout");
			//sendReceiveSocket.setSoTimeout(1000);
			sendReceiveSocket.receive(receivePacket);
		} catch (IOException e) {
			//e.printStackTrace();
			//System.exit(1);
			return;
		}
		
		//Process received datagram
		System.out.println("Scheduler: Packet received from floor");
		System.out.println("From host:   " + receivePacket.getAddress());
		System.out.println("Host port:   " + receivePacket.getPort());
		int len = receivePacket.getLength();
		System.out.println("Length: " + len);
		System.out.println("Containing: ");
		String msgReceiveElevatorStr = new String(receivePacket.getData(),0,len);
		System.out.println("Packet data in bytes: " + Arrays.toString(msgReceiveElevatorStr.getBytes()));
		System.out.println("Packet data as a string: " + msgReceiveElevatorStr + "\n");
		
		//When the Scheduler receives a message from the floor, we need to put in an elevator list
		populateElevatorLists((int)data[0]);
	}*/
	
	
	public void populateElevatorLists(int floor)
	{
		System.out.println("Floor Request: " + floor);

		
		ArrayList<Integer> request = new ArrayList<Integer>();
		request.add(floor);
		requestMap.put(floor, request);
		
		if (elevator1Request.isEmpty() && elevator2Request.isEmpty()) { 
			if (Math.abs(position[1] - floor) <  Math.abs(position[0] - floor)) { 
				elevator2Request.add(floor);
			} 
			else { 
				elevator1Request.add(floor);
			}		
			
		}  else if (!elevator1Request.isEmpty() || elevator2Request.isEmpty()) { 
			if (direction[0] == getDir(floor) && !hasElePassedFloor(direction[0], position[0], floor)) { 

				elevator1Request.add(floor);
			} else { 

				elevator2Request.add(floor);
			}
		} else if (elevator1Request.isEmpty() || !elevator2Request.isEmpty()) { 
			if (direction[1] == getDir(floor) && !hasElePassedFloor(direction[1], position[1], floor)) { 

				elevator2Request.add(floor);
			} else { 

				elevator1Request.add(floor);
			}
		} else { 
			if (direction[0] == direction[1]) { 
				if (!hasElePassedFloor(direction[0], position[0], floor) &&  !hasElePassedFloor(direction[1], position[1], floor)) { 
					if (Math.abs(position[1] - floor) <  Math.abs(position[0] - floor)) { 

						elevator2Request.add(floor);
					} else { 

						elevator1Request.add(floor);
					}	
				} else if (!hasElePassedFloor(direction[1], position[1], floor)) {

					elevator2Request.add(floor);
				} else if (!hasElePassedFloor(direction[0], position[0], floor)) {

					elevator1Request.add(floor);
				} else { 
					if (elevator1Request.size() < elevator2Request.size()) {

						elevator1Request.add(floor);
					} else {

						elevator2Request.add(floor);
					}
				}
			} else { 
				if (direction[0] == getDir(floor)) {

					elevator1Request.add(floor);
				} else {

					elevator2Request.add(floor);
				}
			}
		}
	}
	
	public int getDir(int floor)
	{
		if (floor > 0) {
			return 0; //Going Up
		} else if (floor < 0) {
			return 1; //Going Down
		} else {
			return 2; //Stopped
		}
	}
	
	public boolean hasElePassedFloor(int direc, int cur, int floor)
	{
		if (direc == 0) { //Elevator is going up
			if ((floor - cur) > 0) {
				return false;
			} else {
				return true;
			}
		} else if (direc == 1) { //Elevator is going down
			if ((floor - cur) < 0) {
				return false;
			} else {
				return true;
			}
		}
		return false; //Shouldn't get here but will be able to service the floor and reset its direction if not moving
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
		
		populateElevatorLists((int)floorData[2]);

		// Print out received packet
		this.printPacket(elevatorPacket);
		
		instructionPacket = this.createPacket("New Instruction".getBytes(), elevatorPacket.getPort());

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
