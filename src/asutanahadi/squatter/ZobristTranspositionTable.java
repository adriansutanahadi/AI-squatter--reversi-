package asutanahadi.squatter;

import java.awt.Point;
import java.util.Hashtable;


// Holds the hash table and also encapsulating what is the entry
public class ZobristTranspositionTable {
	
	public Hashtable<Long, ZobristTableEntry> table = null;
	
	public ZobristTranspositionTable(){
		this.table = new Hashtable<Long, ZobristTableEntry>();
	}
	
	public static enum ScoreType {
		ACCURATE,
		FAIL_LOW,
		FAIL_HIGH
	}
	
	

	
	
}
