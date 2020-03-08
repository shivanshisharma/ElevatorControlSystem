package elevator;
/**
 *  Motor Class
 *  motorState = 0 Means its stopped
 *  motorState = 1 Means its Going UP
 *  motorState = 2 Means its Going DOWN
 * */

public class Motor {
	public enum MotorState {
		STOP{
			public byte getValue() {
				return 0;
			}
		},
		UP{
			public byte getValue() {
				return 1;
			}
		},
		DOWN{
			public byte getValue() {
				return 2;
			}
		},
		STATIONARY{
			public byte getValue() {
				return 3;
			}
		};

		public abstract byte getValue();
	}

	private MotorState state;

	public Motor() {
		this.state = MotorState.STATIONARY;
	}

	public void setMotorState (MotorState state) {

		this.state = state;
	}


	public MotorState getMotorState() {

		return state;

	}	

}
