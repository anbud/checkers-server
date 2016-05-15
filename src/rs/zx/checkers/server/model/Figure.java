package rs.zx.checkers.server.model;

public class Figure {
	public static final int RED = 1;
	public static final int WOOD = 2;
	
	private int tip;
	
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
}
