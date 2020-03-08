package schedule;
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

	private List<Floor> floorRequestQueue;
	private List<ElevatorMessage> elevatorMessageQueue;
	private Map<Integer,Elevator> elevators;

	private DatagramSocket floorSocket, elevatorSocket;

	public Scheduler() {
		floorRequestQueue = new ArrayList<Floor>();
		elevatorMessageQueue = new ArrayList<ElevatorMessage>();
		elevators = new HashMap<Integer, Elevator>();

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
