/**
 * 
 */
package schedule.common;

/**
 * @author fareedahmad
 *
 */
public class Floor {
	private int floorNumber;
	private boolean up;
	private int carButton;

	public Floor(int floorNumber, boolean up, int carButton) {
		this.floorNumber = floorNumber;
		this.up = up;
		this.carButton = carButton;
	}

	public int getCarButton() {
		return carButton;
	}

	public boolean isUp() {
		return up;
	}

	public int getFloorNumber() {
		return floorNumber;
	}

	public String toString() {
		return floorNumber + " " + (up? "Up":"Down") + " " + carButton;
	}

}
