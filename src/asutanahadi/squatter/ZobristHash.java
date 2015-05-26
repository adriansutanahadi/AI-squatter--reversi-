package asutanahadi.squatter;

import java.awt.Point;
import java.security.SecureRandom;

import javax.print.attribute.HashAttributeSet;

import asutanahadi.squatter.Board.CellContent;


// Adapted from Artificial Intelligence for Games 
// Class to calculate and initialize a zobrist table
public class ZobristHash {
	//make a dictionary 0 = BLACk 1 = WHITE 2= Capture white 3= Captured Black 4 = Captured Free
	private final int PIECE_TYPE = 5;

	
	private long zobristKey[][][] = null;
	
	public long[][][] getZobristKey() {
		return zobristKey;
	}

	private int dimension;


	
	public ZobristHash(int dimension){
		zobristKey = new long[dimension][dimension][PIECE_TYPE];
		this.dimension = dimension;
		initZobristKey();
	}
	
	// Hash the each non empty element of the board xoring it to return
	// a single hash
	public long getHash(Board b){
		long result = 0;
		for (int i = 0; i < b.getDimension(); i++) {
			for (int j = 0 ; j < b.getDimension(); j++){
				CellContent tmp = b.getGrid()[i][j];
				if (tmp == CellContent.FREE){
					continue;
				} else if (tmp == CellContent.BLACK) {
					result = result ^ zobristKey[i][j][0];
				} else if (tmp == CellContent.WHITE){
					result = result ^ zobristKey[i][j][1];
				} else if (tmp == CellContent.CAPTURED_BLACK){
					result = result ^ zobristKey[i][j][2];
				} else if (tmp == CellContent.CAPTURED_WHITE){
					result = result ^ zobristKey[i][j][3];
				}
			}
		}
		return result;
	}
	
	private void initZobristKey() {
		for (int i = 0; i < this.dimension; i++) {
			for (int j = 0; j < this.dimension; j++){
				for (int k = 0; k < PIECE_TYPE; k++){
					zobristKey[i][j][k] = random64();
				}
			}
		}
	}
	
    private long random64() {
        SecureRandom random = new SecureRandom();
        return random.nextLong();
    }


}
