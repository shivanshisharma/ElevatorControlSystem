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
	private int dir;
	private int carButton;

	public Floor(int floorNumber, int dir, int carButton) {
		this.floorNumber = floorNumber;
		this.dir = dir;
		this.carButton = carButton;
	}

	public int getCarButton() {
		return carButton;
	}

	public int isUp() {
		return dir;
	}

	public int getFloorNumber() {
		return floorNumber;
	}

	public String toString() {
		return floorNumber + " " + dir + " " + carButton;
	}

}
