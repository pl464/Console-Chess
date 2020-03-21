package chess;

/**
@author Lance Luo
@author Patrick Lee
*/
public class Rook extends Piece {
	public boolean canCastle = true;

	Rook (char color) {
		super(color, 'R');
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