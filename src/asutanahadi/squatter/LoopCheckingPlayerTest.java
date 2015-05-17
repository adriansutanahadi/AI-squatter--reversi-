package asutanahadi.squatter;

import static org.junit.Assert.*;
import asutanahadi.squatter.*;

import org.junit.Test;

public class LoopCheckingPlayerTest {
	GenericPlayerTestModule g;

	@Test
	public void loopVsLoop_7(){
		g = new GenericPlayerTestModule(new LoopCheckingPlayer(), new LoopCheckingPlayer(),7);
		try {
			g.refreeTest();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			fail(e.getMessage());

		}
	}
	
	@Test
	public void loopVsLoop_6(){
		g = new GenericPlayerTestModule(new LoopCheckingPlayer(), new LoopCheckingPlayer(),6);
		try {
			g.refreeTest();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			fail(e.getMessage());

		}
	}
}
