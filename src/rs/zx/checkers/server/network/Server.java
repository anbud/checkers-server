package rs.zx.checkers.server.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import rs.zx.checkers.server.exceptions.GameException;
import rs.zx.checkers.server.model.Game;
import rs.zx.checkers.server.model.Player;

public class Server {
	public static final Object mutex = new Object();
	
	private static HashMap<Player, Connection> connectionMap = new HashMap<Player, Connection>();
	private static HashMap<String, Game> gameMap = new HashMap<String, Game>();
	
	public static String serverName = "checkers-server";
	public static String version = "0.0.5";
	
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
	
	public static Set<Player> getPlayers() {
		synchronized(mutex) {
			return connectionMap.keySet();
		}
	}
	
	public static boolean availableName(String name) {
		synchronized(mutex) {
			return !connectionMap.keySet().stream().anyMatch(p -> {
				if(p.getName().equalsIgnoreCase(name))
					return true;
				
				return false;
			});
		}
	}	
	
	public static void sendMessage(Player p, Game g, String message) {
		synchronized(mutex) {
			ArrayList<Player> players = g.getPlayers();
			
			players.stream().forEach(i -> {
				getConnection(i).sendMessage("E_MESSAGE: " + (p != null ? (p.getName() + ": ") : " ") + message);
			});
		}
	}
	
	public static void playerLeft(Player p) {
		synchronized(mutex) {
			for(Game g : gameMap.values()) {
				if(g.getPlayers().contains(p))
					try {
						g.leaveGame(p);
					} catch(GameException e) {}//will not happen
			}
		}
	}
} 
