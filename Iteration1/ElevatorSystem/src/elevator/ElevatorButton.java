package elevator;

/*
 * Elevator Button Class
 * isSelected = true means the floor is selected 
 * */
public class ElevatorButton {
	private boolean isSelected;
	byte floorNumber;

	public ElevatorButton(byte b) {
		isSelected = false;
		floorNumber = b;
		// TODO Auto-generated constructor stub
	}

	public void setButtonState(boolean state) {

		isSelected = state;

	}
	public boolean getButtonState() {

		return isSelected;
	}

	public byte getFloorNumber() {
		return floorNumber;
	}
}
