package asutanahadi.squatter;

import java.awt.Point;
import java.util.Hashtable;


// Holds the hash table and also encapsulating what is the entry
public class ZobristTranspositionTable {
	
	public Hashtable<Long, tableEntry> table = null;
	
	public ZobristTranspositionTable(){
		this.table = new Hashtable<Long, ZobristTranspositionTable.tableEntry>();
	}
	
	public static enum ScoreType {
		ACCURATE,
		FAIL_LOW,
		FAIL_HIGH
	}
	
	
	private class tableEntry {
		private ScoreType scoreType;
		private int score;
		private Point bestMove;
		private int depth;
	}
	
	
}
