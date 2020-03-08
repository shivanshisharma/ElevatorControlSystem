package elevator;




/*
 * Door Class
 * isOpen = True Means the door is open 
 * isOpen = Flase Means the door is close
 * */
public class Door {
	public enum DoorState {
		OPEN{
			public byte getByteValue() {
				return 1;
			}
		},
		CLOSED{
			public byte getByteValue() {
				return 0;
			}
		};

		public abstract byte getByteValue();
	}
	private DoorState state;

	public Door() {
		state = DoorState.CLOSED;
	}

	public void setDoorState(DoorState state) {
		this.state = state;

	}

	public DoorState getDoorState() {
		return state;
	}

}
