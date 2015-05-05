package asutanahadi.squatter;

import java.io.PrintStream;
import java.util.Random;

public class FirstDumbPlayer implements Player, Piece {
	Board b;
	int playerSide;
	
	/* This funstion is called by the referee to initialise the player.
	 *  Return 0 for successful initialization and -1 for failed one.
	 */
	@Override
	public int init(int n, int p) {
		
		if ((p == Piece.WHITE || p == Piece.BLACK) && n > 0) {
			playerSide = p;
			b = new Board(n);
			return 0;
		} 
		else {
			return -1;
		}
	}

	
	@Override
	/* Function called by referee to request a move by the player.
	 *  Return object of class Move
	 */
	public Move makeMove() {
		Move m = new Move();
		Random rand = new Random();
		int dimension = b.getDimension();
		
		m.P = this.playerSide;
		m.Col = rand.nextInt(dimension);
		m.Row = rand.nextInt(dimension);
		
		// check if there is a piece on that position.
		return m;
	}

	@Override
	public int opponentMove(Move m) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getWinner() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void printBoard(PrintStream output) {
		// TODO Auto-generated method stub
		
	}

}
