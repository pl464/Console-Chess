package chess;

public abstract class Piece {
	String name;
	private int xpos, ypos; //only to be used within class
	
	Piece (String name, int xpos, int ypos) {
		this.name = name;
		this.xpos = xpos;
		this.ypos = ypos;
	}
	
	void checkDestination() {
		//xpos and ypos will be used here
	}
	
	void checkIfBlocked() {
		//xpos and ypos will be used here
	}
}

class Rook extends Piece { 
	Rook(String name, int xpos, int ypos) {
		super(name, xpos, ypos);
	}
}

class Knight extends Piece{
	Knight(String name, int xpos, int ypos) {
		super(name, xpos, ypos);
	}
}

class Bishop extends Piece{
	Bishop(String name, int xpos, int ypos) {
		super(name, xpos, ypos);
	}
}

class Queen extends Piece{
	Queen(String name, int xpos, int ypos) {
		super(name, xpos, ypos);
	}
}

class King extends Piece{
	King(String name, int xpos, int ypos) {
		super(name, xpos, ypos);
	}
}

class Pawn extends Piece{
	Pawn(String name, int xpos, int ypos) {
		super(name, xpos, ypos);
	}
}


