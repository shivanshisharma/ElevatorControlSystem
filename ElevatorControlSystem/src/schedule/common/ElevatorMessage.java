package schedule.common;

public class ElevatorMessage {
	private int port;
	private int id;
	private int currFloor;
	private int state;
	private int nextFloor;
	private int targetFloor;
	
	
	
	public ElevatorMessage(int port, int id,int state, int currFloor, int nextFloor, int targetFloor) {
		super();
		this.port = port;
		this.id = id;
		this.currFloor = currFloor;
		this.state = state;
		this.nextFloor = nextFloor;
		this.targetFloor = targetFloor;
	}
	
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
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
	public int getNextFloor() {
		return nextFloor;
	}
	public void setNextFloor(int nextFloor) {
		this.nextFloor = nextFloor;
	}
	public int getTargetFloor() {
		return targetFloor;
	}
	public void setTargetFloor(int targetFloor) {
		this.targetFloor = targetFloor;
	}
	
	public String toString() {
		return "id: " + id +  " currFloor: " + this.currFloor + " state: " + this.state + " nextFloor: "  + this.nextFloor+ " targetFloor: "  + this.targetFloor;
	}
	
}
