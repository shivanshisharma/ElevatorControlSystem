package elevator;


public class ElevatorSystem {
	
	StateMachine elevaterState;


	public void requestUp() {
		// TODO Auto-generated method stub
		elevaterState.requestUp();
		
	}

	public void requestDown() {
		elevaterState.requestDown();
		// TODO Auto-generated method stub
		
	}

	public void requestWait() {
		elevaterState.requestWait();
		// TODO Auto-generated method stub
		
	}

}
