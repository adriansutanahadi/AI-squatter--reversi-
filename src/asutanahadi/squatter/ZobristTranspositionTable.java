package asutanahadi.squatter;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Hashtable;


// Holds the hash table and also encapsulating what is the entry
public class ZobristTranspositionTable {
	private final int TABLE_SIZE = 400;
	private ZobristTableEntry[] table = null;
	
	
	public ZobristTranspositionTable(){
		this.table = new ZobristTableEntry[TABLE_SIZE];
		for (int i = 0;i<TABLE_SIZE;i++){
			table[i] = null;
		}
	}
	
	public ZobristTableEntry getEntry(long hashValue){
		// Need non negative modulo
		ZobristTableEntry entry = table[(((int) hashValue % TABLE_SIZE) + TABLE_SIZE) % TABLE_SIZE];
		if (entry == null){
			return null;
		} else if (entry.hashValue == hashValue) {
			return entry;
		} else {
			return null;
		}
	}
	
	
	public void storeEntry(ZobristTableEntry entry){
		//Always replace the current entry
		this.table[(((int) entry.hashValue % TABLE_SIZE) + TABLE_SIZE) % TABLE_SIZE] = entry;
	}
	
	public static enum ScoreType {
		ACCURATE,
		FAIL_LOW,
		FAIL_HIGH
	}
	
	
	

	
	
}
