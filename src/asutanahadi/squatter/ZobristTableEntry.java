package asutanahadi.squatter;

import java.awt.Point;

import asutanahadi.squatter.ZobristTranspositionTable.ScoreType;

public class ZobristTableEntry {
		ScoreType scoreType;
		int minScore;
		int maxScore;
		Point bestMove;
		int depth;
		long hashValue;
}
