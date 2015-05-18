package asutanahadi.squatter;

import asutanahadi.squatter.Board.CellContent;


public class ZobristBoard extends Board {
	protected long board_hash;
	private ZobristHash z;
	public ZobristBoard(Integer dimension) {
		super(dimension);
		z = new ZobristHash(dimension);
		board_hash = z.getHash(this);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public Boolean addPiece(int x, int y, CellContent player) {
		//TODO
		//Rehash when you add an item
		if (grid[x][y] == CellContent.FREE){
			grid[x][y] = player;
			freeCellCount--;
			updateBoard(x, y, player);
			try {
				updateScore();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		}else {
			return false;
		}
		
		
	}
}
