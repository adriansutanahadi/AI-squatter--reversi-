// Board Class for the game of squatter coded by Denis Thamrin and Adrian Sutanahadi
package asutanahadi.squatter;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Stack;

public class Board {
	
	// Board Declaration
	// Changed each captured to individual CAPTURED BLACK / WHITE
	public enum CellContent {
		BLACK,
		WHITE,
		CAPTURED_FREE,
		CAPTURED_WHITE,
		CAPTURED_BLACK,
		FREE
	}

	private Integer dimension;
	private Integer blackScore;
	private Integer whiteScore;
	private Integer freeCellCount;
	private CellContent[][] grid = null;
	private ArrayList<Point> capturedCellsMap = new ArrayList<Point>();
	private ArrayList<CellContent> capturedCellsOwner;
	//private boolean finished = false;
	
	// Getters Setters
	public Integer getDimension() {
		return dimension;
	}

	public CellContent[][] getGrid() {
		return grid;
	}

	public Integer getBlackScore() {
		return blackScore;
	}

	public Integer getWhiteScore() {
		return whiteScore;
	}

	public boolean isFinished() {
		return (freeCellCount == 0);
	}

	public ArrayList<Point> getCapturedCellsMap() {
		return capturedCellsMap;
	}
	
	// Initialize the board
	public Board(Integer dimension){
		this.dimension = dimension;
		grid = new CellContent[dimension][dimension];
		for (int i = 0; i < this.dimension; i++) {
			for (int j = 0; j < this.dimension; j++) {
				grid[i][j] = CellContent.FREE;
				
			}
		}
		this.freeCellCount = dimension*dimension;
	}
	
