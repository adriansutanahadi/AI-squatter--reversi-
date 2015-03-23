//Board Class for the game of squatter coded by Denis Thamrin
import java.awt.Point;
import java.util.ArrayList;

import org.junit.internal.runners.model.EachTestNotifier;


public class Board {
	
	// Board Declaration
	public enum CellContent {
		BLACK,
		WHITE,
		CAPTURED,
		FREE
	}


	private Integer dimension;
	private CellContent[][] grid = null;
	private ArrayList<Point> capturedCellsMap = new ArrayList<Point>();
	
	//Getter Setters
	public Integer getDimension() {
		return dimension;
	}

	public CellContent[][] getGrid() {
		return grid;
	}

	public ArrayList<Point> getCapturedCellsMap() {
		return capturedCellsMap;
	}
	
	public Board(Integer dimension){
		this.dimension = dimension;
		grid = new CellContent[dimension][dimension];
	}

	// insert cell content at the specific row
	// ROW STAETS FROM 0
	public CellContent[][] addContent(String input, Integer Row){
		String[] input_array = input.split(" ");
		assert(input_array.length == dimension);
		for (int i = 0; i< dimension; i ++){
			try {
				// Add to grid
				CellContent cellcontent = StringToCellContent(input_array[i]);
				grid[Row][i] = cellcontent;
				
				// Add position of captured cells to map
				if (cellcontent == CellContent.CAPTURED) {
					capturedCellsMap.add(new Point(Row,i));
				}
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return this.grid;
	}
	
	private CellContent StringToCellContent(String s) throws Exception{
		switch (s) {
			case "B": return CellContent.BLACK;
			case "W": return CellContent.WHITE;
			case "-": return CellContent.CAPTURED;
			case "+": return CellContent.FREE;
			default: throw new Exception(String.format("String <%s> does not correspond to Cell Enum please check the input string !\n",s));
		}
	}
}




