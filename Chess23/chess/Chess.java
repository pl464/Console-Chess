package chess;

public class Chess {
	
	static Piece[][] board;
	
	public static void main(String[] args) {
		board = new Piece[8][8];
		initBoard(); //initializes the board
		printBoard(); //prints the board
	}

	static void initBoard () {
		
		//create and place the black pieces
		board[0][0] = new Rook("bR", 0, 0);
		board[0][7] = new Rook ("bR", 0, 7);
		board[0][1] = new Knight ("bN", 0, 1);
		board[0][6] = new Knight ("bN", 0, 6);
		board[0][2] = new Bishop ("bB", 0, 2);
		board[0][5] = new Bishop("bB", 0, 5);
		board[0][3] = new Queen("bQ", 0, 3);
		board[0][4] = new King("bK", 0, 4);
		for (int i = 0; i < 8; i++) {
			board[1][i] = new Pawn("bP", 1, i);
		}
		
		//create and place the white pieces
		board[7][0] = new Rook("wR", 0, 0);
		board[7][7] = new Rook ("wR", 0, 7);
		board[7][1] = new Knight ("wN", 0, 1);
		board[7][6] = new Knight ("wN", 0, 6);
		board[7][2] = new Bishop ("wB", 0, 2);
		board[7][5] = new Bishop("wB", 0, 5);
		board[7][3] = new Queen("wQ", 0, 3);
		board[7][4] = new King("wK", 0, 4);
		for (int i = 0; i < 8; i++) {
			board[6][i] = new Pawn("wP", 1, i);
		}
		
		//everything else is set to a null piece
		for (int i = 2; i < 6; i++) {
			for (int j = 0; j < 8; j++) {
				board[i][j] = null;
			}
		}
	}
	
	static void printBoard() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				
				if (board[i][j] != null) { //if there's a piece, print it
					System.out.print(board[i][j].name); 
				} 
				else if (i % 2 == 1 ^ j % 2 == 1 || i % 2 == 0 ^ j % 2 == 0) {
					System.out.print("##"); //this is a black tile
				} 
				else System.out.print("  "); //this is a white tile
				System.out.print(" ");
				
			} System.out.println(i + 1); //prints the row reference to the right of the board
		}
		System.out.println(" a  b  c  d  e  f  g  h  ");
	}
}

