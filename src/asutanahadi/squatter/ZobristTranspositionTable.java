package asutanahadi.squatter;

import java.awt.Point;
import java.awt.print.Printable;
import java.util.ArrayList;
import java.util.Hashtable;


// Holds the hash table and also encapsulating what is the entry
public class ZobristTranspositionTable {
//	private static int hashCollision = 0;
//	private static int hashNotFound = 0;
//	private static int hashFound = 0;
//	private static int hashSwapped = 0;
//	private static int newHash = 0;
	
	private final int TABLE_SIZE = 500;
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
			//hashNotFound++;

			return null;
		} else if (entry.hashValue == hashValue) {
			//hashFound++;

			return entry;
		} else {
			return null;
		}
	}
	
	
	public void storeEntry(ZobristTableEntry entry){
		//Always replace the current entry
		int index = (((int) entry.hashValue % TABLE_SIZE) + TABLE_SIZE) % TABLE_SIZE;
		if( this.table[index] != null ){
			//hashCollision++;
			if (entry.depth > this.table[index].depth){
				//hashSwapped++;

				this.table[index ] = entry;
			}
		} else {
			//newHash++;
			this.table[index ] = entry;
		}

	}
	
	public static enum ScoreType {
		ACCURATE,
		FAIL_LOW,
		FAIL_HIGH
	}
	
//	public void printStatistic(){
//		System.out.println("Collided " + hashCollision + " Found " + hashFound + " Not Found " + hashNotFound
//			+	" Swapped " + hashSwapped + " newhash " + newHash);
//	}
	
	
	

	
	
}
