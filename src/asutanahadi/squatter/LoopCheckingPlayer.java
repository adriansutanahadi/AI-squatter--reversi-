package asutanahadi.squatter;

import java.awt.Point;
import java.util.ArrayList;

public class LoopCheckingPlayer extends FirstDumbPlayer {
	
	/* Function called by referee to request a move by the player.
	 *  Return object of class Move
	 */
	public Move makeMove() {
		Move m = new Move();

		ArrayList<Point> moves = b.getMove();
		Point best_move = greedy_player(moves);
		m.P = this.playerSide;
		m.Col = best_move.x;
		m.Row = best_move.y;
		b.addPiece(m.Col, m.Row, playerSidetoBoardSide());
		// check if there is a piece on that position.
		return m;
	}
	
	// Evaluate all possible moves with depth 1 and return the best one
	private Point greedy_player(ArrayList<Point> possible_move){
		int score = -1;
		Point bestMove = possible_move.get(0);
		int current_score;
		for (Point m: possible_move) {
			Board current_board = new Board(b.getDimension());
			Board.copy_grid(current_board,b);
			current_board.addPiece(m.x, m.y, playerSidetoBoardSide());
			current_score = evaluate(current_board,this.playerSide);
			if (current_score > score){
				score = current_score;
				bestMove = m;
			}
		}
		return bestMove;
	}
	
	//Simple Generic Evaluation Function while focusing on preventing getting captured
	private int evaluate(Board b,int side){
		int l1 = b.getDimension() * 2;
		int l2 = 0;
		int k1 = 0;
		int k2 = 0;
		
		//check distance from first piece (lower, better)
		for (int i = 0; i < b.getIncompleteLoops().size(); i++) {
			if (b.getIncompleteLoopsDistance().get(i) < l1) {
				l1 = b.getIncompleteLoopsDistance().get(i) / 2;
			}
		}
		//check length of incomplete loops (more, better)
		for (int i = 0; i < b.getIncompleteLoops().size(); i++) {
			if (b.getIncompleteLoops().get(i).size() > l2) {
				l2 = b.getIncompleteLoops().get(i).size() / 2;
			}
		}
		
		if (side == WHITE) {
			k1 = 1;
			k2 = -1 * 2;
		} else if (side == BLACK) {
			k1 = -1 * 2;
			k2 = 1;
		}
		return l1 + l2 + k1 * b.getWhiteScore() + k1 + k2 * b.getBlackScore() + k2;
	}
}
