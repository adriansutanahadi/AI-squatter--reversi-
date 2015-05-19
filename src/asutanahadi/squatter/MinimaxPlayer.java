package asutanahadi.squatter;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;

public class MinimaxPlayer extends FirstDumbPlayer {
	
	/* Function called by referee to request a move by the player.
	 *  Return object of class Move
	 */
	public Move makeMove() {
		Move m = new Move();
		int depth = 5;
		Point best_move = minimax_decision(b, depth);
		m.P = this.playerSide;
		m.Col = best_move.x;
		m.Row = best_move.y;
		b.addPiece(m.Col, m.Row, playerSidetoBoardSide());
		// check if there is a piece on that position.
		return m;
	}
	
	// Evaluate all possible moves with depth 1 and return the best one
	private Point minimax_decision(Board state, int depth){
		ArrayList<Point> moves = state.getMove();
		ArrayList<Integer> move_value = new ArrayList<Integer>();
		for (Point move : moves) {
			Board currentBoard = new Board(state.getDimension());
			Board.copy_grid(currentBoard, state);
			currentBoard.addPiece(move.x, move.y, playerSidetoBoardSide());
			move_value.add(minimax_value(currentBoard, depth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, true));
		}
		int maxScore = Collections.max(move_value);
		return moves.get(move_value.indexOf(maxScore));
	}
	
	private int minimax_value(Board state, int depth, int alpha, int beta, boolean maximizingPlayer) {
		if (depth == 0 || state.isFinished()) {
			return evaluate(state, this.playerSide);
		}
		ArrayList<Point> moves = state.getMove();
		if (maximizingPlayer) {
			Integer bestValue = Integer.MIN_VALUE;
			for (Point move : moves) {
				Board currentBoard = new Board(state.getDimension());
				Board.copy_grid(currentBoard, state);
				currentBoard.addPiece(move.x, move.y, playerSidetoBoardSide());
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
			Collections.reverse(moves);
			for (Point move : moves) {
				Board currentBoard = new Board(state.getDimension());
				Board.copy_grid(currentBoard, state);
				currentBoard.addPiece(move.x, move.y, oppositeSidetoBoardSide());
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
	
	//Simple Generic Evaluation Function while focusing on preventing getting captured
	private int evaluate(Board b, int side){
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
