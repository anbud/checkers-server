package rs.zx.checkers.server.model;

import java.util.ArrayList;

import rs.zx.checkers.server.network.Command;
import rs.zx.checkers.server.network.Connection;
import rs.zx.checkers.server.network.Server;

public class Game {
	private String identifier;
	private ArrayList<Player> players = new ArrayList<>();
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
        
	public Player getCurrentPlayer() {
		return currentPlayer;
	}
        
	public void changePlayer() {
		currentPlayer = players.get(0) == currentPlayer ? players.get(1) : players.get(0);
	}
	
	public void joinGame(Player p) throws Exception {
		if(players.size() == 0)
			currentPlayer = p;
		
		if(players.size() < 2) {
			players.add(p);
                        
			Connection c = Server.getConnection(p);
			c.removeRequests();
                        
			Command.valueOf("REQUESTS").run(c);
                        
			Server.broadcastUsers();
		}
                
		if(players.size() == 2) 			
			Server.broadcastGames();
	}
	
	public void leaveGame(Player p) {
		over = true;
			
		players.remove(p);
		
		Server.sendMessage(null, this, p.getName() + " has left the game room!");
		Server.sendGameEvent(this, "E_GAME_OVER");
		
                Server.broadcastUsers();
		Server.broadcastGames();
	}
	
	public void playMove(int fx, int fy) {
		checkState();
	}
	
	public void checkState() {
		if(over) {
			Server.sendGameEvent(this, "E_GAME_OVER");
                        Server.sendMessage(null, this, "Game is over!");
			Server.broadcastGames();
		}
	}
	
	public void setOver(boolean f) {
		over = f;
		
		checkState();
	}
}
