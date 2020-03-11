package chess;

import java.util.*;
import java.io.*;

/**
@author Lance Luo
*/
public class Rook extends Piece {
	public boolean canCastle = true;

	Rook (char color, char type) {
		super(color, type);
	}
	
	public boolean validMove(Piece[][] board, int startCol, int startRow, int endCol, int endRow, int turn) {
		int xDist = endCol - startCol;
		int yDist = endRow - startRow;
		if (xDist == 0 ^ yDist == 0) {
			int xDir = (xDist < 0) ? -1 : (xDist > 0) ? 1 : 0;
			int yDir = (yDist < 0) ? -1 : (yDist > 0) ? 1 : 0;
			for (int i = 1; i < Math.max(Math.abs(xDist), Math.abs(yDist)); i++) {
				if (board[startRow + (yDir * i)][startCol + (xDir * i)] != null) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
}