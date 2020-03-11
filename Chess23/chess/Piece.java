package chess;

import java.util.*;
import java.io.*;

/**
@author Lance Luo
*/
public abstract class Piece {
	public char color, type;
	
	Piece (char color, char type) {
		this.color = color;
		this.type = type;
	}
	
	public abstract boolean validMove(Piece[][] board, int startCol, int startRow, int endCol, int endRow, int turn);
}