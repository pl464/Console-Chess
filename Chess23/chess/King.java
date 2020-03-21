package chess;

import chess.Chess;
/**
@author Lance Luo
@author Patrick Lee
*/
public class King extends Piece {
	public boolean canCastle = true;

	King (char color) {
		super(color, 'K');
	}
	
	public boolean validMove(Piece[][] board, int startCol, int startRow, int endCol, int endRow, int turn) {
		int xDist = endCol - startCol;
		int yDist = endRow - startRow;
		int rookCol = (endCol > startCol) ? 7 : 0;
		int rookRow = (color == 'w') ? 7 : 0;
		int xDir = (xDist < 0) ? -1 : 1;
		Piece corner = board[rookRow][rookCol];
		if (xDist == 0 && yDist == 0) {
			return false;
		}
		if (Math.abs(xDist) <= 1 && Math.abs(yDist) <= 1) {
			return true;
		}
		if (canCastle && Math.abs(xDist) == 2 && Math.abs(yDist) == 0) {
			if (Chess.isCheck(this.color)) {
				return false;
			}
			if (corner != null && corner.type == 'R' && ((Rook) corner).canCastle) {
				for (int i = 1; i < Math.abs(rookCol - startCol); i++) {
					if (board[startRow][startCol + (xDir * i)] != null) {
						return false;
					} else { //test if a check will result along the way
						board[startRow][startCol + (xDir * i)] = this;
						board[startRow][startCol] = null;
						if (Chess.isCheck(this.color)) {
							board[startRow][startCol] = this;
							board[startRow][startCol + (xDir * i)] = null;
							return false;
						}
						board[startRow][startCol] = this;
						board[startRow][startCol + (xDir * i)] = null;
					}
				}
				return true;
			}
		}
		return false;
	}
}