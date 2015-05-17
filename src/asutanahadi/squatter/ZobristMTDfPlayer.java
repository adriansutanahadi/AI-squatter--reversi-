package asutanahadi.squatter;

import java.awt.Point;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Random;

// Idea for Zohrist from AI for games second edition
public class ZobristMTDfPlayer extends FirstDumbPlayer {
	private ZobristHash z;
	private long board_hash;
	@Override
	public int init(int n, int p) {
		
		if ((p == Piece.WHITE || p == Piece.BLACK) && n > 0) {
			playerSide = p;
			b = new Board(n);
			// Board can be white,black ,white captured,black captured or empty
			z = new ZobristHash(n);
			board_hash = z.getHash(b);
			//initZobristKey();
			return 0;
		} 
		else {
			return -1;
		}
	}
	

	
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
		int k1 = 0;
		int k2 = 0;
		if (side == WHITE) {
			k1 = 1;
			k2 = -1 * 2;
		} else if (side == BLACK) {
			k1 = -1 * 2;
			k2 = 1;
		}
		return k1 * b.getWhiteScore() + k1 + k2 * b.getBlackScore() + k2;
	}
}
