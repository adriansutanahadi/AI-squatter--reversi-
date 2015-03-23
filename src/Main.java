import java.awt.print.Printable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Main {
	
	
	// Ctrl-D for mac for cancel
	
	//If there is a syntactic error in the input or an incorrect number of positions, 
	//your program should detect the error, output a short error message and exit. 
	//Your program should produce no other output than what is specified above.
	public static void main(String[] args) {
		Board grid;
		Boolean firstRead = true;
		String input;
		Integer dimension;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		// TODO Auto-generated method stub
		
	      try {
	    	  while((input = br.readLine()) != null)
	    		  if (firstRead) {
	    			  dimension = Integer.parseInt(input);
	    			  firstRead = false;
	    			  grid = new Board(dimension);
	    		  }
	    		  else {
	    			  //Add input to block
	    		  }
	       } catch (IOException ioe) {
	          System.out.println("IO error trying to read your name!");
	          System.exit(1);
	       }

	}

}
