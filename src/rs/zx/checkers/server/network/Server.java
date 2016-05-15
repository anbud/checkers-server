package rs.zx.checkers.server.network;

import java.util.ArrayList;
import java.util.HashMap;

import rs.zx.checkers.server.model.Game;
import rs.zx.checkers.server.model.Player;

public class Server {
	public static final Object mutex = new Object();
	
	private static HashMap<Player, Connection> connectionMap = new HashMap<Player, Connection>();
	private static HashMap<String, Game> gameMap = new HashMap<String, Game>();
	
	public static String serverName;
	
	public static void assignConnection(Player player, Connection con) {
		synchronized(mutex) {
			connectionMap.put(player, con);
			con.setPlayer(player);
		}
	}
	
	public static void newGame(String name, Game game) {
		synchronized(mutex) {
			gameMap.put(name, game);
		}
	}
	
	public static Game getGame(String id) {
		synchronized(mutex) {
			return gameMap.get(id);
		}
	}
	
	public static Connection getConnection(Player p) {
		synchronized(mutex) {
			return connectionMap.get(p);
		}
	}
	
	public static boolean availableName(String name) {
		return !connectionMap.keySet().stream().anyMatch(p -> {
			if(p.getName().equalsIgnoreCase(name))
				return true;
			
			return false;
		});
	}	
	
	public static void sendMessage(Player p, Game g, String message) {
		ArrayList<Player> players = g.getPlayers();
		
		players.stream().forEach(i -> {
			getConnection(i).sendMessage("E_MESSAGE: " + p.getName() + " " + message);
		});
	}
} 
