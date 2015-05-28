// Board Class for the game of squatter coded by Denis Thamrin and Adrian Sutanahadi
package asutanahadi.squatter;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class Board {
	
	// Board Declaration
	// Changed each captured to individual CAPTURED BLACK / WHITE
	public static enum CellContent {
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
	private ArrayList<ArrayList<Point>> loops;
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
		return (getFreeCellCount() == 0);
	}
	
	public Integer getFreeCellCount() {
		return freeCellCount;
	}
	
	protected void setFreeCellCount(Integer freeCellCount) {
		this.freeCellCount = freeCellCount;
	}

	protected ArrayList<Point> getCapturedCellsMap() {
		return capturedCellsMap;
	}

	public ArrayList<CellContent> getCapturedCellsOwner() {
		return capturedCellsOwner;
	}
	
	public ArrayList<ArrayList<Point>> getLoops() {
		return this.loops;
	}
	
	// convert the loop into a Polygon object
	public ArrayList<Polygon> getLoopsInPolygon() {
		ArrayList<Polygon> loopsInPolygon = new ArrayList<Polygon>();
		for (ArrayList<Point> loop : this.loops) {
			// convert the loop into a Polygon object
			Polygon loopPolygon = new Polygon();
			for (Point p : loop) {
				loopPolygon.addPoint(p.x, p.y);
			}
			loopsInPolygon.add(loopPolygon);
		}
		return loopsInPolygon;
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
		this.setFreeCellCount(dimension*dimension);
	}
	
	public Board(Board b) {
		this.dimension = b.getDimension();
		grid = new CellContent[dimension][dimension];
		this.whiteScore = b.getWhiteScore();
		this.blackScore = b.getBlackScore();
		this.setFreeCellCount(b.getFreeCellCount());
		this.capturedCellsMap = new ArrayList<Point>(b.getCapturedCellsMap());
		Board.copy_grid(this, b);
	}
	
	// Add a single piece to the specified location, return True if succeed 
	public Boolean addPiece(int x, int y, CellContent player) {
		if (grid[x][y] == CellContent.FREE){
			grid[x][y] = player;
			setFreeCellCount(getFreeCellCount() - 1);
			updateBoard(x, y, player);
			try {
				updateScore();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		} else {
			return false;
		}
		
	}
	
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
					capturedCellsMap.add(new Point(p.x, p.y));
					setFreeCellCount(getFreeCellCount() - 1);
					break;
				case BLACK:
					grid[p.x][p.y] = CellContent.CAPTURED_BLACK;
					capturedCellsMap.add(new Point(p.x, p.y));
					break;
				case CAPTURED_WHITE:
					grid[p.x][p.y] = CellContent.WHITE;
					capturedCellsMap.remove(new Point(p.x, p.y));
					break;
				default:
				}
			} else if (player == CellContent.BLACK) {
				switch (piece) {
				case FREE:
					grid[p.x][p.y] = CellContent.CAPTURED_FREE;
					capturedCellsMap.add(new Point(p.x, p.y));
					setFreeCellCount(getFreeCellCount() - 1);
					break;
				case WHITE:
					grid[p.x][p.y] = CellContent.CAPTURED_WHITE;
					capturedCellsMap.add(new Point(p.x, p.y));
					break;
				case CAPTURED_BLACK:
					grid[p.x][p.y] = CellContent.BLACK;
					capturedCellsMap.remove(new Point(p.x, p.y));
					break;
				default:
				}
			}
		}
	}
	
	protected ArrayList<Point> floodFill (int x, int y, CellContent player) {
		ArrayList<Point> processedPoints = new ArrayList<Point>();
		if (!checkCellValidity(new Point(x, y)) || grid[x][y] == player) {
			return processedPoints;
		}
		// define the 4 directions
		Point n = new Point(0, -1);
		Point e = new Point(1, 0);
		Point s = new Point(0, 1);
		Point w = new Point(-1, 0);
		Point[] directions = new Point[] {n, e, s, w};
		
		Stack<Point> queue = new Stack<Point>();
		queue.push(new Point(x, y));
		while(!queue.empty()) {
			Point currentPosition = queue.pop();
			if (grid[currentPosition.x][currentPosition.y] != player) {
				processedPoints.add(currentPosition);
				for (Point dir : directions) {
					Point nextPosition = new Point(currentPosition.x + dir.x, currentPosition.y + dir.y);
					if (!checkCellValidity(nextPosition)) {
						return new ArrayList<Point>();
					}
					if (checkCellValidity(nextPosition) && !processedPoints.contains(nextPosition)) {
						queue.push(nextPosition);
					}
				}
			}
		}
		return processedPoints;
	}
	
