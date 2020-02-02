import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import elevator.Elevator;
import floor.Floor;
import schedule.Scheduler;

/**
 * 
 */

/**
 * @author fareedahmad
 *
 */
public class PacketTest {

	private Floor floor;
	private Scheduler scheduler;
	private Elevator elevator;
	
	@Before
	public void beforeAllTestMethods() {
		floor = new Floor();
		scheduler = new Scheduler();
		elevator = new Elevator();
	}

	@Test
	public void readInputfileTest() {
		fail("Not yet implemented");
	}

	@Test
	public void floorToSchedulerTest() {
		
	}
}
