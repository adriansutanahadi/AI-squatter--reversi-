package asutanahadi.squatter;

import java.awt.Point;
import java.security.KeyStore.Entry;
import java.util.ArrayList;
import java.util.Collections;

public class MinimaxPlayer extends FirstDumbPlayer {
	
	private ZobristBoard b;		
	private ZobristHash z;
	private int depth;
	
	
	
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

			
//			//Test function
//			ZobristBoard currentBoard = new ZobristBoard(this.b,this.z);
//			currentBoard.addPiece(0, 0, playerSidetoBoardSide(true));
//			currentBoard.addPiece(1, 0, playerSidetoBoardSide(false));
//			currentBoard.addPiece(0, 1, playerSidetoBoardSide(true));
//			System.out.println(currentBoard.board_hash);
//
//			
//			ZobristBoard similarBoard = new ZobristBoard(this.b,this.z);
//			similarBoard.addPiece(0, 1, playerSidetoBoardSide(true));
//			similarBoard.addPiece(1, 0, playerSidetoBoardSide(false));
//			similarBoard.addPiece(0, 0, playerSidetoBoardSide(true));
//			System.out.println(similarBoard.board_hash);
//			
//			
//			ZobristBoard differentBoard = new ZobristBoard(this.b,this.z);
//			differentBoard.addPiece(0, 2, playerSidetoBoardSide(true));
//			differentBoard.addPiece(1, 0, playerSidetoBoardSide(false));
//			differentBoard.addPiece(0, 0, playerSidetoBoardSide(true));
//			System.out.println(differentBoard.board_hash);			
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
		this.depth = (int) (7.5 - Math.pow(((b.getFreeCellCount() * 2) / Math.pow(b.getDimension(), 2)), 2)) * 3 /2;
		if (this.b.getDimension() >= 7){
			if (b.getFreeCellCount() < 12){
				this.depth = 16;
			}			
			else if (b.getFreeCellCount() < 13){
				this.depth = 14;
			}
			else if (b.getFreeCellCount() < 14){
				this.depth = 12;
				
			} else if (b.getFreeCellCount() < 16){
				this.depth = 10;
			}	else if (b.getFreeCellCount() < 20){
				this.depth = 8;
			}
			else{
				this.depth = (int) (7.5 - Math.pow(((b.getFreeCellCount() * 2) / Math.pow(b.getDimension(), 2)), 2)) + 1 ;
			}
			

		}
		// Must be even depth !
		if (this.depth % 2 != 0){
			this.depth -= 1;
		}
		System.out.println("Depth is = " + this.depth);
		//this.depth = 10;
		Point best_move = minimax_decision(b, depth);
		m.P = this.playerSide;
		m.Col = best_move.x;
		m.Row = best_move.y;
		b.addPiece(m.Col, m.Row, playerSidetoBoardSide(true));
//		this.tTable.printStatistic();
		// check if there is a piece on that position.
		return m;
	}
	
	// Evaluate all possible moves with depth 1 and return the best one
	private Point minimax_decision(ZobristBoard state, int depth){
		int alpha = Integer.MIN_VALUE;
		int beta = Integer.MAX_VALUE;
		int bestScore = Integer.MIN_VALUE;
		Point bestMove = null;


		ArrayList<Point> moves = state.getMove();

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
			if (entry.depth >= this.depth - depth){
				if (maximizingPlayer){
					return entry.maxScore;
				} else {
					return entry.minScore;
				}
			}

		}
		if (maximizingPlayer) {
			Integer bestValue = Integer.MIN_VALUE;
			Point bestMove;
			for (Point move : moves) {
				ZobristBoard currentBoard = new ZobristBoard(state,this.z);
				currentBoard.addPiece(move.x, move.y, playerSidetoBoardSide(maximizingPlayer));
				int val = minimax_value(currentBoard, depth - 1, alpha, beta, false);
				if (val > bestValue){
					bestValue = val;
					bestMove = move;
					ZobristTableEntry newEntry = new ZobristTableEntry();
					newEntry.hashValue = currentBoard.board_hash;
					newEntry.maxScore = val;
					newEntry.bestMove = bestMove;
					newEntry.depth = this.depth - depth;
					tTable.storeEntry(newEntry);
				}

				alpha = Math.max(alpha, bestValue);
				if (beta <= alpha) {
					break;

				}
			}
			return bestValue;
		} else {
			Integer bestValue = Integer.MAX_VALUE;
			Point bestMove;
			for (Point move : moves) {
				ZobristBoard currentBoard = new ZobristBoard(state,this.z);
				currentBoard.addPiece(move.x, move.y, playerSidetoBoardSide(maximizingPlayer));

				int val = minimax_value(currentBoard, depth - 1, alpha, beta, true);
				if (val < bestValue){
					bestValue = val;
					bestMove = move;
					ZobristTableEntry newEntry = new ZobristTableEntry();
					newEntry.hashValue = currentBoard.board_hash;
					newEntry.minScore = val;
					newEntry.bestMove = bestMove;
					newEntry.depth = this.depth - depth;
					tTable.storeEntry(newEntry);
				}
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
				System.out.println("FOUND");
				
			}
		}
		
		if (bestIndex != 0) {
			System.out.println("FOUND");
		}
		//Swap the position
		Collections.swap(moves, bestIndex, 0);
	}
	
	//Simple Generic Evaluation Function while focusing on preventing getting captured
	private int evaluate(Board b, Board.CellContent side){
		int k1 = 0;
		int k2 = 0;
		int side_node = 0;

		if (b.getGrid()[0][0] == side || b.getGrid()[b.getDimension()-1][b.getDimension()-1] == side
				|| b.getGrid()[b.getDimension()-1][0] == side || b.getGrid()[0][b.getDimension()-1] == side){
			side_node -= 1;
		} 
		
		if (side == Board.CellContent.WHITE) {
			k1 = 1;
			k2 = -1 * 2;
		} else if (side == Board.CellContent.BLACK) {
			k1 = -1 * 2;
			k2 = 1;
		}
		return ((k1 * b.getWhiteScore()) + k1 + (k2 * b.getBlackScore()) + k2) + side_node;
	}
}
