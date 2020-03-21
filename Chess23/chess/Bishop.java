package chess;

/**
@author Lance Luo
@author Patrick Lee
*/
public class Bishop extends Piece {
	
	Bishop (char color) {
		super(color, 'B');
	}
	
	public boolean validMove(Piece[][] board, int startCol, int startRow, int endCol, int endRow, int turn) {
		int xDist = endCol - startCol;
		int yDist = endRow - startRow;
		if (Math.abs(xDist) == Math.abs(yDist) && xDist != 0) {
			int xDir = (xDist < 0) ? -1 : 1;
			int yDir = (yDist < 0) ? -1 : 1;
			for (int i = 1; i < Math.abs(xDist); i++) {
				if (board[startRow + (yDir * i)][startCol + (xDir * i)] != null) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
}