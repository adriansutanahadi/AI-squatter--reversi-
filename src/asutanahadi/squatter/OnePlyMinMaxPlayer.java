package asutanahadi.squatter;

import java.awt.Point;
import java.util.ArrayList;

public class OnePlyMinMaxPlayer extends FirstDumbPlayer {
	
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
}
