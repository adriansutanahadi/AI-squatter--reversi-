package asutanahadi.squatter;

import java.awt.Point;

import asutanahadi.squatter.ZobristTranspositionTable.ScoreType;

/*
 * Entry to be stored in table
 * Note Scoretype is not used in minimax player but used in another experimental player
 * bestMove = best move that could be made given a hash value
 * depth = how deep you recurse in the tree (deeper the better)
 */
public class ZobristTableEntry {
		ScoreType scoreType;
		int minScore;
		int maxScore;
		Point bestMove;
		int depth;
		long hashValue;
}
