package asutanahadi.squatter;

import java.awt.Point;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Random;

import asutanahadi.squatter.Board.CellContent;

public class FirstDumbPlayer implements Player, Piece {
	Board b;
	Boolean illegalMoveMade = false;
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

		ArrayList<Point> moves = b.getMove();


		m.P = this.playerSide;

		m.Col = moves.get(0).x;

		m.Row = moves.get(0).y;
		try{
		b.addPiece(m.Col, m.Row, playerSidetoBoardSide());
		}
		catch (Exception e){
			System.out.println(e);
		}

		// check if there is a piece on that position.

		return m;
	}
	// convert an enum from player class to reflect on board class
	private CellContent playerSidetoBoardSide(){
		if (this.playerSide == WHITE){
			return CellContent.WHITE;
		} else{
			return CellContent.BLACK;
		}
	}
	
	private Move randomMove(){
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
		try {
			if (m.P ==  BLACK && this.playerSide == WHITE) {
				legal = b.addPiece(m.Col, m.Row, CellContent.BLACK);
			} else if (m.P == WHITE && this.playerSide == BLACK) {
				legal = b.addPiece(m.Col, m.Row, CellContent.WHITE);
			}
		} catch (Exception e){
			System.out.println(e);
		}
		illegalMoveMade = true;
		return legal;
	}


	/* This function when called by referee should return the winner
	 *	Return -1, 0, 1, 2, 3 for INVALID, EMPTY, WHITE, BLACK, DEAD respectively
	 */
	@Override
	public int getWinner() {
		if (illegalMoveMade == True) {
			return -1;
		} else if (board.isFinished()) {
			if (board.getBlackScore() > board.getWhiteScore()){
				return 2;
			} else if (board.getWhiteScore() > board.getBlackScore()){
				return 1;
			} else {
				return 3;
			}
		} else {
			return 0;
		}
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
