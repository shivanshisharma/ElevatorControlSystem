package schedule.common;

import java.util.List;

public class Elevator {
	private int id;
	private int currFloor;
	private int state;
	private int passengers;
	private List<Integer> pickUpFloors;
	private List<Integer> dropOffFloors;

	public Elevator(int id, int currFloor, int state, int passengers, List<Integer> pickUpFloors, List<Integer> dropOffFloors) {
		this.id = id;
		this.currFloor = currFloor;
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

	public List<Integer> getPickUpFloors() {
		return pickUpFloors;
	}

	public void setPickUpFloors(List<Integer> pickUpFloors) {
		this.pickUpFloors = pickUpFloors;
	}

	public List<Integer> getDropOffFloors() {
		return dropOffFloors;
	}

	public void setDropOffFloors(List<Integer> dropOffFloors) {
		this.dropOffFloors = dropOffFloors;
	}

	public void update(Elevator elevator) {
		this.currFloor = elevator.currFloor;
		this.state = elevator.state;
		this.passengers = elevator.passengers;
		this.dropOffFloors = elevator.dropOffFloors;
		this.pickUpFloors = elevator.pickUpFloors;
	}

}
