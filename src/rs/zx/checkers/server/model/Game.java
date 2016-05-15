package rs.zx.checkers.server.model;

import java.util.ArrayList;

import rs.zx.checkers.server.exceptions.GameException;

public class Game {
	private String identifier;
	private ArrayList<Player> players = new ArrayList<Player>();
	private Board board;
	private Player currentPlayer;
	private boolean over;
	
	public Game(String identifier) {
		this.identifier = identifier;
	}
	
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	public ArrayList<Player> getPlayers() {
		return players;
	}
	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}
	public Board getBoard() {
		return board;
	}
	public void setBoard(Board board) {
		this.board = board;
	}
	public Player getCurrentPlayer() {
		return currentPlayer;
	}
	public void changePlayer() {
		currentPlayer = players.get(0) == currentPlayer ? players.get(1) : players.get(0);
	}
	
	public void joinGame(Player p) throws GameException {
		if(players.size() == 0)
			currentPlayer = p;
		
		if(players.size() < 2)
			players.add(p);
		else
			throw new GameException("Game " + identifier + " has no room for more players!");
	}
	
	public void leaveGame(Player p) throws GameException {
		if(players.contains(p))
			over = true;
	}
	
	public void playMove(int fx, int fy, int tx, int ty, boolean eaten) {
		Field nf = board.getXY(fx, fy).clone();
		
		board.setXY(tx, ty, nf);
		board.getXY(fx, fy).setFigure(null);
		
		if(eaten) {
			int iter = Math.abs(fx-tx)-1;
			
			for(int i = 0; i < iter; i++)
				board.getXY(fx+1, fy+1).setFigure(null);
		}
	}
	
	public void checkState() {
		over = board.getRedCount() == 0 || board.getWoodCount() == 0;
	}
	
	public boolean isOver() {
		return over;
	}
}
