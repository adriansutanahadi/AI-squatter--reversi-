package asutanahadi.squatter;

import static org.junit.Assert.*;
import asutanahadi.squatter.*;

import org.junit.Test;

public class FirstDumbPlayerTest {
	GenericPlayerTestModule g;
	
	@Test
	public void dumbVsdumb_7(){
		g = new GenericPlayerTestModule(new FirstRandomPlayer(), new FirstRandomPlayer(),7);
		try {
			g.refreeTest();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			fail(e.getMessage());

		}
	}
	
	@Test
	public void dumbVsdumb_6(){
		g = new GenericPlayerTestModule(new FirstRandomPlayer(), new FirstRandomPlayer(),6);
		try {
			g.refreeTest();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			fail(e.getMessage());

		}
	}

}
