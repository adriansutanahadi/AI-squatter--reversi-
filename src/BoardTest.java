import static org.junit.Assert.*;

import org.junit.Test;




public class BoardTest {
	private Board board;
	@Test
	//public void test() {
	//	fail("Not yet implemented");
	//}
	
	public void init(){
		int six_int = 6;
		board = new Board(six_int);
		assert(board.getGrid() != null);
		Integer six = 6;
		assertEquals(six, board.getDimension());
	}
	
	@Test
	public void add_input(){
		board = new Board(6);
		try {
		board.addContent("+ W W W W +", 0);
		board.addContent("W - W B B +", 1);
		// check here
		board.addContent("W W W B - B", 2);
		board.addContent("B B B - - B", 3);
		board.addContent("B - - W - B", 4);
		board.addContent("B B B W B W", 5);
		} catch (Exception e) {
			System.out.println(e);
		}
		assertEquals(Board.CellContent.FREE, board.getGrid()[0][0]);
		assertEquals(Board.CellContent.WHITE, board.getGrid()[5][5]);
		//System.out.println(board.getGrid());
		
	}
}
