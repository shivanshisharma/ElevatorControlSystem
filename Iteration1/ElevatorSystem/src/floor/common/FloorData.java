/**
 * 
 */
package floor.common;

/**
 * @author fareedahmad
 *
 */
public class FloorData {
	private String time;
	private int floorNumber;
	private boolean up;
	private int carButton;
	
	public FloorData(String time, int floorNumber, boolean up, int carButton) {
		this.time = time;
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

	public String getTime() {
		return time;
	}
	
	public String toString() {
		return time + " " + floorNumber + " " + (up? "Up":"Down") + " " + carButton;
	}
	
}
