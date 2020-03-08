package schedule.common;

import java.util.List;

public class Elevator {
	private int id;
	private int currFloor;
	private int destFloor;
	private int state;
	private int passengers;
	private List<Floor> pickUpFloors;
	private List<Integer> dropOffFloors;

	public Elevator(int id, int currFloor,int destFloor, int state, int passengers, List<Floor> pickUpFloors, List<Integer> dropOffFloors) {
		this.id = id;
		this.currFloor = currFloor;
		this.setDestFloor(destFloor);
		this.state = state;
		this.setPassengers(passengers);
		this.setPickUpFloors(pickUpFloors);
		this.setDropOffFloors(dropOffFloors);
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCurrFloor() {
		return currFloor;
	}
	public void setCurrFloor(int currFloor) {
		this.currFloor = currFloor;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}

	public int getPassengers() {
		return passengers;
	}

	public void setPassengers(int passengers) {
		this.passengers = passengers;
	}

	public List<Floor> getPickUpFloors() {
		return pickUpFloors;
	}

	public void setPickUpFloors(List<Floor> pickUpFloors) {
		this.pickUpFloors = pickUpFloors;
	}

	public List<Integer> getDropOffFloors() {
		return dropOffFloors;
	}

	public void setDropOffFloors(List<Integer> dropOffFloors) {
		this.dropOffFloors = dropOffFloors;
	}
	
	public boolean hasPendingTask() {
		return !pickUpFloors.isEmpty() || !dropOffFloors.isEmpty();
	}

	public void update(ElevatorMessage status) {
		this.currFloor = status.getCurrFloor();
		this.state = status.getState();
	}

	public int getDestFloor() {
		return destFloor;
	}

	public void setDestFloor(int destFloor) {
		this.destFloor = destFloor;
	}

}