//	protected void updateBoard(int x, int y, CellContent player) {
//		
//		findLoop(x, y, player);
//		ArrayList<Polygon> loopsInPolygon = getLoopsInPolygon();
//
//		
//		// for each loop, check if there are new captured pieces
//		for (Polygon loopPolygon : loopsInPolygon) {
//			
//			// limit the area of the search
//			Rectangle checkArea = loopPolygon.getBounds();
//			Point topLeft = new Point(checkArea.x, checkArea.y);
//			Point bottomLeft = new Point(checkArea.x + checkArea.width, checkArea.y + checkArea.height);
//			
//			// search the area for new captured pieces
//			for (int i = topLeft.x; i <= bottomLeft.x; i++) {
//				for (int j = topLeft.y; j <= bottomLeft.y; j++) {
//					Point piecePosition = new Point(i, j);
//					CellContent piece = grid[i][j];
//					
//					// convert the piece to the captured ones if it is inside the polygon(loop)
//					if (player == CellContent.WHITE) {
//						if (loopPolygon.contains(piecePosition)) {
//							switch (piece) {
//							case FREE:
//								grid[i][j] = CellContent.CAPTURED_FREE;
//								capturedCellsMap.add(new Point(i, j));
//								freeCellCount--;
//								break;
//							case BLACK:
//								grid[i][j] = CellContent.CAPTURED_BLACK;
//								capturedCellsMap.add(new Point(i, j));
//								break;
//							case CAPTURED_WHITE:
//								grid[i][j] = CellContent.WHITE;
//								capturedCellsMap.remove(new Point(i, j));
//								break;
//							default:
//							}
//						}
//					} else if (player == CellContent.BLACK) {
//						if (loopPolygon.contains(piecePosition)) {
//							switch (piece) {
//							case FREE:
//								grid[i][j] = CellContent.CAPTURED_FREE;
//								capturedCellsMap.add(new Point(i, j));
//								freeCellCount--;
//								break;
//							case WHITE:
//								grid[i][j] = CellContent.CAPTURED_WHITE;
//								capturedCellsMap.add(new Point(i, j));
//								break;
//							case CAPTURED_BLACK:
//								grid[i][j] = CellContent.BLACK;
//								capturedCellsMap.remove(new Point(i, j));
//								break;
//							default:
//							}
//						}
//					}
//				}
//			}
//		}
//	}

	
//	private void findLoop(int x, int y, CellContent player) {
//		
//		// define the 8 directions
//		Point nw = new Point(-1, -1);
//		Point n = new Point(0, -1);
//		Point ne = new Point(1, -1);
//		Point e = new Point(1, 0);
//		Point se = new Point(1, 1);
//		Point s = new Point(0, 1);
//		Point sw = new Point(-1, 1);
//		Point w = new Point(-1, 0);
//		Point[] directions = new Point[] {n, e, s, w, nw, ne, se, sw};
//		
//		Point firstPiece = new Point(x,y);
//
//		this.loops = new ArrayList<ArrayList<Point>>(); // to store loops
//		
//		// to store the state when there is an intersection
//		ArrayList<Point> positionList = new ArrayList<Point>();
//		ArrayList<Point> prevPositionList = new ArrayList<Point>();
//		ArrayList<ArrayList<Point>> pathList = new ArrayList<ArrayList<Point>>();
//		ArrayList<Integer> pathListIndex = new ArrayList<Integer>();
//		
//		// initialise first move (which is the first piece)
//		positionList.add(new Point(firstPiece));
//		prevPositionList.add(new Point(firstPiece));
//		pathList.add(new ArrayList<Point>());
//		pathListIndex.add(new Integer(0));
//		
//		for (int i = 0; i < positionList.size(); i++) {
//			Stack<Point> queue = new Stack<Point>();
//			ArrayList<Point> path = new ArrayList<Point>(pathList.get(pathListIndex.get(i)));
//			queue.push(positionList.get(i));
//			Point prevPosition = prevPositionList.get(i);
//			
//			// use depth-first search to check for a loop
//			while (!queue.empty()) {
//				Point currentPosition = queue.pop();
//				if (!path.contains(currentPosition)) {
//					// visit the piece
//					path.add(new Point(currentPosition));
//					
//					// check for available pieces to go to in 8 directions
//					ArrayList<Point> nextPositionAvailable = new ArrayList<Point>();
//					for (Point dir : directions) {
//						Point nextPosition = new Point(currentPosition.x + dir.x, currentPosition.y + dir.y);
//						if (!checkCellValidity(nextPosition)) {
//							continue;
//						}
//						if (!prevPosition.equals(nextPosition) && grid[nextPosition.x][nextPosition.y] == player) {
//							nextPositionAvailable.add(new Point(nextPosition));
//						}
//					}
//					
//					// if there is less than 2 piece surrounding the first piece, there is no loop.
//					if (currentPosition.equals(firstPiece) && nextPositionAvailable.size() < 2) {
//						break;
//					}
//					boolean intersect = false;
//					for (int j = 0; j < nextPositionAvailable.size(); j++) {
//						if (nextPositionAvailable.get(j).equals(firstPiece)) {
//							// store the path if it creates a loop
////							this.loops.add(new ArrayList<Point>(pathList.get(i)));
//							ArrayList<ArrayList<Point>> removalList = new ArrayList<ArrayList<Point>>();
//							boolean addLoop = false;
//							for (ArrayList<Point> loop : this.loops) {
//								if (loop.size() < path.size()) {
//									// check if loop is inside pathList.get(i)
//									boolean inside = true;
//									for (Point p : loop) {
//										if (!path.contains(p)) {
//											inside = false;
//										}
//										if (inside) {
//											addLoop = true;
//											if (!removalList.contains(loop)) removalList.add(loop);
//										}
//									}
//								} else {
//									// check if pathList.get(i) is inside loop
//									boolean inside = true;
//									for (Point p : path) {
//										if (!loop.contains(p)) {
//											inside = false;
//										}
//										if (!inside) {
//											addLoop = true;
//										}
//									}
//								}
//							}
//							for (ArrayList<Point> loop : removalList) {
//								this.loops.remove(loop);
//							}
//							if (this.loops.size() == 0) {
//								addLoop = true;
//							}
//							if (addLoop && path.size() > 3) this.loops.add(new ArrayList<Point>(path));
//						} else if (!path.contains(nextPositionAvailable.get(j))) {
//							if (j == 0) {
//								// mark next piece to be traversed
//								queue.push(nextPositionAvailable.get(0));
//								prevPosition = currentPosition;
//							} else {
//								// store the other pieces, so that it can be continued when this one has finished traversing
//								positionList.add(new Point(nextPositionAvailable.get(j)));
//								prevPositionList.add(new Point(currentPosition));
//								intersect = true;
//								pathListIndex.add(new Integer(pathList.size()));
//							}
//						}
//						if (intersect) {
//							pathList.add(new ArrayList<Point>(path));
//						}
//					}
//				}
//			}
//		}
//	}
	/*
	 * Generate all possible moves in current board
	 * With Move ordering where position the leftmost, right most, top and bottom position is added last
	 * As it is considered low value (can't be captured and can't capture anything)
	 * Also prioritize position that is around non empty cell 
	 * It is to simulate a real player where the player will look around the non empty cell and place it
	 * Around there to block or create loop
	 */ 
	public ArrayList<Point> getMove(){
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
		
		ArrayList<Point> moves = new ArrayList<Point>();
		ArrayList<Point> front = new ArrayList<Point>();
		ArrayList<Point> back = new ArrayList<Point>();
		for (int i = 0; i < this.dimension; i++) {
			for (int j = 0; j < this.dimension; j++) {
				
				if (grid[i][j] == CellContent.FREE){
					// put the low value node to the back of the list
					if ((i == 0 && j == 0) ||( i == 0 && j == this.dimension - 1) || (i == this.dimension - 1 && j == 0)
							|| i == this.dimension - 1 && j == this.dimension - 1 ){
						back.add(new Point(i, j));
						continue;
					}

					// put the cell in front of the list if it is adjacent to a piece
					boolean putAtFront = false;
					for (Point dir : directions) {
						Point neighbourPoint = new Point(i + dir.x, j + dir.y);
						if (checkCellValidity(neighbourPoint) && (grid[neighbourPoint.x][neighbourPoint.y] == CellContent.BLACK || grid[neighbourPoint.x][neighbourPoint.y] == CellContent.WHITE)) {
							putAtFront = true;
							break;
						}
					}
					if (putAtFront) {
						front.add(new Point(i, j));
					} else {
						moves.add(new Point(i, j));
					}
				}
			}
		}
		
		// added randomness to make the game less predictable
		Collections.shuffle(moves);
		Collections.shuffle(front);
		Collections.shuffle(back);
		
		// add front to the front of the list
		moves.addAll(0, front);
		moves.addAll(back);

		return moves;
	}
	
	public static void copy_grid(Board target,Board copy){
		CellContent[][] target_grid = target.getGrid();
		CellContent[][] copy_grid = copy.getGrid();
		for (int i = 0; i < copy.getDimension(); i++) {
			for (int j = 0 ; j<copy.getDimension();j++){
				target_grid[i][j] = copy_grid[i][j];
			}
		}
		
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
	protected boolean checkCellValidity(Point p){
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
		
		if (grid[p.x][p.y] == CellContent.CAPTURED_BLACK) {
			return CellContent.WHITE;
		}
		if (grid[p.x][p.y] == CellContent.CAPTURED_WHITE) {
			return CellContent.BLACK;
		}
		
		// check upwards
		check = new Point(p);
		check.y -= 1;
		while (checkCellValidity(check)){
			if (isCaptured(grid[check.x][check.y])){
				check.y -= 1;
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
				check.x -= 1;
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




