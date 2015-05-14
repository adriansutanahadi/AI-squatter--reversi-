package asutanahadi.squatter;
import static org.junit.Assert.*;

import org.junit.Test;

import asutanahadi.squatter.Board.CellContent;




public class BoardTest {
	private Board board;
	@Test

	
	// Simple init test
	public void init(){
		board = new Board(7);
		try {
			assertEquals((int)7, (int)board.getDimension());
			board.addPiece(0, 0, CellContent.WHITE);
			assertEquals(CellContent.WHITE, board.getGrid()[0][0]);
			assertEquals(CellContent.FREE, board.getGrid()[6][6]);
			assertEquals(false, board.isFinished());
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	// Functional testing.
	
	//one single loop 
	//  w w w
	//  w b w
	//  w w w
	@Test
	public void add_loop_square(){
		board = new Board(7);
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
			assertEquals((Integer) 1,(Integer) board.getWhiteScore() );
			assertEquals((Integer) 0,(Integer) board.getBlackScore() );
		} catch (Exception e) {
			// TODO: handle exception
		}
	
	}
	
	//one single loop 
	//  - w -
	//  w b w
	//  - w -
	@Test
	public void add_loop_diamond(){
		board = new Board(7);
		try {
			board.addPiece(1, 1, CellContent.BLACK);
			board.addPiece(1, 0, CellContent.WHITE);
			board.addPiece(0, 1, CellContent.WHITE);
			board.addPiece(1, 2, CellContent.WHITE);
			board.addPiece(2, 1, CellContent.WHITE);

			board.updateScore();
			assertEquals((Integer) 1,(Integer) board.getWhiteScore() );
			assertEquals((Integer) 0,(Integer) board.getBlackScore() );
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	// test for doubly loop
	//  - w - w -
	//  w b w b w
	//  - w w w w
	@Test
	public void add_loop_doubly(){
		board = new Board(7);
		try {
			board.addPiece(2, 0, CellContent.WHITE);
			board.addPiece(0, 1, CellContent.WHITE);
			board.addPiece(1, 2, CellContent.WHITE);
			board.addPiece(2, 1, CellContent.WHITE);
			
			board.addPiece(1, 1, CellContent.BLACK);
			
			board.addPiece(2, 2, CellContent.WHITE);
			board.addPiece(3, 0, CellContent.WHITE);
			board.addPiece(3, 2, CellContent.WHITE);
			board.addPiece(4, 1, CellContent.WHITE);
			board.addPiece(4, 2, CellContent.WHITE);		
			
			board.addPiece(3, 1, CellContent.BLACK);
			
			
		

			board.updateScore();
			assertEquals((Integer) 2,(Integer) board.getWhiteScore() );
			assertEquals((Integer) 0,(Integer) board.getBlackScore() );
		} catch (Exception e) {
			// TODO: handle exception
		}
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
		add_loop_diamond();
		// left side
		try {
			board.addPiece(1, 3, CellContent.WHITE);
			board.addPiece(1, 4, CellContent.WHITE);
			board.addPiece(1, 5, CellContent.WHITE);
	
			
			// bottom side
			board.addPiece(2, 5, CellContent.WHITE);
			board.addPiece(3, 5, CellContent.WHITE);
			board.addPiece(4, 5, CellContent.WHITE);
			 
			// right side
			board.addPiece(4, 4, CellContent.WHITE);
			board.addPiece(4, 3, CellContent.WHITE);
			board.addPiece(4, 2, CellContent.WHITE);
			
			// top side
			board.addPiece(3, 2, CellContent.WHITE);
			board.addPiece(2, 2, CellContent.WHITE);
			

			board.updateScore();
			assertEquals((Integer) 5,(Integer) board.getWhiteScore() );
			assertEquals((Integer) 0,(Integer) board.getBlackScore() );
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	
	 
	
}