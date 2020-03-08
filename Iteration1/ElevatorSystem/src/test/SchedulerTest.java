package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import schedule.Scheduler;
import schedule.common.ElevatorMessage;
import schedule.common.Floor;

public class SchedulerTest {
	
	Scheduler sch;
	@Before
	public void setUp() throws Exception {
		sch = new Scheduler();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void addElevatorMessageTest() {
		assertTrue(sch.getElevatorMessageQueue().isEmpty());
		sch.addElevatorMessage(new ElevatorMessage(0,0,0,0,0,0));
		assertFalse(sch.getElevatorMessageQueue().isEmpty());
	}
	
	@Test
	public void getElevatorMessageTest() {
		ElevatorMessage testData = new ElevatorMessage(0,0,0,0,0,0);
		assertNull(sch.getElevatorMessage());
		sch.addElevatorMessage(testData);
		
		ElevatorMessage retrieveMessage = sch.getElevatorMessage();
		assertNotNull(retrieveMessage);
		assertEquals(retrieveMessage,testData);
	}
	
	@Test
	public void addFloorRequestTest() {
		Floor testData = new Floor(0,0,0);
		assertTrue(sch.getFloorRequestQueue().isEmpty());
		sch.addFloorRequest(testData);
		assertFalse(sch.getFloorRequestQueue().isEmpty());
		assertEquals(testData,sch.getFloorRequestQueue().get(0));
	}

}
