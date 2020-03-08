package schedule;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import schedule.common.Elevator;
import schedule.common.ElevatorMessage;
import schedule.common.Floor;
import schedule.common.SchedulerInstruction;
import schedule.common.SchedulerInstruction.INSTRUCTION;
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
	private List<Floor> floorRequestQueue;
	private List<ElevatorMessage> elevatorMessageQueue;
	private Map<Integer,Elevator> elevators;

	private ArrayList<Integer> elevator1Request, elevator2Request;
	
	//Gives the position of the elevators and its direction, every position in the arrays means one specific elevator.
	int[] position;
	int[] direction;
	
	Map<Integer, ArrayList<Integer>> requestMap;

	private DatagramPacket floorPacket, elevatorPacket, instructionPacket;
	private DatagramSocket floorSocket, elevatorSocket;
	
	public Scheduler() {
		floorRequestQueue = new ArrayList<Floor>();
		elevatorMessageQueue = new ArrayList<ElevatorMessage>();
		elevator1Request = new ArrayList<>();
		elevator2Request = new ArrayList<>();
		elevators = new HashMap<Integer, Elevator>();
		
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
	
		Thread elevatorHandler = new Thread(new ElevatorThread(this,elevatorSocket));
		Thread floorHandler = new Thread(new FloorThread(this,floorSocket));
		
		elevatorHandler.start();
		floorHandler.start();
	}

	
	public synchronized void addElevatorMessage(ElevatorMessage item) {
		if(!elevators.containsKey(item.getId())) {
			elevators.put(item.getId(), new Elevator(item.getId(),item.getState(),item.getCurrFloor(),item.getTargetFloor(),0,new ArrayList<Floor>(),new ArrayList<Integer>()));
		}
		if(elevatorMessageQueue.add(item)) {
			System.out.println("New elevator message: " + item);
		}
		
		notifyAll();
	}
	
	public synchronized ElevatorMessage getElevatorMessage() {
		if(elevatorMessageQueue.isEmpty()) {
			notifyAll();
			return null;
		}
		notifyAll();
		return elevatorMessageQueue.remove(0);
	}
	
	public synchronized void addFloorRequest(Floor item) {
		floorRequestQueue.add(item);
		notifyAll();
	}
	
/*
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
	*/
	public SchedulerInstruction.INSTRUCTION processElevatorMessage(ElevatorMessage message) {
		Elevator elevator = elevators.get(message.getId());
		elevator.update(message);
		SchedulerInstruction.INSTRUCTION command = INSTRUCTION.ELEVETOR_WAIT;
		if(!elevator.hasPendingTask()) {
			if(floorRequestQueue.isEmpty()) {
				//tell elevator to wait
				command = INSTRUCTION.ELEVETOR_WAIT;
			}else {
				//give elevator a floor request
				Floor floorData = floorRequestQueue.remove(0);
				elevator.getPickUpFloors().add(floorData);
				elevator.setDestFloor(floorData.getFloorNumber());
			}
		}
		
		switch(elevator.getState()) {
		case 3:
		case 0: // stopped at floor
			if(!elevator.getPickUpFloors().isEmpty()) {
				if(elevator.getCurrFloor() == elevator.getPickUpFloors().get(0).getFloorNumber()) {
					//pick up passenger
					elevator.setDestFloor(elevator.getPickUpFloors().remove(0).getCarButton());
					elevator.getDropOffFloors().add(elevator.getDestFloor());
				}
			}
			
			if(!elevator.getDropOffFloors().isEmpty()) {
				if(elevator.getCurrFloor() == elevator.getDropOffFloors().get(0)) {
					//drop off passenger
					
					elevator.getDropOffFloors().remove(0);
				}
			}

			if(elevator.getCurrFloor() > elevator.getDestFloor()) {
				//move down
				command = INSTRUCTION.ELEVETOR_DOWN;
			}else if(elevator.getCurrFloor() < elevator.getDestFloor()) {
				//move up
				command = INSTRUCTION.ELEVETOR_UP;
			}
					
			break;
		case 1: // moving up
					
			if(elevator.getDestFloor() == elevator.getCurrFloor() + 1) {
				//Next floor elevator needs to stop
				command = INSTRUCTION.ELEVETOR_STOP;
			}else {
				//continue moving to next floor
				command = INSTRUCTION.ELEVETOR_UP;
			}
			break;
		case 2: // moving down
			if(elevator.getDestFloor() == elevator.getCurrFloor() - 1) {
				//Next floor elevator needs to stop
				command = INSTRUCTION.ELEVETOR_STOP;
			}else {
				//continue moving to next floor
				command = INSTRUCTION.ELEVETOR_DOWN;
			}
			break;
		}
		return command;
	}
	
	@Override
	public void run() {
		
		while(true) {
			ElevatorMessage elevatorMessage = this.getElevatorMessage();
			if(elevatorMessage != null) {
				System.out.println("Sending reply");	
				SchedulerInstruction.INSTRUCTION message = processElevatorMessage(elevatorMessage);
				byte response[] = {message.getValue()};
				new Thread(new MessageSender(response, elevatorMessage.getPort())).start();;
			}
			
		}
	}
	
	

	public List<Floor> getFloorRequestQueue() {
		return floorRequestQueue;
	}


	public void setFloorRequestQueue(List<Floor> floorRequestQueue) {
		this.floorRequestQueue = floorRequestQueue;
	}


	public List<ElevatorMessage> getElevatorMessageQueue() {
		return elevatorMessageQueue;
	}


	public void setElevatorMessageQueue(List<ElevatorMessage> elevatorMessageQueue) {
		this.elevatorMessageQueue = elevatorMessageQueue;
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Thread schedular = new  Thread(new Scheduler());
		schedular.start();
		
		
	}
}
