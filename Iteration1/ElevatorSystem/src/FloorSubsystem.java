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

public class FloorSubsystem implements Runnable{

	/**
	 * @param args
	 */
	private Boolean upButton;
	private Boolean downButton;
	private int position;
	private int[] floors = new int[SIZE];
	private static final int SIZE = 10;
	private Scheduler s;
	
	public FloorSubsystem(Scheduler s) {
		this.s = s;
		floors = IntStream.range(1,10).toArray();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
