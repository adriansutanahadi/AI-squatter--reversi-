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
		board_hash = b.board_hash;
	}
	
	@Override
	public Boolean addPiece(int x, int y, CellContent player) {
		//make a dictionary 0 = BLACk 1 = WHITE 2= Capture white 3= Captured Black 4= Captured Free
		// Everytime grid is set, you need to add the hash to the zobirst hash 
		int player_key = 0;
		if (player == CellContent.BLACK) {
			player_key = 0;
		} else if (player == CellContent.WHITE) {
			player_key = 1;
		}
		if (grid[x][y] == CellContent.FREE){
			grid[x][y] = player;
			freeCellCount--;
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
	//make a dictionary 0 = BLACk 1 = WHITE 2= Capture white 3= Captured Black 4= Captured Free
	protected void updateBoard(int x, int y, CellContent player) {
		ArrayList<Point> capturedTop = floodFill(x, y-1, player);
		ArrayList<Point> capturedLeft = floodFill(x+1, y, player);
		ArrayList<Point> capturedBottom = floodFill(x, y+1, player);
		ArrayList<Point> capturedRight = floodFill(x-1, y, player);
		capturedTop.addAll(capturedLeft);
		capturedTop.addAll(capturedBottom);
		capturedTop.addAll(capturedRight);
		for (Point p : capturedTop) {
			CellContent piece = grid[p.x][p.y];
			
			// convert the piece to the captured ones if it is inside the polygon(loop)
			if (player == CellContent.WHITE) {
				switch (piece) {
				case FREE:
					grid[p.x][p.y] = CellContent.CAPTURED_FREE;
					board_hash = board_hash ^ z.getZobristKey()[x][y][4];
					capturedCellsMap.add(new Point(p.x, p.y));
					freeCellCount--;
					break;
				case BLACK:
					grid[p.x][p.y] = CellContent.CAPTURED_BLACK;
					board_hash = board_hash ^ z.getZobristKey()[x][y][0];
					board_hash = board_hash ^ z.getZobristKey()[x][y][3];
					capturedCellsMap.add(new Point(p.x, p.y));
					break;
				case CAPTURED_WHITE:
					grid[p.x][p.y] = CellContent.WHITE;
					board_hash = board_hash ^ z.getZobristKey()[x][y][2];
					board_hash = board_hash ^ z.getZobristKey()[x][y][1];
					capturedCellsMap.remove(new Point(p.x, p.y));
					break;
				default:
				}
			} else if (player == CellContent.BLACK) {
				switch (piece) {
				case FREE:
					grid[p.x][p.y] = CellContent.CAPTURED_FREE;
					board_hash = board_hash ^ z.getZobristKey()[x][y][4];
					capturedCellsMap.add(new Point(p.x, p.y));
					freeCellCount--;
					break;
				case WHITE:
					grid[p.x][p.y] = CellContent.CAPTURED_WHITE;
					board_hash = board_hash ^ z.getZobristKey()[x][y][1];
					board_hash = board_hash ^ z.getZobristKey()[x][y][2];
					capturedCellsMap.add(new Point(p.x, p.y));
					break;
				case CAPTURED_BLACK:
					grid[p.x][p.y] = CellContent.BLACK;
					board_hash = board_hash ^ z.getZobristKey()[x][y][3];
					board_hash = board_hash ^ z.getZobristKey()[x][y][0];
					capturedCellsMap.remove(new Point(p.x, p.y));
					break;
				default:
				}
			}
		}
	}
	

}
