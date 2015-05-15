package asutanahadi.squatter;

import static org.junit.Assert.fail;

import org.junit.Test;

public class GenericPlayerTestModule {
	private Player P1;
	private Player P2;
	private Move lastPlayedMove;
	private int board_size;
	
	public GenericPlayerTestModule(FirstDumbPlayer p1,FirstDumbPlayer p2,int board_size) {
		this.P1 = p1;
		this.P2 = p2;
		this.board_size = board_size;
		// TODO Auto-generated constructor stub
	}

	public void refreeTest() {
		

		
		lastPlayedMove = new Move();
		int NumberofMoves = 0;
		int boardEmptyPieces=Integer.valueOf(board_size * board_size);

		P1.init(Integer.valueOf(board_size), Piece.WHITE);
		P2.init(Integer.valueOf(board_size), Piece.BLACK);
		
		
		while(boardEmptyPieces > 0 && P1.getWinner() == 0 && P2.getWinner() ==0)
		{
			
			NumberofMoves++;
			System.out.println(NumberofMoves);
			lastPlayedMove=P1.makeMove();
			System.out.println("Placing to. "+lastPlayedMove.Row+":"+lastPlayedMove.Col+" by "+lastPlayedMove.P);		
			P1.printBoard(System.out);
			boardEmptyPieces--;			

			if(P2.opponentMove(lastPlayedMove)<0)
			{
				System.out.println("Exception: Player 2 rejected the move of player 1.");
				P1.printBoard(System.out);
				P2.printBoard(System.out);
				fail("Exception: Player 2 rejected the move of player 1.");

				
				break;
			}			
			else if(P2.getWinner()==0  && P1.getWinner()==0){
				NumberofMoves++;	
				lastPlayedMove = P2.makeMove();
				System.out.println("Placing to. "+lastPlayedMove.Row+":"+lastPlayedMove.Col+" by "+lastPlayedMove.P);
				boardEmptyPieces--;
				P2.printBoard(System.out);
			
			if(P1.opponentMove(lastPlayedMove)<0)
			{
				System.out.println("Exception: Player 1 rejected the move of player 2.");
				P2.printBoard(System.out);
				P1.printBoard(System.out);
				fail("Exception: Player 1 rejected the move of player 2.");
				break;
			}
			}
			
		}
		System.out.println("--------------------------------------");
		System.out.println("P2 Board is :");
		P2.printBoard(System.out);
		System.out.println("P1 Board is :");
		P1.printBoard(System.out);
		
		System.out.println("Player one (White) indicate winner as: "+ P1.getWinner());
		System.out.println("Player two (Black) indicate winner as: "+ P2.getWinner());
		System.out.println("Total Number of Moves Played in the Game: "+ NumberofMoves);
		System.out.println("Referee Finished !");
	}
}
