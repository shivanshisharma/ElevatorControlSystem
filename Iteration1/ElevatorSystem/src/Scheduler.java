import java.util.ArrayList;
import java.util.Collections;

/**
 * 
 */

/**
 * @author Fareed Ahmad 
 * Tareq Hanafi - 101036095
 * Jaskaran Singh 
 * Shivanshi Sharma
 * @param <Objects>
 *
 */
public class Scheduler {

	private ArrayList<Integer> schedulerElevator = new ArrayList<>();
	private int index;
	
	//If false, it heads down, if true, it heads up.
	private Boolean DirEl;
	//If stopped
	private Boolean stop;
	
	
	/**
	 * @param args
	 */
	
	/*
	 * This function adds a 
	 */
	public void addElevatorRequest(Integer item) {
		if(schedulerElevator.contains(item)){
			 System.out.println("Request already received");
			 return;
		}
		
		schedulerElevator.add(item);
	}
	
	public boolean checkRequests(Integer item) {
		if(schedulerElevator.contains(item)) {
			System.out.println("");
			return true;
		}
		return false;
	}
	
	public ArrayList<Integer> sortAscending(){
		Collections.sort(schedulerElevator);
		return schedulerElevator;
	}
		 
	
	

}
