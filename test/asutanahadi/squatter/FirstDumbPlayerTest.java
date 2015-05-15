package asutanahadi.squatter;

import static org.junit.Assert.*;
import asutanahadi.squatter.Piece;

import org.junit.Test;

public class FirstDumbPlayerTest {
	GenericPlayerTestModule g;
	
	@Test
	public void dumbVsdumb_7(){
		g = new GenericPlayerTestModule(new FirstDumbPlayer(), new FirstDumbPlayer(),7);
		g.refreeTest();
	}
	
	@Test
	public void dumbVsdumb_6(){
		g = new GenericPlayerTestModule(new FirstDumbPlayer(), new FirstDumbPlayer(),6);
		g.refreeTest();
	}

}
