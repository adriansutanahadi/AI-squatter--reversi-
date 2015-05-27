package asutanahadi.squatter;

import java.awt.Point;
import java.security.KeyStore.Entry;
import java.util.ArrayList;
import java.util.Collections;

public class MinimaxPlayer extends FirstRandomPlayer {
	
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
		this.tTable = new ZobristTranspositionTable();	
		
		this.depth = (int) (8.5 - Math.pow(((b.getFreeCellCount() * 2) / Math.pow(b.getDimension(), 2)), 2)) ;
		if (this.b.getDimension() >= 7){
			if (b.getFreeCellCount() < 10){
				this.depth = 10;
			}			
			else if (b.getFreeCellCount() < 11){
				this.depth = 11;
			}
			else if (b.getFreeCellCount() < 14){
				this.depth = 8;
				
			} else if (b.getFreeCellCount() < 16){
				this.depth = 6;
			}	else if (b.getFreeCellCount() < 20){
				this.depth = 6;
			}
			else{
				this.depth = (int) (8.5 - Math.pow(((b.getFreeCellCount() * 2) / Math.pow(b.getDimension(), 2)), 2));
			}
			

		}
//		this.depth = 4;
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
			



				
			alpha = Math.max( alpha, minimax_value(currentBoard, depth - 1, alpha, beta, false));
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
		
		// define the 8 directions
		Point nw = new Point(-1, -1);
		Point n = new Point(0, -1);
		Point ne = new Point(1, -1);
		Point e = new Point(1, 0);
		Point se = new Point(1, 1);
		Point s = new Point(0, 1);
		Point sw = new Point(-1, 1);
		Point w = new Point(-1, 0);
		Point[] directions = new Point[] {n, e, s, w, nw, ne, se, sw};
		
		
		int side_node = 0;
		int whiteConstant = 0;
		int blackConstant = 0;
		
		int whiteLayer = 0;
		int blackLayer = 0;
		int whiteNeighbourScore = 0;
		int blackNeighbourScore = 0;
		
		int whiteSign = 0;
		int blackSign = 0;
		

		//Determining Side
		if (side == Board.CellContent.WHITE) {
			whiteSign = 1;
			blackSign = -1;
			
			whiteConstant = 1;
			blackConstant = 4 ;
		} else if (side == Board.CellContent.BLACK) {
			whiteSign = -1;
			blackSign = 1;
			
			whiteConstant = 4;
			blackConstant = 1;
		}
		
		// Side node scoring
		if (b.getGrid()[0][0] == side || b.getGrid()[b.getDimension()-1][b.getDimension()-1] == side
			|| b.getGrid()[b.getDimension()-1][0] == side || b.getGrid()[0][b.getDimension()-1] == side){
			side_node -= 9001;
		} 
		
		
		
//		//If late game then use the plain scoring
//		if (b.getFreeCellCount() < b.getDimension() * b.getDimension() * 2 / 3) {			
//			return ( 30 * ((whiteConstant * b.getWhiteScore()) + (blackConstant * b.getBlackScore())) + side_node) * 100;
//		} 
//		else {
			//Without early game info, try picking the late outer most layer
			Point middlePoint = new Point ((b.getDimension()-1)/2,(b.getDimension()-1)/2);
			for (int i = 0; i < b.getDimension(); i++) {
				for (int j = 0; j < b.getDimension(); j++) {
					
					if (b.getGrid()[i][j] == Board.CellContent.WHITE) {
						
						// positioning of the piece (nearer to board side = the better)
						// only applies at early game (controlled by the variable importance below)
						whiteLayer += (Math.abs(middlePoint.x - i) > Math.abs(middlePoint.y - j)) ? Math.abs(middlePoint.y - j) : Math.abs(middlePoint.x - i);

						// check for neighboring points and gives penalty if it tries to clump
						int sameNeighbour = 0;
						for (Point dir: directions){
							
							Point currentPoint = new Point (i+dir.x,j+dir.y);
							if (b.checkCellValidity(currentPoint)){
								if (b.getGrid()[currentPoint.x][currentPoint.y] == Board.CellContent.WHITE){
									sameNeighbour++;
								}
							}
							
							if (sameNeighbour > 2){
								whiteNeighbourScore -= 1;
							} else {
								whiteNeighbourScore += 1;
							}
						}
					}
					
					if (b.getGrid()[i][j] == Board.CellContent.BLACK) {
						
						// positioning of the piece (nearer to board side = the better)
						// only applies at early game (controlled by the variable importance below)
						blackLayer += (Math.abs(middlePoint.x - i) > Math.abs(middlePoint.y - j)) ? Math.abs(middlePoint.y - j) : Math.abs(middlePoint.x - i);

						// check for neighboring points and gives penalty if it tries to clump
						int sameNeighbour = 0;
						for (Point dir: directions){
							
							Point currentPoint = new Point (i+dir.x,j+dir.y);
							if (b.checkCellValidity(currentPoint)){
								if (b.getGrid()[currentPoint.x][currentPoint.y] == Board.CellContent.BLACK){
									sameNeighbour++;
								}
							}
							
							if (sameNeighbour > 2){
								blackNeighbourScore -= 1;
							} else{
								blackNeighbourScore += 1;
							}
						}					
					}
				}
			}
			
		int layerImportance = 0;
		int captureImportance = 100;
		int clumpImportance = 10;

		// determine whether it is early game or not
		if (b.getFreeCellCount() > b.getDimension() * b.getDimension() * 4/5) {
			layerImportance = 5;
		}
		
		int layerScore = layerImportance * ( (whiteSign * whiteLayer) + (blackSign * blackLayer));
		int captureScore = captureImportance * (whiteSign * b.getWhiteScore() * whiteConstant + blackSign * b.getBlackScore() * blackConstant);
		int clumpScore =  clumpImportance * (whiteNeighbourScore * whiteSign + blackNeighbourScore * blackSign);
		
		//return  layerScore + (side_node) + captureScore + clumpScore ;
		return layerScore + captureScore + clumpScore + side_node;
	
		
	}
	
}
