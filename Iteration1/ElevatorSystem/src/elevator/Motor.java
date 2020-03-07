package elevator;
/**
 *  Motor Class
 *  motorState = 0 Means its stopped
 *  motorState = 1 Means its Going UP
 *  motorState = 2 Means its Going DOWN
 * */

public class Motor {
	private byte motorState;


	public Motor() {
		
		motorState = 0;
		
	}
	
	public void setMotorState (byte state) {
		
		motorState = state;
	}
	
	
	public int getMotorState() {
		
		return motorState;
		
	}	
	
}
