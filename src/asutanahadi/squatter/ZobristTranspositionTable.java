package asutanahadi.squatter;

import java.awt.Point;
import java.awt.print.Printable;
import java.util.ArrayList;
import java.util.Hashtable;


/*
 *  Holds the hashtable entry
 *  Adatped from Artificial intelligence for games
 *  Transposition Table act as a memoization (dynamic programming) for all the moves that we opened up.
 *  That saying we don't need to open a previously checked node thus saving the time to traverse.
 *  It identifies a state by it's hash value.
 *  Get Entry O(1)
 *  Store Entry O(1)
 *  It just uses TABLE_SIZE amount of memory + entries.
 *  
 */
public class ZobristTranspositionTable {

	
	private final int TABLE_SIZE = 500;
	private ZobristTableEntry[] table = null;
	
	
	public ZobristTranspositionTable(){
		this.table = new ZobristTableEntry[TABLE_SIZE];
		for (int i = 0;i<TABLE_SIZE;i++){
			table[i] = null;
		}
	}
	
	public ZobristTableEntry getEntry(long hashValue){

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
		int index = (((int) entry.hashValue % TABLE_SIZE) + TABLE_SIZE) % TABLE_SIZE;
		if( this.table[index] != null ){

			if (entry.depth > this.table[index].depth){


				this.table[index ] = entry;
			}
		} else {
			//newHash++;
			this.table[index ] = entry;
		}

	}
	
	//Used in experimental player(MTDF player) but not in minimax.
	public static enum ScoreType {
		ACCURATE,
		FAIL_LOW,
		FAIL_HIGH
	}

	
	
}
