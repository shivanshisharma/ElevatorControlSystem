package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import floor.Floor;

public class FloorTest {
	
	private Floor floor;
	
	@Before
	public void setUp() throws Exception {
		floor = new Floor();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void floorButtonTest() {
		assertFalse(floor.getUpFloorButton().isActive());
		assertFalse(floor.getDownFloorButton().isActive());
		floor.getUpFloorButton().activate();
		floor.getDownFloorButton().activate();
		assertTrue(floor.getUpFloorButton().isActive());
		assertTrue(floor.getDownFloorButton().isActive());
	}
	
	@Test
	public void floorButtonLampTest() {
		assertFalse(floor.getUpLamp().isActive());
		assertFalse(floor.getDownLamp().isActive());
		floor.getUpLamp().activate();
		floor.getDownLamp().activate();
		assertTrue(floor.getUpLamp().isActive());
		assertTrue(floor.getDownLamp().isActive());
	}
	
	@Test
	public void floorLampTest() {
		assertFalse(floor.getUpDirectionLamp().isActive());
		assertFalse(floor.getDownDirectionLamp().isActive());
		floor.getUpDirectionLamp().activate();
		floor.getDownDirectionLamp().activate();
		assertTrue(floor.getUpDirectionLamp().isActive());
		assertTrue(floor.getDownDirectionLamp().isActive());
	}

}
