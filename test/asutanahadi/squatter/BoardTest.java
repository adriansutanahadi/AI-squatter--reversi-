package asutanahadi.squatter;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.ArrayList;

import org.junit.Test;

import asutanahadi.squatter.Board.*;

public class BoardTest {
	@Test

	
	// Simple init test
	public void init(){
		Board board = new Board(7);
		assertEquals((int)7, (int)board.getDimension());
		try {

			board.addPiece(0, 0, CellContent.WHITE);

		} catch (Exception e) {
			// TODO: handle exception
		}
		assertEquals(CellContent.WHITE, board.getGrid()[0][0]);
		assertEquals(CellContent.FREE, board.getGrid()[6][6]);
		assertEquals(false, board.isFinished());
	}
	
	// Functional testing.
	
	//one single loop 
	//  w w w
	//  w b w
	//  w w w
	@Test
	public void add_loop_square(){
		Board board = new Board(7);
		try {			
			board.addPiece(1, 1, CellContent.BLACK);
			board.addPiece(0, 0, CellContent.WHITE);
			board.addPiece(0, 1, CellContent.WHITE);
			board.addPiece(0, 2, CellContent.WHITE);
			board.addPiece(1, 2, CellContent.WHITE);
			board.addPiece(2, 2, CellContent.WHITE);
			board.addPiece(2, 1, CellContent.WHITE);
			board.addPiece(2, 0, CellContent.WHITE);
			board.addPiece(1, 0, CellContent.WHITE);

			board.updateScore();

		} catch (Exception e) {
			// TODO: handle exception
		}
		
		assertEquals((int) 1,(int) board.getWhiteScore() );
		assertEquals((int) 0,(int) board.getBlackScore() );
	
	}
	
	//one single loop 
	//  - w -
	//  w b w
	//  - w -
	@Test
	public void add_loop_diamond(){
		Board board = new Board(7);
		try {
			board.addPiece(1, 1, CellContent.BLACK);
			board.addPiece(1, 0, CellContent.WHITE);
			board.addPiece(0, 1, CellContent.WHITE);
			board.addPiece(1, 2, CellContent.WHITE);
			board.addPiece(2, 1, CellContent.WHITE);

			board.updateScore();

		} catch (Exception e) {
			// TODO: handle exception
		}
		assertEquals((int) 1,(int) board.getWhiteScore() );
		assertEquals((int) 0,(int) board.getBlackScore() );
	}
	
	// test for doubly loop
	//  - w - w -
	//  w b w b w
	//  - w w w w
	@Test
	public void add_loop_doubly(){
		Board board = new Board(7);
		try {
			board.addPiece(1, 1, CellContent.BLACK);
			board.addPiece(1, 0, CellContent.WHITE);
			board.addPiece(0, 1, CellContent.WHITE);
			board.addPiece(1, 2, CellContent.WHITE);
			board.addPiece(2, 1, CellContent.WHITE);
			

			board.addPiece(3, 1, CellContent.BLACK);
			board.addPiece(2, 2, CellContent.WHITE);
			board.addPiece(3, 0, CellContent.WHITE);
			board.addPiece(3, 2, CellContent.WHITE);
			board.addPiece(4, 1, CellContent.WHITE);
			board.addPiece(4, 2, CellContent.WHITE);		
			
			
			
		

			board.updateScore();

		} catch (Exception e) {
			// TODO: handle exception
		}
		assertEquals((int) 2,(int) board.getWhiteScore() );
		assertEquals((int) 0,(int) board.getBlackScore() );
	}

	//one single loop 
	//  - w - - - -
	//  w b w - - -
	//  - w - - - -
	//  - w w w w -
	//  - w - - w -
	//  - w - - w - 
	//  - w w w w -
	@Test
	public void add_big_loop(){
		Board board = new Board(7);


			
		// left side
		try {
			board.addPiece(1, 1, CellContent.BLACK);
			board.addPiece(1, 0, CellContent.WHITE);
			board.addPiece(0, 1, CellContent.WHITE);
			board.addPiece(1, 2, CellContent.WHITE);
			board.addPiece(2, 1, CellContent.WHITE);

			
			
			board.addPiece(1, 3, CellContent.WHITE);
			board.addPiece(1, 4, CellContent.WHITE);
			board.addPiece(1, 5, CellContent.WHITE);
			board.addPiece(1, 6, CellContent.WHITE);
	
			
			// bottom side
			board.addPiece(2, 6, CellContent.WHITE);
			board.addPiece(3, 6, CellContent.WHITE);
			board.addPiece(4, 6, CellContent.WHITE);
			 
			// right side
			board.addPiece(4, 5, CellContent.WHITE);
			board.addPiece(4, 4, CellContent.WHITE);
			board.addPiece(4, 3, CellContent.WHITE);
			
			// top side
			board.addPiece(3, 3, CellContent.WHITE);
			board.addPiece(2, 3, CellContent.WHITE);
			

			board.updateScore();

		} catch (Exception e) {
			// TODO: handle exception
		}
		assertEquals((int) 5,(int) board.getWhiteScore() );
		assertEquals((int) 0,(int) board.getBlackScore() );
	}
	//	 0 1 2 3 4 5
	//one single loop 
	//0  - - - - - - -
	//1  b b b b b - -
	//2  b - w - b - -
	//3  b w b w b - -
	//4  b - w - b - -
	//5  b b b b b - -
	//6  - - - - - - -
	@Test
	public void loop_inside_loop(){
		Board board = new Board(7);
		try {
			board.addPiece(2, 3, CellContent.BLACK);
			board.addPiece(2, 2, CellContent.WHITE);
			board.addPiece(1, 3, CellContent.WHITE);
			board.addPiece(2, 4, CellContent.WHITE);
			board.addPiece(3, 3, CellContent.WHITE);
			// Score is white capture 1
			assertEquals((int) 1,(int) board.getWhiteScore() );
			assertEquals((int) 0,(int) board.getBlackScore() );
			
			//Black capturing the initial white
			board.addPiece(0, 1, CellContent.BLACK);
			board.addPiece(0, 2, CellContent.BLACK);
			board.addPiece(0, 3, CellContent.BLACK);
			board.addPiece(0, 4, CellContent.BLACK);
			board.addPiece(0, 5, CellContent.BLACK);
			
			board.addPiece(1, 5, CellContent.BLACK);
			board.addPiece(2, 5, CellContent.BLACK);
			board.addPiece(3, 5, CellContent.BLACK);
			board.addPiece(4, 5, CellContent.BLACK);
			
			board.addPiece(4, 4, CellContent.BLACK);	
			board.addPiece(4, 3, CellContent.BLACK);	
			board.addPiece(4, 2, CellContent.BLACK);	
			board.addPiece(4, 1, CellContent.BLACK);
			
			
			board.addPiece(3, 1, CellContent.BLACK);	
			board.addPiece(2, 1, CellContent.BLACK);
			board.addPiece(1, 1, CellContent.BLACK);

			
		} catch (Exception e){
			
		}

		assertEquals((int) 0,(int) board.getWhiteScore() );
		assertEquals((int) 8,(int) board.getBlackScore() );
	}
	
	// testing if making possible moves work
	// TODO more extreme cases please
	@Test
	public void test_possible_moves(){
		Board board = new Board(7);
		ArrayList<Point> moves = board.getMove();
		assertEquals((int) 7*7,(int) moves.size() );
		try {
			board.addPiece(2, 3, CellContent.BLACK);
		} catch (Exception e){
			
		}
		moves = board.getMove();
		assertEquals((int) 7*7 - 1,(int) moves.size() );
	}
	 
	
}