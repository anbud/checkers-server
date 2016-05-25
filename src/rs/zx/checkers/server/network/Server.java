package rs.zx.checkers.server.network;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

import rs.zx.checkers.server.model.Game;
import rs.zx.checkers.server.model.Player;

public class Server {
	public static final Object mutex = new Object();
	
	private static final HashMap<Player, Connection> connectionMap = new HashMap<>();
	private static final HashMap<String, Game> gameMap = new HashMap<>();
        
	private static final LinkedList<Connection> allConnections = new LinkedList<>();
	
	public static final String serverName = "checkers-server";
	public static final String version = "0.9.2";
	
	public static void assignConnection(Player player, Connection con) {
		synchronized(mutex) {
			connectionMap.put(player, con);
			con.setPlayer(player);
		}
	}
	
	public static void addConnection(Connection con) {
		synchronized(mutex) {
			allConnections.add(con);
			
			try {
                            Command.valueOf("FREE_USERS").run(con);
                            Command.valueOf("GAMES").run(con);
			} catch (Exception e) {}
		}
	}
	
	public static void newGame(String name, Game game) {
		synchronized(mutex) {
                        gameMap.put(name, game);
		}
	}
        
        public static void removeGame(String id) {
            synchronized(mutex) {
                gameMap.remove(id);
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
	
	public static Player getPlayer(String name) {
		synchronized(mutex) {
			Player[] p = connectionMap.keySet().stream().filter(i -> {
				return i.getName().equalsIgnoreCase(name);
			}).toArray(Player[]::new);
			
			return p.length > 0 ? p[0] : null;
		}
	}
	
	public static Collection<Game> getGames() {
		synchronized(mutex) {
			return gameMap.values();
		}
	}
	
	public static boolean availableName(String name) {
		synchronized(mutex) {
			return !connectionMap.keySet().stream().anyMatch(p -> {
				return p.getName().equalsIgnoreCase(name);
			});
		}
	}	
	
	public static Game getPlayerGame(Player p) {
		synchronized(mutex) {
			Game[] g = gameMap.values().stream().filter(i -> {
				return i.getPlayers().contains(p);
			}).toArray(Game[]::new);
			
			return g.length > 0 ? g[0] : null;
		}
	}
	
	public static void sendMessage(Player p, String message) {
		synchronized(mutex) {			
			allConnections.stream().forEach(i -> {
				i.sendMessage((p == null ? "E_LOBBY_INFO: " : "E_LOBBY_MESSAGE: ") + (p != null ? (p.getName() + ": ") : " ") + message);
			});
		}
	}
	
	public static void sendMessage(Player p, Game g, String message) {
		synchronized(mutex) {
			ArrayList<Player> players = g.getPlayers();
			
			players.stream().forEach(i -> {
				getConnection(i).sendMessage((p == null ? "E_INFO: " : "E_MESSAGE: ") + (p != null ? (p.getName() + ": ") : " ") + message);
			});
		}
	}
	
	public static void sendGameEvent(Game g, String event, String... args) {
		synchronized(mutex) {
			ArrayList<Player> players = g.getPlayers();
			
			players.stream().forEach(i -> {
				getConnection(i).sendMessage(event + (args.length == 0 ? "" : ": " + Arrays.toString(args).replaceAll("(?:\\[|\\]|,)", "")));
			});
		}
	}
	
	public static void playerLeft(Player p) {
		synchronized(mutex) {
			Game g = getPlayerGame(p);
                        
                        if(g != null)
                            g.leaveGame(p);
			
			allConnections.remove(getConnection(p));
                        connectionMap.remove(p);
			Server.broadcastUsers();
		}
	}
	
	public static void broadcastUsers() {
		synchronized(mutex) {
			allConnections.stream().forEach(i -> {
				try {
					Command.valueOf("FREE_USERS").run(i);
				} catch (Exception e) {}
			});
		}
	}
	
	public static void broadcastGames() {
		synchronized(mutex) {
			allConnections.stream().forEach(i -> {
				try {
					Command.valueOf("GAMES").run(i);
				} catch (Exception e) {}
			});
		}
	}
	
	public static boolean freeUser(Player p) {
		synchronized(mutex) {
			return Server.getPlayerGame(p) == null;
		}
	}
} 
 