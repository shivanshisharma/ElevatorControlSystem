package elevator;

public class ElevatorLight {
	private boolean isOn;

	void light() {
		isOn = false;
		// TODO Auto-generated method stub
		
	}
	public void setLight(boolean status) {
		isOn = status;
	}
	public boolean getLightStatus() {
		return isOn;
	}

}
