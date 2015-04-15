package asutanahadi.squatter;
import java.io.IOException;
import java.util.Scanner;

public class Main {
	
	
	// Ctrl-D for mac for cancel
	
	//If there is a syntactic error in the input or an incorrect number of positions, 
	//your program should detect the error, output a short error message and exit. 
	//Your program should produce no other output than what is specified above.
	public static void main(String[] args) {
		Board grid = null;
		Boolean firstRead = true;
		String input = null;
		String result = null;
		Integer dimension = 0;
		Integer lineCount = 0;
		Scanner sc = new Scanner(System.in);
		
	    try {
	    	// read input
	    	while(sc.hasNextLine()) {
	    		// record input
	    		input = sc.nextLine();
	    		
	    		// if there is no more input, break the loop
	    		if (input.isEmpty()) {
	    			break;
	    		}
	    		
	    		// record dimension from the first line
	    		if (firstRead) {
	    			dimension = Integer.parseInt(input);
	    			firstRead = false;
	    			grid = new Board(dimension);
	    		}
	    		else {
	    			
	    			if (dimension == 0) {
	    		    	// close the scanner
	    		    	sc.close();
	    				throw new Exception("Dimension cannot be 0.");
	    			}
	    			
	    			// Add input to block
	    			if (lineCount <= dimension) {
	    				grid.addContent(input, lineCount-1);
	    			} else {
	    		    	// close the scanner
	    		    	sc.close();
	    				throw new Exception("Too much rows from the input.");
	    			}
	    		}
	    		lineCount++;
	    	}
	    	
	    	// close the scanner
	    	sc.close();
	    	
	    	// check if the input is enough
	    	if (lineCount <= dimension) {
	    		throw new Exception("Not enough row input.");
	    	}
	    	
	    	// update the score
        	grid.updateScore();
        	
        	// check if the game has ended and who wins
        	if (!grid.isFinished()) {
        		result = "None";
        	}
        	else if (grid.getBlackScore() < grid.getWhiteScore()){
        		result = "White";
        	} else if (grid.getBlackScore() > grid.getWhiteScore()){
        		result = "Black";
        	} else {
        		result = "Draw";
        	}
        	
	    } catch (IOException ioe) {
	    	System.out.println("IO error trying to read your name!");
	        System.exit(1);
	    } catch (Exception e) {
	    	System.out.println(e);
	    	System.exit(1);
	    }
    	
    	// write output
	    System.out.println(result);
	    System.out.println(grid.getWhiteScore());
	    System.out.println(grid.getBlackScore());
	    
	}
}
