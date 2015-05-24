package asutanahadi.squatter;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;

public class MinimaxPlayer extends FirstDumbPlayer {
	
	private ZobristBoard b;		
	ZobristHash z;
	
	
	private ZobristTranspositionTable tTable;	
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

	
	/* Function called by referee to request a move by the player.
	 *  Return object of class Move
	 */
	public Move makeMove() {
		Move m = new Move();
		int depth = (int) (7.5 - Math.pow(((b.getFreeCellCount() * 2) / Math.pow(b.getDimension(), 2)), 2));

		Point best_move = minimax_decision(b, depth);
		m.P = this.playerSide;
		m.Col = best_move.x;
		m.Row = best_move.y;
		b.addPiece(m.Col, m.Row, playerSidetoBoardSide(true));
		// check if there is a piece on that position.
		return m;
	}
	
	// Evaluate all possible moves with depth 1 and return the best one
	private Point minimax_decision(ZobristBoard state, int depth){
		int alpha = Integer.MIN_VALUE;
		int beta = Integer.MAX_VALUE;
		int bestScore = Integer.MIN_VALUE;
		Point bestMove = null;
		ZobristTableEntry entry = tTable.getEntry(state.board_hash);

		ArrayList<Point> moves = state.getMove();
		if (entry != null){
			Point entryBestMove = entry.bestMove;
			move_order(entryBestMove, moves);
			//TODO
			//Move Order
		}
		for (Point move : moves) {
			if (bestMove == null) {
				bestMove = move;
			}
			ZobristBoard currentBoard = new ZobristBoard(state,this.z);
			currentBoard.addPiece(move.x, move.y, playerSidetoBoardSide(true));
			alpha = Math.max(alpha, minimax_value(currentBoard, depth - 1, alpha, beta, false));
			if (alpha > bestScore) {
				bestMove = move;
				bestScore = alpha;


			}
		}
		return bestMove;
	}
	
	private int minimax_value(ZobristBoard state, int depth, int alpha, int beta, boolean maximizingPlayer) {
		if (depth == 0 || state.isFinished()) {

			return evaluate(state, playerSidetoBoardSide(maximizingPlayer));
		}
		ArrayList<Point> moves = state.getMove();
		ZobristTableEntry entry = tTable.getEntry(state.board_hash);
		if (entry != null){
			//TODO
			//Move Order
		}
		if (maximizingPlayer) {
			Integer bestValue = Integer.MIN_VALUE;
			for (Point move : moves) {
				ZobristBoard currentBoard = new ZobristBoard(state,this.z);
				currentBoard.addPiece(move.x, move.y, playerSidetoBoardSide(maximizingPlayer));
				int val = minimax_value(currentBoard, depth - 1, alpha, beta, false);
				bestValue = Math.max(bestValue, val);
				alpha = Math.max(alpha, bestValue);
				if (beta <= alpha) {
					break;

				}
			}
			return bestValue;
		} else {
			Integer bestValue = Integer.MAX_VALUE;

			for (Point move : moves) {
				ZobristBoard currentBoard = new ZobristBoard(state,this.z);
				currentBoard.addPiece(move.x, move.y, playerSidetoBoardSide(maximizingPlayer));

				int val = minimax_value(currentBoard, depth - 1, alpha, beta, true);

				bestValue = Math.min(bestValue, val);
				beta = Math.min(beta, bestValue);
				if (beta <= alpha) {
					break;
				}
			}
			return bestValue;
		}
	}
	
	//Move order the array of moves so that the best one will be opened first
	private void move_order(Point highPriorityMove, ArrayList<Point> moves){
		int bestIndex = 0;
		int i = 0;
		for (Point move : moves) {
			i++;
			if (moves.equals(highPriorityMove)){
				bestIndex = i;
			}
		}
		//Swap the position
		Collections.swap(moves, bestIndex, 0);
	}
	
	//Simple Generic Evaluation Function while focusing on preventing getting captured
	private int evaluate(Board b, Board.CellContent side){
		int k1 = 0;
		int k2 = 0;
		if (side == Board.CellContent.WHITE) {
			k1 = 1;
			k2 = -1 * 2;
		} else if (side == Board.CellContent.BLACK) {
			k1 = -1 * 2;
			k2 = 1;
		}
		return ((k1 * b.getWhiteScore()) + k1 + (k2 * b.getBlackScore()) + k2);
	}
}
