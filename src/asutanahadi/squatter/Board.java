// Board Class for the game of squatter coded by Denis Thamrin and Adrian Sutanahadi
package asutanahadi.squatter;
import java.awt.Point;
import java.util.ArrayList;

public class Board {
	
	// Board Declaration
	public enum CellContent {
		BLACK,
		WHITE,
		CAPTURED,
		FREE
	}

	private Integer dimension;
	private Integer blackScore;
	private Integer whiteScore;
	private CellContent[][] grid = null;
	private ArrayList<Point> capturedCellsMap = new ArrayList<Point>();
	private ArrayList<CellContent> capturedCellsOwner;
	private boolean finished = true;
	
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
		return finished;
	}

	public ArrayList<Point> getCapturedCellsMap() {
		return capturedCellsMap;
	}
	
	// Initialize the board
	public Board(Integer dimension){
		this.dimension = dimension;
		grid = new CellContent[dimension][dimension];
	}

	// insert cell content at the specific row
	// row starts from 0
	public CellContent[][] addContent(String input, Integer y) throws Exception{
		String[] input_array = input.split(" ");
		Integer lengthOfInput = input_array.length;
		
		// check whether the input supplied enough rows
		if (lengthOfInput != this.dimension){
			throw new Exception(String.format("Expected %d number of columns at row %d.",this.dimension,y));
		}
		
		// Populate the two-dimensional array
		for (int x = 0; x < dimension; x++){
			
				// Add to grid
				CellContent cellcontent = stringToCellContent(input_array[x]);
				grid[x][y] = cellcontent;
				
				// Add position of captured cells to map
				if (cellcontent == CellContent.CAPTURED) {
					capturedCellsMap.add(new Point(x,y));
				}
				
				// Check if there is a free cell
				if (cellcontent == CellContent.FREE) {
					finished = false;
				}
		}
		return this.grid;
	}
	
	// Convert input string to CellContent Enum
	private CellContent stringToCellContent(String s) throws Exception{
		switch (s) {
			case "B": return CellContent.BLACK;
			case "W": return CellContent.WHITE;
			case "-": return CellContent.CAPTURED;
			case "+": return CellContent.FREE;
			default: throw new Exception(String.format("String <%s> does not correspond to Cell Enum please check the input string !\n",s));
		}
	}
	
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
			if (grid[check.x][check.y] == CellContent.CAPTURED){
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
			if (grid[check.x][check.y] == CellContent.CAPTURED){
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
			if (grid[check.x][check.y] == CellContent.CAPTURED){
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
			if (grid[check.x][check.y] == CellContent.CAPTURED){
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




