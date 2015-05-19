package asutanahadi.squatter;

import java.awt.Point;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import javax.naming.directory.SearchResult;


// Idea for Zohrist from AI for games second edition
public class ZobristMTDfPlayer extends FirstDumbPlayer {
	
	ZobristBoard b;
	private ZobristTranspositionTable tTable = null;
	@Override
	public int init(int n, int p) {
		
		if ((p == Piece.WHITE || p == Piece.BLACK) && n > 0) {
			playerSide = p;
			b = new ZobristBoard(n);
			super.b = this.b;
			// Board can be white,black ,white captured,black captured or empty

			this.tTable = new ZobristTranspositionTable();
			//initZobristKey();
			return 0;
		} 
		else {
			return -1;
		}
	}
	private class SearchResult {
		public int score;
		public Point bestMove;
		
		public SearchResult(int score,Point bestMove){
			this.score = score;
			this.bestMove = bestMove;
		}
	}
	//Test Driver is a test search for mtd-f
	//Adapted from Ai for games by Elsevier Millington
	private SearchResult test(ZobristBoard searchBoard,int maxDepth,int currentDepth,int gamma){
		int lowestDepth = -1;
		if (currentDepth > lowestDepth) lowestDepth = currentDepth;
		// Lookup the entry from the transposition table
		ZobristTableEntry entry = tTable.table.get(searchBoard.board_hash);
		if (entry == null) entry = new ZobristTableEntry();
		if ( (entry != null) && (entry.depth > maxDepth - currentDepth)){
			if (entry.minScore > gamma) {
				return new SearchResult(entry.minScore, entry.bestMove);
			} else if (entry.maxScore < gamma) {
				return new SearchResult(entry.maxScore , entry.bestMove);
			} else {
				// We need to create the entry
				entry.depth = maxDepth - currentDepth;
				entry.minScore = Integer.MIN_VALUE;
				entry.maxScore = Integer.MAX_VALUE;
				this.tTable.table.put(searchBoard.board_hash,entry);
			}
		}
		
		// Check if we're done recursing
		if (searchBoard.isFinished() || currentDepth == maxDepth){
			//evaluate maybe wrong
			entry.minScore =  evaluate(searchBoard, this.playerSide);
			entry.maxScore = evaluate(searchBoard, this.playerSide);
			this.tTable.table.put(searchBoard.board_hash,entry);
			return new SearchResult(entry.minScore,null);
		}
		

		// Now go into bubbling mode
		Point bestMove = null;
		int bestScore = Integer.MIN_VALUE;
	
		ArrayList<Point> moves = searchBoard.getMove();
		if (gamma <0){
			Collections.reverse(moves);
		}
		for (Point m: moves) {
			ZobristBoard new_board = new ZobristBoard(b.getDimension());
			Board.copy_grid(new_board,searchBoard);
			new_board.addPiece(m.x, m.y, playerSidetoBoardSide());
			
			//Recurse
			SearchResult recursedResult = test(new_board, maxDepth, currentDepth+1, -gamma);
			int recursedScore = recursedResult.score;
			Point currentMove = recursedResult.bestMove;
			
			int currentScore = -recursedScore;
			
			if (currentScore > bestScore) {
				//disrespency with the book
				//book uses m here we use bestMove
				entry.bestMove = currentMove;
				bestScore = currentScore;
				bestMove = m;
			}

		}
		
		if (bestScore < gamma) {
			entry.maxScore = bestScore;
		} else {
			entry.minScore = bestScore;
		}
		// Store the entry and return the best score and move.
		this.tTable.table.put(searchBoard.board_hash,entry);
		return new SearchResult(bestScore,bestMove);
	}
	
	private Point mtd(ZobristBoard b,int maxDepth,int guess){
		Point move = null;
		SearchResult result;
		while(true){
			int gamma = guess;
			result = test(b, maxDepth, 0, gamma-1);
			guess = result.score;
			move = result.bestMove;
			if (gamma == guess )break;
		}
		
		if (move == null){
			move = b.getMove().get(0);
		}
		return move;
		
	}
	
	
	
	public Move makeMove() {
		Move m = new Move();


		
		Point best_move = mtd(b, 2, Integer.MAX_VALUE);
		
		m.P = this.playerSide;
		m.Col = best_move.x;
		m.Row = best_move.y;
		b.addPiece(m.Col, m.Row, playerSidetoBoardSide());
		// check if there is a piece on that position.
		return m;
	}
	

	
	//Simple Generic Evaluation Function while focusing on preventing getting captured
	private int evaluate(Board b,int side){
		int k1 = 0;
		int k2 = 0;
		if (side == WHITE) {
			k1 = 1;
			k2 = -1 * 2;
		} else if (side == BLACK) {
			k1 = -1 * 2;
			k2 = 1;
		}
		for (int i = 0; i < b.getDimension(); i++) {
			for (int j = 0; j < b.getDimension(); j++) {
				
			}
		}
		
		return k1 * b.getWhiteScore() + k1 + k2 * b.getBlackScore() + k2;
	
	}
}
