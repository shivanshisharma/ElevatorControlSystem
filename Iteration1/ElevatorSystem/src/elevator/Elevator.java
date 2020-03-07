
package elevator;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import elevator.Door.DoorState;
import elevator.Motor.MotorState;
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
	private int targetFloor;
	private boolean operational;
	private int nextFloor;

	private DatagramPacket elevatorDataPacket, instructionPacket;
	private DatagramSocket sendReceiveSocket;

	private ElevatorButton button1, button2, button3, button4, button5;
	private ElevatorLight light1, light2, light3, light4, light5; 

	private Motor motor;
	private ElevatorButton [] buttonArray;
	private ElevatorLight[] lightArray;
	private String direction;
	private ElevatorSubsystem state;
	private Door door; 
	private static final int WAIT = 2000;

	public Elevator(int id) {

		try {
			// Construct a datagram socket to send and receive
			sendReceiveSocket = new DatagramSocket();
		} catch (SocketException se) { // Can't create the socket.
			se.printStackTrace();
			System.exit(1);
		}

		this.id =id;
		this.operational = true;
		this.currFloor = 0;
		this.targetFloor = 0;
		this.nextFloor = 0;

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

		door = new Door();
		motor = new Motor();
		state = new ElevatorSubsystem();
	}

	@Override
	public void run() {
		while(true) {
			// Create datagram packet
			elevatorDataPacket = this.createPacket(this.toMessage(), 2);

			// Print out info that is in the packet before sending
			//this.printPacket(elevatorDataPacket);

			// Send the datagram packet to the host on port 23
			this.sendPacket(sendReceiveSocket, elevatorDataPacket, "Elevator");

			// Receive response packet
			byte data[] = new byte[10];
			instructionPacket = new DatagramPacket(data, data.length);
			this.receivePacket(sendReceiveSocket, instructionPacket, "Elevator");
		
			setState(data[0]);

		}
	}
	
	private void setState(int instruction) {
		switch (instruction) {
			case 0: // Scheduler sent stop command
				
				System.out.println("Stopping at next floor");
				if(motor.getMotorState().equals(MotorState.DOWN)) {
					currFloor --;
				}else if(motor.getMotorState().equals(MotorState.UP)) {
					currFloor++;
				}
				motor.setMotorState(MotorState.STOP);
				try {
					Thread.sleep(WAIT);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("Opening doors");
				door.setDoorState(DoorState.OPEN);
				System.out.println("Stopped at: " + currFloor);
				//wait for people to get on
				break;
			case 1: //scheudler sent up command
				System.out.println("Closing doors");
				door.setDoorState(DoorState.CLOSED);
				motor.setMotorState(MotorState.UP);
				System.out.println("Going up from floor " + currFloor);
				
				try {
					Thread.sleep(WAIT);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				currFloor++;
				System.out.println("Reached floor " + currFloor);
				break;
			case 2: //scheduler sent down command
				System.out.println("Closing doors");
				door.setDoorState(DoorState.CLOSED);
				motor.setMotorState(MotorState.DOWN);
				System.out.println("Going down from floor " + currFloor);
				try {
					Thread.sleep(WAIT);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				currFloor --;
				System.out.println("Reached floor " + currFloor);
				break;
			case 3: //scheduler sent wait command
				System.out.println("Waiting for insttruction at floor " + currFloor);
				motor.setMotorState(MotorState.STATIONARY);
				try {
					Thread.sleep(WAIT);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				door.setDoorState(DoorState.OPEN);
				break;
		}
		
	}
	// This class Process the Request received from Scheduler and calls approproate methods.
	public void processRequest(/* Enter the Content from Packet*/) {
		//Task 0 = Pick up -1 Drop off 
		// For sample Floor is hardcoded to 2
		/*
		int task = 0;
		increaseFloor(2);
		setMotor(1);
		openDoor();
		closeDoor();


		if(task == 0) {
			setMotor();
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
		 */

	}
	private void closeDoor() {
		door.setDoorState(DoorState.CLOSED);
	}

	private void openDoor() {
		door.setDoorState(DoorState.OPEN);
	}

	public void setMotor(MotorState direction) {
		System.out.println("Elevator "+getId()+": set motor to "+this.direction);
		motor.setMotorState(direction);
	}

	public MotorState getDirection() {
		return motor.getMotorState();
	}

	public void increaseFloor(int destination) {
		this.currFloor = destination;
		System.out.println("Elevator "+getId()+": -> floor "+this.currFloor);
	}

	public void decreaseFloor(int destination) {
		this.currFloor = destination;
		System.out.println("Elevator "+getId()+": -> floor "+this.currFloor);
	}

	public String toString() {
		return "id: " + id +  " currFloor: " + this.currFloor + " motor: " + this.motor.getMotorState() + " nextFloor: "  + this.nextFloor+ " targetFloor: "  + this.targetFloor;
	}
	
	public byte[] toMessage() {
		byte message[] = new byte[5];
		message[0] = (byte) id;
		message[1] = motor.getMotorState().getValue();
		message[2] = (byte) currFloor;
		message[3] = (byte) nextFloor;
		message[4] = (byte) targetFloor;
		return message;
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

	public static void main(String[] args) {
		Thread elevator1 = new Thread(new Elevator(1));
		elevator1.start();	
	}
}
