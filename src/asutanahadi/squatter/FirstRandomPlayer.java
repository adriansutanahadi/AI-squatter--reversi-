package asutanahadi.squatter;

import java.awt.Point;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Random;

import asutanahadi.squatter.Board.CellContent;

public class FirstRandomPlayer implements Player, Piece {
	protected Board b;
	private Boolean illegalMoveMade = false;
	protected int playerSide;
	Random rand;
	
	/* This function is called by the referee to initialise the player.
	 *  Return 0 for successful initialization and -1 for failed one.
	 */
	@Override
	public int init(int n, int p) {
		this.rand = new Random();
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
		int  n = rand.nextInt(moves.size());
		m.Col = moves.get(n).x;

		m.Row = moves.get(n).y;
		try{
		b.addPiece(m.Col, m.Row, playerSidetoBoardSide(true));
		}
		catch (Exception e){
			System.out.println(e);
		}
		// check if there is a piece on that position.
		return m;
	}
	// convert an enum from player class to reflect on board class
	protected CellContent playerSidetoBoardSide(boolean notOppositePlayer){
		if (notOppositePlayer) {
			if (this.playerSide == WHITE){
				return CellContent.WHITE;
			} else {
				return CellContent.BLACK;
			}
		} else {
			if (this.playerSide == WHITE){
				return CellContent.BLACK;
			} else {
				return CellContent.WHITE;
			}
		}
	}
	
	protected Move randomMove(){
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
				if (b.addPiece(m.Col, m.Row, CellContent.BLACK)){
					legal = 0;
				}
			} else if (m.P == WHITE && this.playerSide == BLACK) {
				if (b.addPiece(m.Col, m.Row, CellContent.WHITE)){
					legal = 0;
				}
			}
		} catch (Exception e){
			System.out.println(e);
		}
		if (legal == -1){
			illegalMoveMade = true;
		}
		return legal;
	}


	/* This function when called by referee should return the winner
	 *	Return -1, 0, 1, 2, 3 for INVALID, EMPTY, WHITE, BLACK, DEAD respectively
	 */
	@Override
	public int getWinner() {
		if (illegalMoveMade == true) {
			return -1;
		} else if (b.isFinished()) {
			if (b.getBlackScore() > b.getWhiteScore()){
				return 2;
			} else if (b.getWhiteScore() > b.getBlackScore()){
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
					break;
					case WHITE: s = "W" ;
					break;
					case CAPTURED_FREE: s = "-" ;
					break;
					case CAPTURED_WHITE: s = "w" ;
					break;
					case CAPTURED_BLACK: s = "b" ;
					break;
					case FREE: s = "+" ;
					break;
					default: s = "";
					break;
				}
				output.print(s);
				if (j != d-1) output.print(" ");
			}
			output.print("\n");
		}
		
	}

}
