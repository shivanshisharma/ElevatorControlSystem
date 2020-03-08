package floor;

public class FloorButton {
	private int direction;
	private boolean active;

	public FloorButton(int direction) {
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
