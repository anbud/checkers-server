package rs.zx.checkers.server.network;

import rs.zx.checkers.server.exceptions.GameException;
import rs.zx.checkers.server.model.Game;
import rs.zx.checkers.server.model.Player;
import rs.zx.checkers.server.utils.Utils;

public enum Command {
	LOGIN(1) {
		@Override
		public void run(Connection con, String[] arguments) throws Exception {
			if(Server.availableName(arguments[0])) {
				Player p = new Player(Utils.randomId(6), arguments[0]);

				Server.assignConnection(p, con);

				con.sendMessage("E_OK");
			} else {
				con.sendMessage("E_USERNAME_TAKEN");
			}
		}			
	},
	CREATE_GAME(0) {
		@Override
		public void run(Connection con, String[] arguments) throws Exception {
			if(con.getPlayer() != null) {
				String id = Utils.randomId(10);
			
				Game g = new Game(id);
			
				Server.newGame(id, g);
			 
				g.joinGame(con.getPlayer());

				con.sendMessage("E_OK: " + id);
			} else {
				con.sendMessage("E_NO_PLAYER");
			}
		}
	},
	JOIN_GAME(1) {
		@Override
		public void run(Connection con, String[] arguments) throws Exception {	 	
			if(con.getPlayer() != null) {
				try {
					Server.getGame(arguments[0]).joinGame(con.getPlayer());
				} catch(GameException e) {
					con.sendMessage("E_GAME_FULL");
				}
				
				con.sendMessage("E_OK");
			} else {
				con.sendMessage("E_NO_PLAYER");
			}
		}
	},
	LEAVE_GAME(1) {
		@Override
		public void run(Connection con, String[] arguments) throws Exception {	 	
			if(con.getPlayer() != null) {
				Server.getGame(arguments[0]).leaveGame(con.getPlayer());
			
				con.sendMessage("E_OK");
			} else {
				con.sendMessage("E_NO_PLAYER");
			}
		}
	},
	PRIVMSG(2) {
		@Override
		public void run(Connection con, String[] arguments) throws Exception {
			if(con.getPlayer() != null) {
				if(Server.getGame(arguments[0]) != null) {
					Server.sendMessage(con.getPlayer(), Server.getGame(arguments[0]), arguments[1]);
					
					con.sendMessage("E_OK");
				} else {
					con.sendMessage("E_NO_GAME");
				}
			} else {
				con.sendMessage("E_NO_PLAYER");
			}
		}
	},
	MOVE(6) {
		@Override
		public void run(Connection con, String[] arguments) throws Exception {
			if(con.getPlayer() != null) {
				Game g = Server.getGame(arguments[0]);
				Player p = con.getPlayer();
				 
				if(g != null) {
					if(g.getCurrentPlayer() == p) {
						g.changePlayer();
						
						try {
							g.playMove(Integer.parseInt(arguments[1]), Integer.parseInt(arguments[2]), Integer.parseInt(arguments[3]), Integer.parseInt(arguments[4]), arguments[5].equals("true"));
						} catch(NumberFormatException e) {
							con.sendMessage("E_INVALID_ARGUMENTS");
						}

						con.sendMessage("E_OK");
					} else 
						con.sendMessage("E_INVALID_MOVE");					
				} else 
					con.sendMessage("E_NO_GAME");				
			} else
				con.sendMessage("E_NO_PLAYER");
		}
	},
	PONG(0) {
		@Override
		public void run(Connection con, String[] arguments) throws Exception {
			con.setLag((System.currentTimeMillis()-con.getLastPingTime())/1000.0);
			con.sendMessage("E_LAG: " + con.getLag());
		} 
	},
	//for fun
	HELP(0) {
		@Override
		public void run(Connection con, String[] arguments) throws Exception {
			con.sendMessage("Available commands: LOGIN, PONG, CREATE GAME, JOIN GAME, LEAVE GAME, PRIVMSG, MOVE, HELP.");
		}		 
	};

	private int argumentCount;

	private Command(int argumentCount) {
		this.argumentCount = argumentCount;
	}

	public int getArgumentCount() {
		return argumentCount;
	}

	public abstract void run(Connection con, String[] arguments) throws Exception;
}
