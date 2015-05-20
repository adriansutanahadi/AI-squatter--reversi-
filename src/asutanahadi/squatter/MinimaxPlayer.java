package asutanahadi.squatter;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;

import asutanahadi.squatter.Board.CellContent;

public class MinimaxPlayer extends FirstDumbPlayer {
	
	/* Function called by referee to request a move by the player.
	 *  Return object of class Move
	 */
	
	ZobristBoard b;
	ZobristHash z;
	private int depth = 5;
	private ZobristTranspositionTable tTable = null;
	@Override
	public int init(int n, int p) {
		
		if ((p == Piece.WHITE || p == Piece.BLACK) && n > 0) {
			playerSide = p;
			this.z = new ZobristHash(n);
			b = new ZobristBoard(n,this.z);
			
			super.b = this.b;
			// Board can be white,black ,white captured,black captured or empty

			this.tTable = new ZobristTranspositionTable();
			
			return 0;
		} 
		else {
			return -1;
		}
	}
	
	public Move makeMove() {
		Move m = new Move();
		
		update_depth();
		Point best_move = minimax_decision(b, depth);
		m.P = this.playerSide;
		m.Col = best_move.x;
		m.Row = best_move.y;
		b.addPiece(m.Col, m.Row, playerSidetoBoardSide(true));
		// check if there is a piece on that position.
		return m;
	}
	
	private void update_depth(){
		int total_round = b.getDimension() * b.getDimension();

		int early_stage = total_round - total_round/3;
		int middle_stage = total_round - (total_round/3)*2;
		if (b.freeCellCount >= early_stage){
			this.depth = 5;
		} else if (b.freeCellCount >= middle_stage){
			this.depth = 5;
		} else {
			this.depth = 5;
		}

	
		
	}
	
	// Evaluate all possible moves with depth n and return the best one
	// Before recursing, check if it is inside table entry, 
	// If inside then return the table content else recurse and add to table;
	private Point minimax_decision(ZobristBoard state, int depth){
		ArrayList<Point> moves = state.getMove();
		ArrayList<Integer> move_value = new ArrayList<Integer>();
		ZobristTableEntry entry = tTable.table.get(state.board_hash);
		if (entry == null){
			for (Point move : moves) {
				
				ZobristBoard currentBoard = new ZobristBoard(state,this.z);
				currentBoard.addPiece(move.x, move.y, playerSidetoBoardSide(true));
				ZobristTableEntry currentBoardHash = tTable.table.get(currentBoard.board_hash);
				if (currentBoardHash == null){
					move_value.add(minimax_value(currentBoard, depth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, true));
				} else {
					move_value.add(currentBoardHash.maxScore);
				}
			}
			int maxScore = Collections.max(move_value);
			Point bestMove = moves.get(move_value.indexOf(maxScore));
			ZobristTableEntry tmpEntry = tTable.table.get(state.board_hash);
			if (tmpEntry == null){
				ZobristTableEntry tableEntry = new ZobristTableEntry();
				tableEntry.maxScore = maxScore;
				tableEntry.bestMove = bestMove;
				tableEntry.depth = depth;
			} else {
				tmpEntry.bestMove = bestMove;
			}
			return bestMove;
		} else{
			return entry.bestMove;
		}
	}
	
	private int minimax_value(ZobristBoard state, int depth, int alpha, int beta, boolean maximizingPlayer) {
		if (depth == 0 || state.isFinished()) {
			int score = evaluate(state, playerSidetoBoardSide(maximizingPlayer));
			ZobristTableEntry entry = new ZobristTableEntry();
			if (maximizingPlayer){
				entry.maxScore = score;
				entry.depth = depth;
			} else{
				entry.minScore = score;
				entry.depth = depth;
			}
			tTable.table.put(state.board_hash, entry);
			return score;
		}
		ArrayList<Point> moves = state.getMove();
		ZobristTableEntry entry = tTable.table.get(state.board_hash);
		if (maximizingPlayer) {
			
			if (entry == null){
				Integer bestValue = Integer.MIN_VALUE;
				for (Point move : moves) {
					int val;
					ZobristBoard currentBoard = new ZobristBoard(state,this.z);
					currentBoard.addPiece(move.x, move.y, playerSidetoBoardSide(maximizingPlayer));
					ZobristTableEntry currentBoardHash = tTable.table.get(currentBoard.board_hash);
					if (currentBoardHash == null){
						val = minimax_value(currentBoard, depth - 1, alpha, beta, false);
					} else {
						val = currentBoardHash.maxScore;
					}
					
					bestValue = Math.max(bestValue, val);
					alpha = Math.max(alpha, bestValue);
					if (beta <= alpha) {
						break;
					}
				}
				return bestValue;
			} else {
				return entry.maxScore;
			}
			
		} else {
			if (entry == null){
			Integer bestValue = Integer.MAX_VALUE;
			Collections.reverse(moves);
			for (Point move : moves) {
				int val;
				ZobristBoard currentBoard = new ZobristBoard(state,this.z);
				currentBoard.addPiece(move.x, move.y, playerSidetoBoardSide(maximizingPlayer));
				ZobristTableEntry currentBoardHash = tTable.table.get(currentBoard.board_hash);
				if (currentBoardHash == null){
					val = minimax_value(currentBoard, depth - 1, alpha, beta, true);
				} else {
					val = currentBoardHash.minScore;
				}
				bestValue = Math.min(bestValue, val);
				beta = Math.min(beta, bestValue);
				if (beta <= alpha) {
					break;
				}
			}
				return bestValue;
			}else {
				return entry.minScore;
			}
		}
	}
	
	//Simple Generic Evaluation Function while focusing on preventing getting captured
	private int evaluate(Board b, Board.CellContent side){
		int w1 = 100;
		int w2 = 90;
		int sign = 0;
		int black_side_score = 0;
		int white_side_score = 0;
		if (side == Board.CellContent.WHITE) {

			sign = 1;
		} else if (side == Board.CellContent.BLACK) {

			sign = -1;
		}
		for (int i = 0; i <b.getDimension(); i++) {
			for (int j = 0; j < b.getDimension(); j++) {
				CellContent content = b.grid[i][j];
				if (j ==  0 || i==0 || j == b.getDimension() || i == b.getDimension()){
					if (content == CellContent.BLACK){
						black_side_score++;
					} else if(content == CellContent.WHITE){
						white_side_score++;
					}
				}
				
			}
		}
		int score = ((w1*sign * b.getWhiteScore())  + (w1*-sign * b.getBlackScore())) + w2*-sign*black_side_score + w2*sign*white_side_score;
		return score;
	}
}
