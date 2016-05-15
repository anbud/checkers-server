package rs.zx.checkers.server.model;


public class Board {
	private Field[][] board;
	
	public Board() {
		board = new Field[10][10];
		for(int i = 0; i < 10; i++)
			for(int j = 0; j < 10; j++)
				board[i][j] = new Field(i+j % 2 == 0);
	}
	
	public void setXY(int x, int y, Field f) {
		board[x][y] = f;
	}
	
	public Field getXY(int x, int y) {
		return board[x][y];
	}
}
