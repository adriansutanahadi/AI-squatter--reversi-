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
	public int addPiece(int row, int col, CellContent player) {
		if (grid[row][col] == CellContent.FREE){
			grid[row][col] = player;
			freeCellCount--;
			updateBoard(row, col, player);
			return 0;
		}else {
			return -1;
		}
		
	}
	
	private void updateBoard(int row, int col, CellContent player) {
		ArrayList<ArrayList<Point>> loops = findLoop(row, col, player);
		for (ArrayList<Point> loop : loops) {
			Polygon loopPolygon = new Polygon();
			for (Point p : loop) {
				loopPolygon.addPoint(p.x, p.y);
			}
			Rectangle checkArea = loopPolygon.getBounds();
			Point topLeft = new Point(checkArea.x, checkArea.y);
			Point bottomLeft = new Point(checkArea.x + checkArea.width, checkArea.y + checkArea.height);
			for (int i = topLeft.x; i <= bottomLeft.x; i++) {
				for (int j = topLeft.y; j <= bottomLeft.y; j++) {
					CellContent piece = grid[i][j];
					if (player == CellContent.WHITE) {
						switch (piece) {
						case FREE: grid[i][j] = CellContent.CAPTURED_FREE;
						case BLACK: grid[i][j] = CellContent.CAPTURED_BLACK;
						case CAPTURED_WHITE: grid[i][j] = CellContent.WHITE;
						default:  grid[i][j] = CellContent.CAPTURED_FREE;
						}
					} else if (player == CellContent.BLACK) {
						switch (piece) {
						case FREE: grid[i][j] = CellContent.CAPTURED_FREE;
						case WHITE: grid[i][j] = CellContent.CAPTURED_WHITE;
						case CAPTURED_BLACK: grid[i][j] = CellContent.BLACK;
						default:  grid[i][j] = CellContent.CAPTURED_FREE;
						}
					}
					
				}
			}
		}
	}
	
	private ArrayList<ArrayList<Point>> findLoop(int row, int col, CellContent player) {
		Point nw = new Point(-1, -1);
		Point n = new Point(0, -1);
		Point ne = new Point(1, -1);
		Point e = new Point(1, 0);
		Point se = new Point(1, 1);
		Point s = new Point(0, 1);
		Point sw = new Point(-1, 1);
		Point w = new Point(-1, 0);
		Point[] directions = new Point[] {nw, n, ne, e, se, s, sw, w};
		
		Point firstPiece = new Point(row,col);
		
		ArrayList<ArrayList<Point>> loops = new ArrayList<ArrayList<Point>>();
		ArrayList<Point> discovered = new ArrayList<Point>();
		
		ArrayList<Point> positionList = new ArrayList<Point>();
		ArrayList<ArrayList<Point>> pathList = new ArrayList<ArrayList<Point>>();
		
		positionList.add(firstPiece);
		pathList.add(new ArrayList<Point>());
		for (int i = 0; i < positionList.size(); i++) {
			Stack<Point> queue = new Stack<Point>();
			queue.push(positionList.get(i));
			Point prevPosition = positionList.get(i);
			while (!queue.empty()) {
				Point currentPosition = queue.pop();
				if (!discovered.contains(currentPosition)) {
					discovered.add(currentPosition);
					pathList.get(i).add(currentPosition);
					ArrayList<Point> nextPositionAvailable = new ArrayList<Point>();
					
					// check for same piece type in 8 directions
					for (Point dir : directions) {
						Point nextPosition = new Point(currentPosition.x + dir.x, currentPosition.y + dir.y);
						if (!prevPosition.equals(nextPosition) && grid[nextPosition.x][nextPosition.y] == player) {
							nextPositionAvailable.add(nextPosition);
						}
					}
					
					// process first available piece
					if (nextPositionAvailable.size() > 0) {
						if (!discovered.contains(nextPositionAvailable.get(0))) {
							queue.push(nextPositionAvailable.get(0));
							prevPosition = currentPosition;
						} else if (nextPositionAvailable.get(0) == firstPiece) {
							loops.add(new ArrayList<Point>(pathList.get(i)));
						}
					}
					
					// put the rest in the queue
					for (int j = 1; j < nextPositionAvailable.size(); j++) {
						positionList.add(nextPositionAvailable.get(j));
						pathList.add(new ArrayList<Point>(pathList.get(i)));
					}
				}
			}
		}
		return loops;
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




