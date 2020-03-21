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
		
		//test to see if the move produces a check
		Piece p0 = board[startRow][startCol];
		Piece p1 = board[endRow][endCol];
		board[endRow][endCol] = p0;
		board[startRow][startCol] = null;
		if (isCheck(c)) {
			board[startRow][startCol] = p0;
			board[endRow][endCol] = p1;
			return true;
		}
		board[startRow][startCol] = p0;
		board[endRow][endCol] = p1;
		
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
	
	public static boolean isCheckmate(char color) {
		//test possible moves to see if they end the check; if none, checkmate detected
		for (int j = 0; j < 8; j++) {
			for (int k = 0; k < 8; k++) {
				Piece p0 = board[j][k]; //save the original piece before moving it
				if (p0 == null || p0.color != color) {
					continue;
				}
				ArrayList<int[]> possMoves = getPossMoves(p0, j, k);
				//for every possible move, try the move 
				for (int[] possMove : possMoves) {
					int testY = possMove[0];
					int testX = possMove[1]; 
					Piece p1 = board[testY][testX]; //save the destination piece
					board[testY][testX] = p0;
					board[j][k] = null;
					if (!isCheck(color)) {
						board[j][k] = p0;
						board[testY][testX] = p1;
						return false; 
					}
					else {
						board[j][k] = p0;
						board[testY][testX] = p1;
					}
				}
			}
		}
		return true;
	}

	//given a color, check if the king is in check 
	public static boolean isCheck(char color) {
		//get king's position as starting point
		int yPos = getKingPos(color)[0];
		int xPos = getKingPos(color)[1];
		
		//search diagonally for enemy bishops and queen
		String[] diagDirections = {"NW", "NE", "SE", "SW"};
		for (int i = 0; i < 4; i++) {
			int[] position = searchInDirection(yPos, xPos, diagDirections[i], 7);
			Piece p = board[position[0]][position[1]];
			if (p != null && (p.type == 'B' || p.type == 'Q') && p.color != color) {
				return true;
			}
		}
		//search horizontally/vertically for enemy rooks and queen
		String[] cardDirections = {"N", "E", "S", "W"};
		for (int i = 0; i < 4; i++) {
			int[] position = searchInDirection(yPos, xPos, cardDirections[i], 7);
			Piece p = board[position[0]][position[1]];
			if (p != null && (p.type == 'R' || p.type == 'Q') && p.color != color) {
				return true;
			}
		}
		//search for checking knights
		int[] arr = {1, 2, -1, -2}; //array to get all tiles where checking knight can be
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) { //try each combo of 1 and 2
				int y = yPos + arr[i]; int x = xPos + arr[j]; //add 1 or 2 to y and x
				if ((arr[i] + arr[j]) % 2 == 0) 
					continue; //don't try combos that aren't a 1 and 2
				if (!(0 <= y && y <= 7 && 0 <= x && x <= 7)) 
					continue; //checks if tile is on the board
				if (board[y][x] == null) 
					continue;
				if (board[y][x].type == 'N' && board[y][x].color != color) 
					return true;
			}
		}
		//search for checking kings 
		String[] allDirections = {"N", "NE", "E", "SE", "S", "SW", "W", "NW"};
		for (int i = 0; i < 8; i++) {
			int[] position = searchInDirection(yPos, xPos, allDirections[i], 1);
			Piece p = board[position[0]][position[1]]; 
			if (p != null && (p.type == 'K') && p.color != color) {
				return true;
			}
		}
		//search for checking pawns
		//reminder: diagDirections = {"NW", "NE", "SE", "SW"};
		int a = 0; int b = 1;
		if (color == 'b') { a = 2; b = 3;}
		for (int i = a; i <= b; i++) {
			int[] position = searchInDirection(yPos, xPos, diagDirections[i], 1);
			Piece p = board[position[0]][position[1]];
			if (p != null && (p.type == 'P') && p.color != color) {
				return true;
			}
		} 
		return false;
	}
	
	public static int[] getKingPos(char color) {
		int[] kingPos = {-1, -1};
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (board[i][j] != null &&
						board[i][j].color == color && board[i][j].type == 'K') {
					kingPos[0] = i; 
					kingPos[1] = j;
					return kingPos;
				}
			}
		}
		return kingPos;
	}
	
	//given a starting tile and direction, returns an int[] representing position at which the first
	//piece is found. if no piece is found, it will return the last position checked
	public static int[] searchInDirection(int yPos, int xPos, String direction, int dist) {
		switch (direction) {
			case "NE":
				while (yPos > 0 && xPos < 7 && dist > 0) {
					yPos--; xPos++; dist--;
					if (board[yPos][xPos] != null) break;
				} break;
			case "SE":
				while (yPos < 7 && xPos < 7 && dist > 0) {
					yPos++; xPos++; dist--;
					if (board[yPos][xPos] != null) break;
				} break;
			case "SW":
				while (yPos < 7 && xPos > 0 && dist > 0) {
					yPos++; xPos--; dist--;
					if (board[yPos][xPos] != null) break;
				} break;
			case "NW":
				while (yPos > 0 && xPos > 0 && dist > 0) {
					yPos--; xPos--; dist--;
					if (board[yPos][xPos] != null) break;
				} break;
			case "N":
				while (yPos > 0 && dist > 0) {
					yPos--; dist--;
					if (board[yPos][xPos] != null) break;
				} break;
			case "E":
				while (xPos < 7 && dist > 0) {
					xPos++; dist--;
					if (board[yPos][xPos] != null) break;
				} break;
			case "S":
				while (yPos < 7 && dist > 0) {
					yPos++; dist--;
					if (board[yPos][xPos] != null) break;
				} break;
			case "W":
				while (xPos > 0 && dist > 0) {
					xPos--; dist--;
					if (board[yPos][xPos] != null) break;
				} break;
		}
		return new int[] {yPos, xPos};
	}

	//given a piece and its location, returns a list of possible moves as {x,y} locations
	public static ArrayList<int[]> getPossMoves(Piece p, int yPos, int xPos){
		ArrayList<int[]> possMoves = new ArrayList<int[]>();
		switch (p.type) {
			case 'B':
				String[] B_Directions = {"NW", "NE", "SE", "SW"};
				int[][] B_Moves = {{-1, -1}, {-1, 1}, {1, 1}, {1, -1}};
				for (int i = 0; i < 4; i++) {
				
					int[] position = searchInDirection(yPos, xPos, B_Directions[i], 7);
					int tilesMoved = Math.abs(position[1] - xPos);
					//if you found a friendly piece, don't count its location as a possible move
					if (board[position[0]][position[1]] != null &&
							board[position[0]][position[1]].color == p.color) {
						tilesMoved--;
					}
					int y = yPos; int x = xPos;
					for (int j = 0; j < tilesMoved; j++) {
						y += B_Moves[i][0];
						x += B_Moves[i][1];
						possMoves.add(new int[] {y, x});
					}
				}
				break;
			
			case 'R':
				String[] R_Directions = {"N", "E", "S", "W"};
				int[][] R_Moves = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
				for (int i = 0; i < 4; i++) {
					int[] position = searchInDirection(yPos, xPos, R_Directions[i], 7);
					int tilesMoved = Math.abs((position[0] - yPos) + (position[1] - xPos));
					
					if (board[position[0]][position[1]] != null &&
							board[position[0]][position[1]].color == p.color) tilesMoved--;
					//System.out.println("In direction " + R_Directions[i] + ", moved " + tilesMoved + " times");
					int y = yPos; int x = xPos;
					for (int j = 0; j < tilesMoved; j++) {
						y += R_Moves[i][0];
						x += R_Moves[i][1];
						possMoves.add(new int[] {y, x});
					}
				}
				break;
			
			case 'Q':
				String[] Q_Directions = {"NW", "NE", "SE", "SW", "N", "E", "S", "W"};
				int[][] Q_Moves = {{-1, -1}, {-1, 1}, {1, 1}, {1, -1}, {-1, 0}, {0, 1}, {1, 0}, {0, -1}};
				for (int i = 0; i < 8; i++) {
					
					int[] position = searchInDirection(yPos, xPos, Q_Directions[i], 7);
					int tilesMoved = Math.abs(position[0] - yPos);
					if (tilesMoved == 0) tilesMoved += Math.abs(position[1] - xPos);
					//if you found a friendly piece, don't count its location as a possible move
					if (board[position[0]][position[1]] != null &&
							board[position[0]][position[1]].color == p.color) {
						tilesMoved--;
					}
					int y = yPos; int x = xPos;
					for (int j = 0; j < tilesMoved; j++) {
						y += Q_Moves[i][0];
						x += Q_Moves[i][1];
						possMoves.add(new int[] {y, x});
					}
				}
				break;
			
			case 'N':
				int[] arr = {1, 2, -1, -2}; //array to get all tiles where checking knight can be
				for (int i = 0; i < 4; i++) {
					for (int j = 0; j < 4; j++) { //try each combo of 1 and 2
						int y = yPos + arr[i]; int x = xPos + arr[j]; //add 1 or 2 to y and x
						if ((arr[i] + arr[j]) % 2 == 0) 
							continue; //don't try combos that aren't a 1 and 2
						if (!(0 <= y && y <= 7 && 0 <= x && x <= 7)) 
							continue; //checks if tile is on the board
						if (board[y][x] == null) {
							possMoves.add(new int[] {y, x});
							continue;}
						if (board[y][x].color != p.color) {
							possMoves.add(new int[] {y, x});
							continue;}
					}
				}
				break;
			
			case 'K':
				String[] K_Directions = {"NW", "NE", "SE", "SW", "N", "E", "S", "W"};
				int[][] K_Moves = {{-1, -1}, {-1, 1}, {1, 1}, {1, -1}, {-1, 0}, {0, 1}, {1, 0}, {0, -1}};
				for (int i = 0; i < 8; i++) {
					int[] position = searchInDirection(yPos, xPos, K_Directions[i], 1);
					int tilesMoved = Math.abs(position[0] - yPos);
					if (tilesMoved == 0) tilesMoved += Math.abs(position[1] - xPos);
					if (board[position[0]][position[1]] != null &&
							board[position[0]][position[1]].color == p.color) {
						tilesMoved--;
					}
					int y = yPos; int x = xPos;
					if (tilesMoved > 0) {
						y += K_Moves[i][0];
						x += K_Moves[i][1];
						possMoves.add(new int[] {y, x});
					}
				}
				break;
				
			case 'p':
				int y; int x = xPos;
				if (p.color == 'b') {
					y = yPos + 1; if (y <= 7 && board[y][x] == null) possMoves.add(new int[] {y, xPos});
					if (yPos == 1) possMoves.add(new int[] {3, xPos});
				} else {
					y = yPos - 1; if (y >= 0 && board[y][x] == null) possMoves.add(new int[] {y, xPos});
					if (yPos == 6) possMoves.add(new int[] {4, xPos});
				}
				if (x != 7) {
					x = xPos + 1; 
					if (board[y][x] != null && board[y][x].color != p.color) 
						possMoves.add(new int[] {y, x});
				} 
				if (x != 0) {
					x = xPos - 1; 
					if (board[y][x] != null && board[y][x].color != p.color) 
						possMoves.add(new int[] {y, x});
				}
				break;
		} 
		return possMoves;
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
			if (isCheck((turn % 2 == 1) ? 'b' : 'w')) { //if black just moved, check wK
				if (isCheckmate((turn % 2 == 1) ? 'b' : 'w')) {
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