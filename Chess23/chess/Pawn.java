package chess;

import java.util.*;
import java.io.*;

/**
@author Lance Luo
*/
public class Pawn extends Piece {
	public int passant = -1;

	Pawn (char color, char type) {
		super(color, type);
	}
	
	public boolean validMove(Piece[][] board, int startCol, int startRow, int endCol, int endRow, int turn) {
		int xDist = endCol - startCol;
		int yDist = endRow - startRow;
		int colorRow = (color == 'w') ? 6 : 1;
		int colorDir = (color == 'w') ? -1 : 1;
		Piece end = board[endRow][endCol];
		Piece passing = board[startRow][endCol];
		if (xDist == 0 && end == null) {
			//normal move
			if (yDist == colorDir) {
				return true;
			}
			//initial move
			else if (yDist == (colorDir * 2) && startRow == colorRow && board[startRow + colorDir][startCol] == null) {
				return true;
			}
		}
		else if (Math.abs(xDist) == 1 && yDist == colorDir) {
			//normal capture
			if (end != null) {
				return true;
			}
			//en passant
			else if (passing != null && passing.color != color && passing.type == 'p' && ((Pawn) board[startRow][endCol]).passant == (turn - 1)) {
				return true;
			}
		}
		return false;
	}
}