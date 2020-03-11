package chess;

import java.util.*;
import java.io.*;

/**
@author Lance Luo
*/
public class Chess {

	public static Piece[][] board = new Piece[8][8];

	public static void initialize() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (i == 0) {
					if (j == 0 || j == 7) {
						board[i][j] = new Rook('b', 'R');
					}
					else if (j == 1 || j == 6) {
						board[i][j] = new Knight('b', 'N');
					}
					else if (j == 2 || j == 5) {
						board[i][j] = new Bishop('b', 'B');
					}
					else if (j == 3) {
						board[i][j] = new Queen('b', 'Q');
					}
					else if (j == 4) {
						board[i][j] = new King('b', 'K');
					}
				}
				else if (i == 1) {
					board[i][j] = new Pawn('b', 'p');
				}
				else if (i == 6) {
					board[i][j] = new Pawn('w', 'p');
				}
				else if (i == 7) {
					if (j == 0 || j == 7) {
						board[i][j] = new Rook('w', 'R');
					}
					else if (j == 1 || j == 6) {
						board[i][j] = new Knight('w', 'N');
					}
					else if (j == 2 || j == 5) {
						board[i][j] = new Bishop('w', 'B');
					}
					else if (j == 3) {
						board[i][j] = new Queen('w', 'Q');
					}
					else if (j == 4) {
						board[i][j] = new King('w', 'K');
					}
				}
				else {
					board[i][j] = null;
				}
			}
		}
	}

	public static void printBoard() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (board[i][j] == null) {
					System.out.print(((i + j) % 2 == 0) ? "   " : "## ");
				}
				else {
					System.out.print(board[i][j].color);
					System.out.print(board[i][j].type + " ");
				}
			}
			System.out.println(8 - i);
		}
		System.out.println(" a  b  c  d  e  f  g  h\n");
	}

	public static boolean isIllegal(String s, int i) {
		//parse user input
		int startCol = s.toLowerCase().charAt(0) - 97;
		int startRow = 8 - Character.getNumericValue(s.charAt(1));
		int endCol = s.toLowerCase().charAt(3) - 97;
		int endRow = 8 - Character.getNumericValue(s.charAt(4));
		Piece start = board[startRow][startCol];
		Piece end = board[endRow][endCol];

		//check that starting piece exists
		if (start == null) {
			return true;
		}

		//check that starting piece is player's color
		char c = (i % 2 == 1) ? 'w' : 'b';
		if (start.color != c) {
			return true;
		}

		//check that ending piece, if exists, is opponent's color
		if (end != null && end.color == c) {
			return true;
		}

		//check that piece movement is legal
		if (start.validMove(board, startCol, startRow, endCol, endRow, i) == false) {
			return true;
		}

		return false;
	}

	public static void makeMove(String s, int i) {
		//parse user input
		int startCol = s.toLowerCase().charAt(0) - 97;
		int startRow = 8 - Character.getNumericValue(s.charAt(1));
		int endCol = s.toLowerCase().charAt(3) - 97;
		int endRow = 8 - Character.getNumericValue(s.charAt(4));
		Piece start = board[startRow][startCol];
		Piece end = board[endRow][endCol];

		//move
		board[endRow][endCol] = start;
		board[startRow][startCol] = null;

		//castling
		if (start.type == 'R') {
			((Rook) start).canCastle = false;
		}
		if (start.type == 'K') {
			if (Math.abs(endCol - startCol) == 2) {
				int rookCol = (endCol > startCol) ? 7 : 0;
				int rookRow = (start.color == 'w') ? 7 : 0;
				int newCol = (endCol > startCol) ? 5 : 3;
				board[rookRow][newCol] = board[rookRow][rookCol];
				board[rookRow][rookCol] = null;
				((King) start).canCastle = false;
				((Rook) board[rookRow][newCol]).canCastle = false;
			}
			else {
				((King) start).canCastle = false;
			}
		}

		//en passant
		if (start.type == 'p') {
			if (Math.abs(endRow - startRow) == 2) {
				((Pawn) start).passant = i;
			}
			if (endCol != startCol && end == null) {
				board[startRow][endCol] = null;
			}
					
		}

		//pawn promotion
		int colorRow = (start.color == 'w') ? 0 : 7;
		if (start.type == 'p' && endRow == colorRow) {
			if (s.length() > 6) {
				switch (s.charAt(6)) {
					case 'R':
						board[endRow][endCol] = new Rook(start.color, s.charAt(6));
						break;
					case 'N':
						board[endRow][endCol] = new Knight(start.color, s.charAt(6));
						break;
					case 'B':
						board[endRow][endCol] = new Bishop(start.color, s.charAt(6));
						break;
					case 'Q':
					default:
						board[endRow][endCol] = new Queen(start.color, s.charAt(6));
						break;
				}
			}
			else {
				board[endRow][endCol] = new Queen(start.color, 'Q');
			}
		}
		return;
	}

	public static boolean isCheckmate(String s, int i) {
		//implement
		return false;
	}

	public static boolean isCheck(String s, int i) {
		//implement
		return false;
	}

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		String move;
		int turn = 1;
		initialize();
		printBoard();

		while (true) {
			System.out.print(((turn % 2 == 1) ? "White" : "Black") + "'s move: ");
			move = scanner.nextLine();

			//check if game over before move
			if (move.equals("draw")) {
				break;
			}
			System.out.println();
			if (move.equals("resign")) {
				System.out.print(((turn % 2 == 1) ? "Black" : "White") + " wins");
				break;
			}

			if (isIllegal(move, turn)) {
				System.out.println("Illegal move, try again\n");
				continue;
			}
			makeMove(move, turn);
			printBoard();

			//check if game over after move
			if (isCheck(move, turn)) {
				if (isCheckmate(move, turn)) {
					System.out.println("Checkmate\n");
					System.out.print(((turn % 2 == 1) ? "White" : "Black") + " wins");
					break;
				}
				else {
					System.out.println("Check\n");
				}
			}
			turn++;
		}
	}
}