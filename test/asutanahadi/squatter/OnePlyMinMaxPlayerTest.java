package asutanahadi.squatter;

import static org.junit.Assert.*;

import org.junit.Test;

public class OnePlyMinMaxPlayerTest {

	GenericPlayerTestModule g;
	
	@Test
	public void dumbVsdumb_7(){
		g = new GenericPlayerTestModule(new OnePlyMinMaxPlayer(), new OnePlyMinMaxPlayer(),7);
		try {
			g.refreeTest();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			fail(e.getMessage());
			System.exit(1);
		}
	}
	
	@Test
	public void dumbVsdumb_6(){
		g = new GenericPlayerTestModule(new OnePlyMinMaxPlayer(), new OnePlyMinMaxPlayer(),6);
		try {
			g.refreeTest();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			fail(e.getMessage());
			System.exit(1);
		}
	}

}
