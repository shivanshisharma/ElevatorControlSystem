package schedule;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import schedule.common.Elevator;
import schedule.common.Floor;
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
	private List<Floor> floorRequests;
	private List<Elevator> elevatorStatus;

	private ArrayList<Integer> elevator1Request, elevator2Request;
	
	//Gives the position of the elevators and its direction, every position in the arrays means one specific elevator.
	int[] position;
	int[] direction;
	
	Map<Integer, ArrayList<Integer>> requestMap;

	private DatagramPacket floorPacket, elevatorPacket, instructionPacket;
	private DatagramSocket floorSocket, elevatorSocket;
	
	public Scheduler() {
		floorRequests = new ArrayList<Floor>();
		elevatorStatus = new ArrayList<Elevator>();
		elevator1Request = new ArrayList<>();
		elevator2Request = new ArrayList<>();
		
		//This will change depending on the amount of elevators in the subsystem running
		position = new int[2];
		position[0] = 1;
		position[1] = 1;
		
		direction = new int[2];
		/*
		 * 0 : means that its going up
		 * 1 : means that its going down
		 * 2 : means that its stopped
		 */
		direction[0] = 2;
		direction[1] = 2;
		
		try {
			floorSocket = new DatagramSocket(1);
		} catch (SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}

		try {
			elevatorSocket = new DatagramSocket(2);
		} catch (SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}
		/*
		try {
			sendSocket = new DatagramSocket();
		} catch (SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}
		*/
		
		Thread elevatorHandler = new Thread(new ElevatorThread(this,elevatorSocket));
		Thread floorHandler = new Thread(new FloorThread(this,floorSocket));
		
		elevatorHandler.start();
		floorHandler.start();
	}

	
	public synchronized void updateElevatorStatus(Elevator item) {
		for(Elevator elevator: elevatorStatus) {
			if(elevator.getId() == item.getId()) {
				elevator.update(item);
				notifyAll();
				return;
			}
		}
		elevatorStatus.add(item);
		notifyAll();
	}
	
	public synchronized void addFloorRequest(Floor item) {
		floorRequests.add(item);
		notifyAll();
	}

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

	public byte[] sendElevator(int port, ArrayList<Integer> request) {
		 byte[] floor = decideDestination(direction[port-1], port, request);
		 return floor;
	}
	
	@Override
	public void run() {
		
		
		
		/*
		byte floorData[] = new byte[100];
		floorPacket = new DatagramPacket(floorData, floorData.length);
		this.receivePacket(receiveSocket, floorPacket, "Scheduler");
		populateElevatorLists((int)floorData[2]);

		// Print out received packet
		this.printPacket(floorPacket);
		
		byte elevatorData[] = new byte[100];
		elevatorPacket = new DatagramPacket(elevatorData, elevatorData.length);
		this.receivePacket(receiveSocket, elevatorPacket, "Scheduler");
		
		// Print out received packet
		this.printPacket(elevatorPacket);
		/*int port = elevatorData[2];
		positions[port-1] = elevatorData[0];
		directions[port-1] = elevatorData[1];
		*/
		/*
		// Send the datagram packet to the Elevator
		byte[] floor = "New Instruction".getBytes();
		if(!elevator1Request.isEmpty()) {
			floor = sendElevator(1, elevator1Request);
		}
		if(!elevator2Request.isEmpty()) {
			floor = sendElevator(1, elevator1Request);
		}
		instructionPacket = this.createPacket(floor, elevatorPacket.getPort());


		// Print out info that is in the packet before sending
		this.printPacket(instructionPacket);
		
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
		*/
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/*
		Thread schedular = new  Thread(new Scheduler());
		schedular.start();
		*/
		Scheduler sch = new Scheduler();
	}
}
