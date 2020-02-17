
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
	
	//things to keep track of the state machine
	private int currFloor;
	int id;
	private int floor; // Elevator's Current Floor

	
	private boolean isWaiting;
	private int targetFloor;
	private boolean door;
	private int motor;
	private boolean operational;
	private boolean[] buttons;
	private int index;
	private boolean goingUp;
	

	private DatagramPacket elevatorDataPacket, instructionPacket;
	private DatagramSocket sendReceiveSocket;
	
	
	ElevatorButton button1, button2, button3, button4, button5;
	ElevatorLight light1, light2, light3, light4, light5; 

	Motor motorState;
	ElevatorButton [] buttonArray;
	ElevatorLight[] lightArray;
	private String direction;
	private int[] floors;
	ElevatorSystem state;
	Door doorState; 

	public Elevator() {
		
		try {
			// Construct a datagram socket to send and receive
			sendReceiveSocket = new DatagramSocket();
		} catch (SocketException se) { // Can't create the socket.
			se.printStackTrace();
			System.exit(1);
		}
		
		operational = true;
		this.floors = IntStream.range(1, 10).toArray();
		this.index = 0;
		this.isWaiting = true;
		this.goingUp = true;
		this.currFloor = 0;
		this.targetFloor = 0;
		
		buttonArray = new ElevatorButton[7];
		buttonArray[0] = button1;
		buttonArray[1] = button2;
		buttonArray[2] = button3;
		buttonArray[3] = button4;
		buttonArray[4] = button5;
		
		lightArray = new ElevatorLight[5];
		lightArray[0] = light1;
		lightArray[1] = light2;
		lightArray[2] = light3;
		lightArray[3] = light4;
		lightArray[4] = light5;
		
		motorState = new Motor();
		state = new ElevatorSystem();
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
		//this.printPacket(instructionPacket);
		
//		elevatorDataPacket = this.createPacket("Arrived at the floor".getBytes(), 1);

//		// Print out info that is in the packet before sending
		this.printPacket(elevatorDataPacket);

		// Send the datagram packet to the host on port 23
		this.sendPacket(sendReceiveSocket, elevatorDataPacket, "Elevator");


		// We're finished, so close the socket.
		sendReceiveSocket.close();
		

		while(true) {
			if(isOperational()) {
				System.out.println("Moving Elevator:"+getId());
			} else {
				System.out.println("Elevator "+getId()+": is NOT operational");

			}
		}

	}
	// This class Process the Request received from Scheduler and calls approproate methods.
	public void processRequest(/* Enter the Content from Packet*/) {
		//Task 0 = Pick up -1 Drop off 
		// For sample Floor is hardcoded to 2
		int task = 0;
		increaseFloor(2 /*Floor*/);
		setMotor(1 /*Direction*/);
		openDoor();
		closeDoor();
		
		
		if(task == 0) {
			setMotor(1 /*Direction*/);
			buttonArray[2].setButtonState(true);
			lightArray[2].setLight(true);
		} else if ( task == -1 ) {
			setMotor(2);
			buttonArray[2].setButtonState(false);
			lightArray[2].setLight(false);
		} else {
			buttonArray[2].setButtonState(false);
			lightArray[2].setLight(false);
			setMotor(2);
		}
		
	}
	private void closeDoor() {
		doorState.setDoorState(false);
		door = false;
		// TODO Auto-generated method stub
		
	}



	private void openDoor() {
		doorState.setDoorState(true);
		door = true;

		// TODO Auto-generated method stub
		
	}



	public String toString() {
		return this.isWaiting + " " + this.currFloor + " "  + this.targetFloor + " " + this.goingUp;
	}

	public static void main(String[] args) {
		Thread elevator1 = new Thread(new Elevator());
		elevator1.start();
		
	}
	
	public int getId() {
		return id;
	}

	public boolean isOperational() {
		return operational;
	}

	public void setOperational(boolean bool) {
		operational = bool;
	}

	public int getDirection() {
		return motor;
	}

	public void setMotor(int direction) {
		System.out.println("Elevator "+getId()+": set motor to "+this.direction);
		motorState.setMotorState((byte)direction);
		if(direction == 0) {
			state.requestUp();
		}else if(direction == 1) {
			state.requestDown();
		}else if(direction == -1){
			state.requestWait();
		}
		
	}
	
	public void increaseFloor(int destination) {
		this.floor = destination;
		System.out.println("Elevator "+getId()+": -> floor "+this.floor);
	}

	public void decreaseFloor(int destination) {
		this.floor = destination;
		System.out.println("Elevator "+getId()+": -> floor "+this.floor);
	}
	
}
