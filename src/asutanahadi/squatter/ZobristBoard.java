package asutanahadi.squatter;

import java.awt.Point;
import java.util.ArrayList;

import asutanahadi.squatter.Board.CellContent;


public class ZobristBoard extends Board {
	protected long board_hash; 
	private ZobristHash z;
	public ZobristBoard(Integer dimension,ZobristHash z) {
		super(dimension);
		//
		this.z = z;
		board_hash = z.getHash(this);
	}
	
	
	public ZobristBoard(ZobristBoard b,ZobristHash z) {
		super(b);
		this.z = z;
		board_hash = z.getHash(this);
	}
	
	@Override
	public Boolean addPiece(int x, int y, CellContent player) {
		//make a dictionary 0 = BLACk 1 = WHITE 2= Capture white 3= Captured Black
		// Everytime grid is set, you need to add the hash to the zobirst hash 
//		int player_key = 0;
//		if (player == CellContent.BLACK) {
//			player_key = 0;
//		} else if (player == CellContent.WHITE) {
//			player_key = 1;
//		}
		if (grid[x][y] == CellContent.FREE){
			grid[x][y] = player;
			freeCellCount--;
			updateBoard(x, y, player);
			//this.board_hash = z.getHash(this);
			//TODO
			// computationally expensive as it do not use incremental zobrist HASH
			// incomplete as it does not store captured D:
			
			//board_hash = board_hash ^ z.getZobristKey()[x][y][player_key];
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
