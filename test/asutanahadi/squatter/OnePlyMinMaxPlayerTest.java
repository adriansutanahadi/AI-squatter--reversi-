package asutanahadi.squatter;

import static org.junit.Assert.*;

import org.junit.Test;

public class OnePlyMinMaxPlayerTest {

	GenericPlayerTestModule g;
	
	@Test
	public void dumbVsdumb_7(){
		g = new GenericPlayerTestModule(new OnePlyMinMaxPlayer(), new OnePlyMinMaxPlayer(),7);
		g.refreeTest();
	}
	
	@Test
	public void dumbVsdumb_6(){
		g = new GenericPlayerTestModule(new OnePlyMinMaxPlayer(), new OnePlyMinMaxPlayer(),6);
		g.refreeTest();
	}

}
