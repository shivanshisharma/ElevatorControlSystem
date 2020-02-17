package elevator;

public interface StateMachine {
	void requestUp();
	void requestDown();
	void requestWait();
	void arrived();
	public void RequestOpenDoor();
	public void RequestCloseDoor();
}
