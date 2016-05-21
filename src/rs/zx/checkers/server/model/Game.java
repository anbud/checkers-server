package rs.zx.checkers.server.model;

import java.io.Serializable;
import java.util.ArrayList;

import rs.zx.checkers.server.exceptions.GameException;
import rs.zx.checkers.server.network.Server;

public class Game implements Serializable {
	private static final long serialVersionUID = -8027592139480577678L;
	
	private String identifier;
	private ArrayList<Player> players = new ArrayList<Player>();
	private Board board = new Board();
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
		
		if(players.size() < 2) {
			players.add(p);
			Server.broadcastUsers();
			Server.sendMessage(null, this, p.getName() + " has joined the game room!");
		} else
			throw new GameException("Game " + identifier + " has no room for more players!");
		
		if(players.size() == 2) {
			Server.sendMessage(null, this, "Game has started.");
			Server.sendGameEvent(this, "E_GAME_STARTED");
			
			Server.broadcastGames();
		}
	}
	
	public void leaveGame(Player p) throws GameException {
		if(players.contains(p)) {
			over = true;
			
			players.remove(p);
		
			Server.sendMessage(null, this, p.getName() + " has left the game room!");
			Server.sendGameEvent(this, "E_GAME_OVER");
			
			Server.broadcastUsers();
		}
	}
	
	public void playMove(int fx, int fy, int tx, int ty, int... et) {
		Field nf = board.getXY(fx, fy).clone();
		
		board.setXY(tx, ty, nf);
		board.getXY(fx, fy).setFigure(null);
		
		checkState();
	}
	
	public void playEat(int x, int y) {
		board.getXY(x, y).setFigure(null);
	}
	
	public void checkState() {
		over = board.getRedCount() == 0 || board.getWoodCount() == 0;
		if(over)
			Server.sendGameEvent(this, "E_GAME_OVER");
	}
	
	public void setOver(boolean f) {
		over = f;
		
		if(over)
			Server.sendGameEvent(this, "E_GAME_OVER");
	}
	
	public boolean isOver() {
		return over;
	}
}
