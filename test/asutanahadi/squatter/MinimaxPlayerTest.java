package asutanahadi.squatter;

import static org.junit.Assert.*;
import asutanahadi.squatter.*;

import org.junit.Test;

public class MinimaxPlayerTest {
	GenericPlayerTestModule g;

	@Test
	public void minimaxVsMinimax_7(){
		g = new GenericPlayerTestModule(new MinimaxPlayer(), new MinimaxPlayer(),7);
		try {
			g.refreeTest();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			fail(e.getMessage());

		}
	}
	
	@Test
	public void minimaxVsMinimax_6(){
		g = new GenericPlayerTestModule(new MinimaxPlayer(), new MinimaxPlayer(),6);
		try {
			g.refreeTest();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			fail(e.getMessage());

		}
	}
}
