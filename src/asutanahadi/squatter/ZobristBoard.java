package asutanahadi.squatter;

import asutanahadi.squatter.Board.CellContent;


public class ZobristBoard extends Board {
	protected long board_hash;
	private ZobristHash z;
	public ZobristBoard(Integer dimension) {
		super(dimension);
		z = new ZobristHash(dimension);
		board_hash = z.getHash(this);
	}
	
	@Override
	public Boolean addPiece(int x, int y, CellContent player) {

		// Everytime grid is set, you need to add the hash to the zobirst hash
		if (grid[x][y] == CellContent.FREE){
			grid[x][y] = player;
			freeCellCount--;
			updateBoard(x, y, player);
			//TODO
			// computationally expensive as it do not use incremental zobrist HASH
			board_hash = z.getHash(this);
			try {
				updateScore();
			} catch (Exception e) {

				e.printStackTrace();
			}
			return true;
		}else {
			return false;
		}
		
	}
	

}
