package rs.zx.checkers.server.model;

import java.io.Serializable;

public class Figure implements Serializable {
	private static final long serialVersionUID = -1339191942108905670L;
	
	public static final int RED = 1;
	public static final int WOOD = 2;
	
	private int tip;
	public boolean queen;
	
	public Figure(Figure f) {
		this.tip = f.getTip();
	}
	
	public Figure(int tip) {
		this.tip = tip;
	}
	
	public void setTip(int tip) {
		this.tip = tip;
	}
	
	public int getTip() {
		return tip;
	}
	
	public void setQueen(boolean queen) {
		this.queen = queen;
	}
	
	public boolean getQueen() {
		return queen;
	}
}
