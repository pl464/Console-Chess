package chess;

/**
@author Lance Luo
@author Patrick Lee
*/
public class Knight extends Piece {
	Knight (char color) {
		super(color, 'N');
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