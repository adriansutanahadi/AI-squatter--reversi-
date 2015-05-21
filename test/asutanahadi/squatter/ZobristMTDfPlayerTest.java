package asutanahadi.squatter;

import static org.junit.Assert.*;

import org.junit.Test;

public class ZobristMTDfPlayerTest {
	GenericPlayerTestModule g;
	
	@Test
	public void onePlyvsonePly_7(){
		g = new GenericPlayerTestModule(new ZobristMTDfPlayer(), new ZobristMTDfPlayer(),7);
		try {
			g.refreeTest();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			fail(e.getMessage());

		}
	}
	
	@Test
	public void onePlyvsonePly_6(){
		g = new GenericPlayerTestModule(new ZobristMTDfPlayer(), new ZobristMTDfPlayer(),6);
		try {
			g.refreeTest();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			fail(e.getMessage());

		}
	}

}
