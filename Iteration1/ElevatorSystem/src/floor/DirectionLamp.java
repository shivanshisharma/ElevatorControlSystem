package floor;

public class DirectionLamp {
	private int direction;
	private boolean active;

	public DirectionLamp(int direction) {
		this.direction = direction;
		this.active = false;
	}
	
	public int getDirection() {
		return direction;
	}
	
	public  boolean isActive() {
		return active;
	}
	
	public void activate() {
		active = true;
	}
	
}
