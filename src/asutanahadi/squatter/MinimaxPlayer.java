package asutanahadi.squatter;

import java.awt.Point;
import java.security.KeyStore.Entry;
import java.util.ArrayList;
import java.util.Collections;

//Check First Random Player for more info
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
				this.depth = 10;
				
			} else if (b.getFreeCellCount() < 16){
				this.depth = 8;
			}	else if (b.getFreeCellCount() < 20){
				this.depth = 6;
			}
			else{
				this.depth = (int) (8.5 - Math.pow(((b.getFreeCellCount() * 2) / Math.pow(b.getDimension(), 2)), 2)) -1;
			}
			

		} else if (this.b.getDimension() == 6){
			if (b.getFreeCellCount() < 10){
				this.depth = 10;
			}			
			else if (b.getFreeCellCount() < 11){
				this.depth = 12;
			}
			else{
				this.depth = (int) (8.5 - Math.pow(((b.getFreeCellCount() * 2) / Math.pow(b.getDimension(), 2)), 2));
			}			
		}

		// Must be even depth ! (ai performs poorly on odd )
		if (this.depth % 2 != 0){
			this.depth -= 1;
		}
		System.out.println("Depth is = " + this.depth);
		Point best_move = minimax_decision(b, depth);
		m.P = this.playerSide;
		m.Col = best_move.x;
		m.Row = best_move.y;
		b.addPiece(m.Col, m.Row, playerSidetoBoardSide(true));

		return m;
	}
	
	// Evaluate all possible moves with depth depth and return the best one
	// A minimax with alpha beta pruning + transposition table to avoid opening the same node
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
		// State in entry, no need to recalculate just ask from the table !
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
					// Store the entry into the table 
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
					// Store the entry into the table 
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
	
	// Move order the array of moves so that the best one will be opened first
	// Unused feature as it fails to work
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
	
	/*Advanced Evaluation Function while focusing on preventing getting captured
	* layerScore + captureScore + clumpScore + side_node;
	* LayerScore = During Early game, the further from the center, the more chance
	* 	to capture someone later in the game. Also can't be captured easily if you are away from the center
	* CaptureScore = The point of the game is to capture, thus giving a very high importance !
	* ClumpScore = Best Feature we have where we want the player to not clump together in a position
	* 	It's strategic if there 2 neighbourhing node (more chance to capture) but
	* 	If it's more than 2 it shows clumping which has no value to the game.
	* Side_node = 4 useless edges has bad weight -9001
	*/

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
		
		
		int sideNode = 0;
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
		
		// 4 edges of board has no value in this game, give it a huge penalty score
		if (b.getGrid()[0][0] == side || b.getGrid()[b.getDimension()-1][b.getDimension()-1] == side
			|| b.getGrid()[b.getDimension()-1][0] == side || b.getGrid()[0][b.getDimension()-1] == side){
			sideNode -= 9001;
		} 
		
		

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
			
		// Prioritize capturing an enemy cell, because that's the point of the game
		int layerImportance = 0;
		int captureImportance = 100;
		int clumpImportance = 10;

		// determine whether it is early game or not if yes take the outer most layer node
		if (b.getFreeCellCount() > b.getDimension() * b.getDimension() * 4/5) {
			layerImportance = 5;
		}
		
		int layerScore = layerImportance * ( (whiteSign * whiteLayer) + (blackSign * blackLayer));
		int captureScore = captureImportance * (whiteSign * b.getWhiteScore() * whiteConstant + blackSign * b.getBlackScore() * blackConstant);
		

		int clumpScore =  clumpImportance * (whiteNeighbourScore * whiteSign + blackNeighbourScore * blackSign);
		
		//return  layerScore + (side_node) + captureScore + clumpScore ;
		return layerScore + captureScore + clumpScore + sideNode;
	
		
	}
	
}
