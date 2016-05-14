package rs.zx.checkers.server.model;


public class Field {
	private boolean black;
	private boolean empty;
	private Figure figure;
	
	public Field(boolean black) {
		this.black = black;
	}

	public boolean isBlack() {
		return black;
	}

	public void setBlack(boolean black) {
		this.black = black;
	}

	public boolean isEmpty() {
		return empty;
	}

	public void setEmpty(boolean empty) {
		this.empty = empty;
	}

	public Figure getFigure() {
		return figure;
	}

	public void setFigure(Figure figure) {
		this.figure = figure;
	}
}
