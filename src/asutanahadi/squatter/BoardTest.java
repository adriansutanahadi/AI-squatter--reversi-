package asutanahadi.squatter;
import static org.junit.Assert.*;

import org.junit.Test;

import asutanahadi.squatter.Board.CellContent;




public class BoardTest {
	private Board board;
	@Test
	//public void test() {
	//	fail("Not yet implemented");
	//}
	
	public void init(){
		board = new Board(7);
		assertEquals((int)7, (int)board.getDimension());
		board.addPiece(0, 0, CellContent.WHITE);
		assertEquals(CellContent.WHITE, board.getGrid()[0][0]);
		assertEquals(CellContent.FREE, board.getGrid()[6][6]);
		assertEquals(false, board.isFinished());
		try {
			board.updateScore();
			System.out.println("yolo");
		} catch (Exception e){
			System.out.println(e);
		}
	}
	
	@Test
	public void add_input(){

		
	}
}
