package elevator;

/*
 * Door Class
 * isOpen = True Means the door is open 
 * isOpen = Flase Means the door is close
 * */
public class Door {
	private boolean isOpen;
	
	public Door() {
		isOpen = false;
	}

	public void setDoorState(boolean state) {
		isOpen = state;
		// TODO Auto-generated method stub
		
	}
	
	public boolean getDoorState() {
		return isOpen;
		
	}

}
