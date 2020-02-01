import java.util.stream.IntStream;

/**
 * 
 */

/**
 * @author Fareed Ahmad 
 * Tareq Hanafi 
 * Jaskaran Singh 
 * Shivanshi Sharma
 *
 */
public class ElevatorSubsystem implements Runnable{

	/**
	 * @param args
	 */
	private int position;
	private Boolean door;
	private Scheduler s;
	private int[] floors = new int[SIZE];
	private static final int SIZE = 10;
	private int index;
	
	public ElevatorSubsystem(Scheduler s) {
		this.s = s;
		floors = IntStream.range(1,10).toArray();
		index = 0;
	}

	@Override
	public void run() {
		
		while(true) {
			
			
		}

	}
	
	
	
	
	


	
	

}
