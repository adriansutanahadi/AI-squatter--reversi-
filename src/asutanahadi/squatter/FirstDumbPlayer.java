package asutanahadi.squatter;

import java.io.PrintStream;
import java.util.Random;

import asutanahadi.squatter.Board.CellContent;

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

	/* Function called by referee to inform the player about the opponent's move
	 *  Return -1 if the move is illegal otherwise return 0
	 */
	public int opponentMove(Move m) {
		// TODO Auto-generated method stub
		int legal = -1;
		
		// Adding the piece
		if (m.P ==  BLACK && this.playerSide == WHITE) {
			legal = b.addPiece(m.Row, m.Col, CellContent.BLACK);
		} else if (m.P == WHITE && this.playerSide == BLACK) {
			legal = b.addPiece(m.Row, m.Col, CellContent.WHITE);
		}
		return legal;
	}

	@Override
	public int getWinner() {
		
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void printBoard(PrintStream output) {
		int d = b.getDimension();
		String s;
		for (int i = 0; i < d; i++) {
			for (int j = 0; j < d; j++) {
				CellContent c = b.getGrid()[i][j];
				switch (c) {
					case BLACK: s = "B";
					case WHITE: s = "W" ;
					case CAPTURED_FREE: s = "-" ;
					case CAPTURED_WHITE: s = "w" ;
					case CAPTURED_BLACK: s = "b" ;
					case FREE: s = "+" ;
					default: s = "";
				}
				output.print(s);
				if (j != d-1) output.print(" ");
			}
			output.print("\n");
		}
		
	}

}
