package asutanahadi.squatter;

import java.awt.Point;
import java.util.ArrayList;

import asutanahadi.squatter.Board.CellContent;

/*
 * Board that implements zobrist hashing to store the state of the table as a hash.
 * Uses incremental zobrist hashing where full board hash need to be calculated once only
 * And the next just need to xor the new piece
 */
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
		board_hash = b.board_hash;
	}
	
	/*
	 * AddPiece to board plus hash the extra piece O(1)	  
	 */
	@Override
	public Boolean addPiece(int x, int y, CellContent player) {
		//0 = BLACk 1 = WHITE 2= Capture white 3= Captured Black 4= Captured Free
		int player_key = 0;
		if (player == CellContent.BLACK) {
			player_key = 0;
		} else if (player == CellContent.WHITE) {
			player_key = 1;
		}
		if (super.getGrid()[x][y] == CellContent.FREE){
			super.getGrid()[x][y] = player;
			setFreeCellCount(getFreeCellCount() - 1);
			updateBoard(x, y, player);
			//this.board_hash = z.getHash(this);
			//TODO
			// computationally expensive as it do not use incremental zobrist HASH
			// incomplete as it does not store captured D:
			
			board_hash = board_hash ^ z.getZobristKey()[x][y][player_key];
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
	/*
	 * Similar to superclass implementation but it hashes the captured cell O(1)	  
	 */
	protected void updateBoard(int x, int y, CellContent player) {
		ArrayList<Point> capturedTop = floodFill(x, y-1, player);
		ArrayList<Point> capturedLeft = floodFill(x+1, y, player);
		ArrayList<Point> capturedBottom = floodFill(x, y+1, player);
		ArrayList<Point> capturedRight = floodFill(x-1, y, player);
		capturedTop.addAll(capturedLeft);
		capturedTop.addAll(capturedBottom);
		capturedTop.addAll(capturedRight);
		for (Point p : capturedTop) {
			CellContent piece = super.getGrid()[p.x][p.y];
			
			// convert the piece to the captured ones if it is inside the polygon(loop)
			if (player == CellContent.WHITE) {
				switch (piece) {
				case FREE:
					super.getGrid()[p.x][p.y] = CellContent.CAPTURED_FREE;
					board_hash = board_hash ^ z.getZobristKey()[x][y][4];
					getCapturedCellsMap().add(new Point(p.x, p.y));
					setFreeCellCount(getFreeCellCount() - 1);
					break;
				case BLACK:
					super.getGrid()[p.x][p.y] = CellContent.CAPTURED_BLACK;
					board_hash = board_hash ^ z.getZobristKey()[x][y][0];
					board_hash = board_hash ^ z.getZobristKey()[x][y][3];
					getCapturedCellsMap().add(new Point(p.x, p.y));
					break;
				case CAPTURED_WHITE:
					super.getGrid()[p.x][p.y] = CellContent.WHITE;
					board_hash = board_hash ^ z.getZobristKey()[x][y][2];
					board_hash = board_hash ^ z.getZobristKey()[x][y][1];
					getCapturedCellsMap().remove(new Point(p.x, p.y));
					break;
				default:
				}
			} else if (player == CellContent.BLACK) {
				switch (piece) {
				case FREE:
					super.getGrid()[p.x][p.y] = CellContent.CAPTURED_FREE;
					board_hash = board_hash ^ z.getZobristKey()[x][y][4];
					getCapturedCellsMap().add(new Point(p.x, p.y));
					setFreeCellCount(getFreeCellCount() - 1);
					break;
				case WHITE:
					super.getGrid()[p.x][p.y] = CellContent.CAPTURED_WHITE;
					board_hash = board_hash ^ z.getZobristKey()[x][y][1];
					board_hash = board_hash ^ z.getZobristKey()[x][y][2];
					getCapturedCellsMap().add(new Point(p.x, p.y));
					break;
				case CAPTURED_BLACK:
					super.getGrid()[p.x][p.y] = CellContent.BLACK;
					board_hash = board_hash ^ z.getZobristKey()[x][y][3];
					board_hash = board_hash ^ z.getZobristKey()[x][y][0];
					getCapturedCellsMap().remove(new Point(p.x, p.y));
					break;
				default:
				}
			}
		}
	}
	

}
