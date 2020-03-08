package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import elevator.Elevator;
import elevator.Door.DoorState;
import elevator.Motor.MotorState;

public class ElevatorTest {
	private Elevator elevator;
	@Before
	public void setUp() throws Exception {
		elevator = new Elevator(1);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void elevatorSetupTest() {
		assertTrue(elevator.getId() == 1);
		assertTrue(elevator.getMotor().getMotorState().equals(MotorState.STATIONARY));
		assertTrue(elevator.getDoor().getDoorState().equals(DoorState.OPEN));
	}

	@Test
	public void elevatorMoveUpTest() {
		assertTrue(elevator.getCurrFloor() == 0);
		assertTrue(elevator.getMotor().getMotorState().equals(MotorState.STATIONARY));
		assertTrue(elevator.getDoor().getDoorState().equals(DoorState.OPEN));
		elevator.setState(1);
		assertTrue(elevator.getCurrFloor() == 1);
		assertTrue(elevator.getMotor().getMotorState().equals(MotorState.UP));
		assertTrue(elevator.getDoor().getDoorState().equals(DoorState.CLOSED));
	}
	
	@Test
	public void elevatorMoveDownTest() {
		elevator.setCurrFloor(3);
		assertTrue(elevator.getCurrFloor() == 3);
		assertTrue(elevator.getMotor().getMotorState().equals(MotorState.STATIONARY));
		assertTrue(elevator.getDoor().getDoorState().equals(DoorState.OPEN));
		elevator.setState(2);
		assertTrue(elevator.getCurrFloor() == 2);
		assertTrue(elevator.getMotor().getMotorState().equals(MotorState.DOWN));
		assertTrue(elevator.getDoor().getDoorState().equals(DoorState.CLOSED));
	}
	
	@Test
	public void elevatorStopAtNextFloorTest() {
		elevator.setCurrFloor(3);
		elevator.setMotor(MotorState.UP);
		elevator.setDoor(DoorState.CLOSED);
		assertTrue(elevator.getCurrFloor() == 3);
		assertTrue(elevator.getMotor().getMotorState().equals(MotorState.UP));
		assertTrue(elevator.getDoor().getDoorState().equals(DoorState.CLOSED));
		elevator.setState(0);
		assertTrue(elevator.getCurrFloor() == 4);
		assertTrue(elevator.getMotor().getMotorState().equals(MotorState.STOP));
		assertTrue(elevator.getDoor().getDoorState().equals(DoorState.OPEN));
	}
	
	@Test
	public void elevatorWaitTest() {
		elevator.setCurrFloor(3);
		elevator.setMotor(MotorState.STOP);
		elevator.setDoor(DoorState.CLOSED);
		assertTrue(elevator.getCurrFloor() == 3);
		assertTrue(elevator.getMotor().getMotorState().equals(MotorState.STOP));
		assertTrue(elevator.getDoor().getDoorState().equals(DoorState.CLOSED));
		elevator.setState(3);
		assertTrue(elevator.getCurrFloor() == 3);
		assertTrue(elevator.getMotor().getMotorState().equals(MotorState.STATIONARY));
		assertTrue(elevator.getDoor().getDoorState().equals(DoorState.OPEN));
	}
}
