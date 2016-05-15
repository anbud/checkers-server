package rs.zx.checkers.server.model;

import java.util.Arrays;

public class Board {
	private Field[][] board;
	
	public Board() {
		board = new Field[10][10];
		for(int i = 0; i < 10; i++)
			for(int j = 0; j < 10; j++) {
				Figure f = null;
				
				if((i + j) % 2 != 0 && (i < 4))
					f = new Figure(Figure.RED);
				
				if((i + j) % 2 != 0 && (i > 5))
					f = new Figure(Figure.WOOD);
				
				board[i][j] = new Field(i+j % 2 != 0);
				board[i][j].setFigure(f);
			}
	}
	
	public void setXY(int x, int y, Field f) {
		board[x][y] = f;
	}
	
	public Field getXY(int x, int y) {
		return board[x][y];
	}
	
	public int getRedCount() {
		int c = 0;
		for(int i = 0; i < board.length; i++)
			for(int j = 0; j < board[i].length; j++)
				if(board[i][j].getFigure().getTip() == Figure.RED)
					c++; //ftw
		
		return c;
	}
	
	public int getWoodCount() {
		int c = 0;
		for(int i = 0; i < board.length; i++)
			for(int j = 0; j < board[i].length; j++)
				if(board[i][j].getFigure().getTip() == Figure.WOOD)
					c++; //ftw
		
		return c;
	}
}
