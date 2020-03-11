package chess;

import java.util.*;
import java.io.*;

/**
@author Lance Luo
*/
public class Knight extends Piece {
	Knight (char color, char type) {
		super(color, type);
	}
	
	public boolean validMove(Piece[][] board, int startCol, int startRow, int endCol, int endRow, int turn) {
		int xDist = endCol - startCol;
		int yDist = endRow - startRow;
		if (Math.abs(xDist * yDist) == 2) {
			return true;
		}
		return false;
	}
}