	// Add a single piece to the specified location, return True if succeed 
	public Boolean addPiece(int x, int y, CellContent player) {
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
	
	private void updateBoard(int x, int y, CellContent player) {
		ArrayList<ArrayList<Point>> loops = findLoop(x, y, player);
		
		// for each loop, check if there are new captured pieces
		for (ArrayList<Point> loop : loops) {
			
			// convert the loop into a Polygon object
			Polygon loopPolygon = new Polygon();
			for (Point p : loop) {
				loopPolygon.addPoint(p.x, p.y);
			}
			
			// limit the area of the search
			Rectangle checkArea = loopPolygon.getBounds();
			Point topLeft = new Point(checkArea.x, checkArea.y);
			Point bottomLeft = new Point(checkArea.x + checkArea.width, checkArea.y + checkArea.height);
			
			// search the area for new captured pieces
			for (int i = topLeft.x; i <= bottomLeft.x; i++) {
				for (int j = topLeft.y; j <= bottomLeft.y; j++) {
					Point piecePosition = new Point(i, j);
					CellContent piece = grid[i][j];
					
					// convert the piece to the captured ones if it is inside the polygon(loop)
					if (player == CellContent.WHITE) {
						if (loopPolygon.contains(piecePosition)) {
							switch (piece) {
							case FREE:
								grid[i][j] = CellContent.CAPTURED_FREE;
								capturedCellsMap.add(new Point(i, j));
								break;
							case BLACK:
								grid[i][j] = CellContent.CAPTURED_BLACK;
								capturedCellsMap.add(new Point(i, j));
								break;
							case CAPTURED_WHITE:
								grid[i][j] = CellContent.WHITE;
								capturedCellsMap.remove(new Point(i, j));
								break;
							default:
							}
						}
					} else if (player == CellContent.BLACK) {
						if (loopPolygon.contains(piecePosition)) {
							switch (piece) {
							case FREE:
								grid[i][j] = CellContent.CAPTURED_FREE;
								capturedCellsMap.add(new Point(i, j));
								break;
							case WHITE:
								grid[i][j] = CellContent.CAPTURED_WHITE;
								capturedCellsMap.add(new Point(i, j));
								break;
							case CAPTURED_BLACK:
								grid[i][j] = CellContent.BLACK;
								capturedCellsMap.remove(new Point(i, j));
								break;
							default:
							}
						}
					}
				}
			}
		}
	}
	
	private ArrayList<ArrayList<Point>> findLoop(int x, int y, CellContent player) {
		
		// define the 8 directions
		Point nw = new Point(-1, -1);
		Point n = new Point(0, -1);
		Point ne = new Point(1, -1);
		Point e = new Point(1, 0);
		Point se = new Point(1, 1);
		Point s = new Point(0, 1);
		Point sw = new Point(-1, 1);
		Point w = new Point(-1, 0);
		Point[] directions = new Point[] {n, e, s, w, nw, ne, se, sw};
		
		Point firstPiece = new Point(x,y);
		
		ArrayList<ArrayList<Point>> loops = new ArrayList<ArrayList<Point>>(); // to store loops
		ArrayList<Point> discovered = new ArrayList<Point>(); // to store discovered item in the graph
		
		// to store the state when there is an intersection
		ArrayList<Point> positionList = new ArrayList<Point>();
		ArrayList<Point> prevPositionList = new ArrayList<Point>();
		ArrayList<ArrayList<Point>> pathList = new ArrayList<ArrayList<Point>>();
		
		// initialise first move (which is the first piece)
		positionList.add(new Point(firstPiece));
		prevPositionList.add(new Point(firstPiece));
		pathList.add(new ArrayList<Point>());
		
		for (int i = 0; i < positionList.size(); i++) {
			Stack<Point> queue = new Stack<Point>();
			queue.push(positionList.get(i));
			Point prevPosition = prevPositionList.get(i);
			
			// use depth-first search to check for a loop
			while (!queue.empty()) {
				Point currentPosition = queue.pop();
				if (!discovered.contains(currentPosition)) {
					// visit the piece
					discovered.add(currentPosition);
					pathList.get(i).add(new Point(currentPosition));
					
					// check for available pieces to go to in 8 directions
					ArrayList<Point> nextPositionAvailable = new ArrayList<Point>();
					for (Point dir : directions) {
						Point nextPosition = new Point(currentPosition.x + dir.x, currentPosition.y + dir.y);
						if (!checkCellValidity(nextPosition)) {
							continue;
						}
						if (!prevPosition.equals(nextPosition) && grid[nextPosition.x][nextPosition.y] == player) {
							nextPositionAvailable.add(new Point(nextPosition));
						}
					}
					
					for (int j = 0; j < nextPositionAvailable.size(); j++) {
						if (nextPositionAvailable.get(j).equals(firstPiece)) {
							// store the path if it creates a loop
							loops.add(new ArrayList<Point>(pathList.get(i)));
						} else if (!pathList.get(i).contains(nextPositionAvailable.get(j))) {
							if (j == 0) {
								// mark next piece to be traversed
								queue.push(nextPositionAvailable.get(0));
								prevPosition = currentPosition;
							} else {
								// store the other pieces, so that it can be continued when this one has finished traversing
								positionList.add(new Point(nextPositionAvailable.get(j)));
								prevPositionList.add(new Point(currentPosition));
								pathList.add(new ArrayList<Point>(pathList.get(i)));
							}
						} 
					}
				}
			}
		}
		return loops;
	}
	/*
	 * Generate all possible moves in current board
	 * Not tested yet
	 * TODO : pruning to be implemented
	 */
	public ArrayList<Point> getMove(){
		ArrayList<Point> moves = new ArrayList<Point>();
		for (int i = 0; i < this.dimension; i++) {
			for (int j = 0; j < this.dimension; j++) {
				if (grid[i][j] == CellContent.FREE){
					moves.add(new Point(i,j));
				}
				
			}
		}		
		return moves;
	}
	
	
	// insert cell content at the specific row
	// row starts from 0
//	public CellContent[][] addContent(String input, Integer y) throws Exception{
//		String[] input_array = input.split(" ");
//		Integer lengthOfInput = input_array.length;
//		
//		// check whether the input supplied enough rows
//		if (lengthOfInput != this.dimension){
//			throw new Exception(String.format("Expected %d number of columns at row %d.",this.dimension,y));
//		}
//		
//		// Populate the two-dimensional array
//		for (int x = 0; x < dimension; x++){
//			
//				// Add to grid
//				CellContent cellcontent = stringToCellContent(input_array[x]);
//				grid[x][y] = cellcontent;
//				
//				// Add position of captured cells to map
//				if (cellcontent == CellContent.CAPTURED) {
//					capturedCellsMap.add(new Point(x,y));
//				}
//				
//				// Check if there is a free cell
//				if (cellcontent == CellContent.FREE) {
//					finished = false;
//				}
//		}
//		return this.grid;
//	}
	
	// Convert input string to CellContent Enum
////	private CellContent stringToCellContent(String s) throws Exception{
//		switch (s) {
//			case "B": return CellContent.BLACK;
//			case "W": return CellContent.WHITE;
//			case "-": return CellContent.CAPTURED;
//			case "+": return CellContent.FREE;
//			default: throw new Exception(String.format("String <%s> does not correspond to Cell Enum please check the input string !\n",s));
//		}
//	}
	
	// Check whether the index is out of bound
	private boolean checkCellValidity(Point p){
		if ((p.x < this.dimension) && (p.x >= 0)
				&& (p.y < this.dimension) && (p.y >= 0)) {
			return true;
		}
		return false;
	}
	
	// Check the ownership of the captured cell
	private CellContent checkCapturedCell(Point p) throws Exception{
		Point check;
		ArrayList<CellContent> surroundingCell = new ArrayList<CellContent>();
		
		// check upwards
		check = new Point(p);
		check.y -= 1;
		while (checkCellValidity(check)){
			if (isCaptured(grid[check.x][check.y])){
				return capturedCellsOwner.get(capturedCellsMap.indexOf(check));
			} else {
				surroundingCell.add(grid[check.x][check.y]);
				break;
			}
		}
		
		// check leftwards
		check = new Point(p);
		check.x -= 1;
		while (checkCellValidity(check)){
			if (isCaptured(grid[check.x][check.y])){
				return capturedCellsOwner.get(capturedCellsMap.indexOf(check));
			} else {
				surroundingCell.add(grid[check.x][check.y]);
				break;
			}
		}
		
		// check downwards
		check = new Point(p);
		check.y += 1;
		while (checkCellValidity(check)){
			if (isCaptured(grid[check.x][check.y])){
				check.y += 1;
				continue;
			} else {
				surroundingCell.add(grid[check.x][check.y]);
				break;
			}
		}
		
		// check rightwards
		check = new Point(p);
		check.x += 1;
		while (checkCellValidity(check)){
			if (isCaptured(grid[check.x][check.y])){
				check.x += 1;
				continue;
			} else {
				surroundingCell.add(grid[check.x][check.y]);
				break;
			}
		}
		
		
		// verify 4 items in surrounding cell are of the same item in the array
		Integer black = 0;
		Integer white = 0;
		for(CellContent c : surroundingCell) {
			if (c == CellContent.BLACK) {
				black++;
			} else if (c == CellContent.WHITE) {
				white++;
			}
		}
		
		// return the result
		if (black == 4) {
			return CellContent.BLACK;
		} else if (white == 4){
			return CellContent.WHITE;
		} 
			// Must be owned by someone, board has errors
			else {
			throw new Exception("Board Has error(s)");
		}
	}
	
	private Boolean isCaptured(CellContent c){
		if (c == CellContent.CAPTURED_BLACK || c == CellContent.CAPTURED_FREE || c == CellContent.CAPTURED_WHITE){
			return true;
		}
		else {
			return false;
		}
	}
	// update the scores
	public void updateScore() throws Exception{
		Integer blackScore = 0;
		Integer whiteScore = 0;
		CellContent currResult = null;
		capturedCellsOwner = new ArrayList<CellContent>();
		
		// check the ownership of each captured cells
		for(Point p : capturedCellsMap) {
			currResult = checkCapturedCell(p);
			capturedCellsOwner.add(currResult);
			// Calculate the score
			if (currResult == CellContent.BLACK) {
				blackScore++;
			} else {
				whiteScore++;
			}
		}
		
		this.blackScore = blackScore;
		this.whiteScore = whiteScore;
	}
	
}




