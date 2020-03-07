package elevator;
/**
 *  Motor Class
 *  motorState = 0 Means its stopped
 *  motorState = 1 Means its Going UP
 *  motorState = 2 Means its Going DOWN
 * */

public class Motor {
	public enum MotorState {
		GoingUp{
			public byte getByteValue() {
				return 1;
			}
		},
		GoingDown{
			public byte getByteValue() {
				return 2;
			}
		},
		Stopped{
			public byte getByteValue() {
				return 0;
			}
		};
		
		public abstract byte getByteValue();
	}
	
	private MotorState state;
	
	public Motor() {
		this.state = MotorState.Stopped;
	}
	
	public void setMotorState (MotorState state) {
		
		this.state = state;
	}
	
	
	public MotorState getMotorState() {
		
		return state;
		
	}	
	
}
