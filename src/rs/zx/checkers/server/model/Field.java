package rs.zx.checkers.server.model;

import java.io.Serializable;

public class Field implements Cloneable, Serializable {
	private static final long serialVersionUID = -4714397373339202135L;
	
	private boolean black;
	private boolean empty;
	private Figure figure;
	
	public Field clone() {
		return new Field(black, empty, figure != null ? new Figure(figure) : null);
	}
	
	public Field(boolean black) {
		this.black = black;
	}
	
	public Field(boolean black, boolean empty, Figure f) {
		this.black = black;
		this.empty = empty;
		figure = f;
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
