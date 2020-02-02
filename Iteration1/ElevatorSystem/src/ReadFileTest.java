import static org.junit.Assert.assertFalse;

import org.junit.Test;

import floor.Floor;

public class ReadFileTest {
	@Test
	public void readInputfileTest() {
		Floor floor = new Floor();
		floor.readFile();
		assertFalse(floor.getData().isEmpty());
	}

}
