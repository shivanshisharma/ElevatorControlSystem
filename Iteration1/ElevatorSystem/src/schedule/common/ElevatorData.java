package schedule.common;

public class ElevatorData {
	private int id;
	private int currFloor;
	private int state;
	
	
	
	public ElevatorData(int id, int currFloor, int state) {
		super();
		this.id = id;
		this.currFloor = currFloor;
		this.state = state;
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
	
	
}
