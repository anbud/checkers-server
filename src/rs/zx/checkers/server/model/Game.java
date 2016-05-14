package rs.zx.checkers.server.model;

import java.util.ArrayList;

import rs.zx.checkers.server.exceptions.GameException;

public class Game {
	private String identifier;
	private ArrayList<Player> players = new ArrayList<Player>();
	private Board board;
	public Player currentPlayer;
	
	public String state;
	
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
		if(players.size() < 2)
			players.add(p);
		else
			throw new GameException("Game " + identifier + " has no room for more players!");
	}
	
	public void leaveGame(Player p) throws GameException {
		if(players.contains(p))
			state = "Player " + p + " lost!";
	}
	
	public void move(Player p, int x, int y) {
		//todo
	}
	
	public boolean isOver() {
		return false;//todo
	}
}
