package elevator;




/*
 * Door Class
 * isOpen = True Means the door is open 
 * isOpen = Flase Means the door is close
 * */
public class Door {
	public enum DoorState {
		Open{
			public byte getByteValue() {
				return 1;
			}
		},
		Closed{
			public byte getByteValue() {
				return 0;
			}
		};

		public abstract byte getByteValue();
	}
	private DoorState state;

	public Door() {
		state = DoorState.Closed;
	}

	public void setDoorState(DoorState state) {
		this.state = state;
		// TODO Auto-generated method stub

	}

	public DoorState getDoorState() {
		return state;
	}

}
