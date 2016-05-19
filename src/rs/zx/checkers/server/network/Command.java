package rs.zx.checkers.server.network;

import rs.zx.checkers.server.exceptions.GameException;
import rs.zx.checkers.server.model.Game;
import rs.zx.checkers.server.model.Player;
import rs.zx.checkers.server.utils.Utils;

public enum Command {
	LOGIN(1) {
		@Override
		public void run(Connection con, String... arguments) throws Exception {
			if(Server.availableName(arguments[0])) {
				Player p = new Player(Utils.randomId(6), arguments[0]);

				Server.assignConnection(p, con);

				con.sendMessage("E_OK");
			} else {
				con.sendMessage("E_USERNAME_TAKEN");
			}
		}			
	},
	/*CREATE_GAME(0) {
		@Override
		public void run(Connection con, String... arguments) throws Exception {
			if(con.getPlayer() != null) {
				
			} else {
				con.sendMessage("E_NO_PLAYER");
			}
		}
	},
	JOIN_GAME(1) {
		@Override
		public void run(Connection con, String... arguments) throws Exception {	 	
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
	},*/
	GAME_REQUEST(1) {
		@Override
		public void run(Connection con, String... arguments) throws Exception {	 	
			if(con.getPlayer() != null) {
				Player p = Server.getPlayer(arguments[0]);
				
				if(p != null) {
					Connection c = Server.getConnection(p);
					c.sendMessage("E_GAME_REQUEST: " + con.getPlayer().getName());
					
					con.sendMessage("E_OK");
				} else {
					con.sendMessage("E_NO_PLAYER");
				}
			} else {
				con.sendMessage("E_NO_PLAYER");
			}
		}
	},
	GAME_ACCEPT(1) {
		@Override
		public void run(Connection con, String... arguments) throws Exception {	 	
			if(con.getPlayer() != null) {
				Player p = Server.getPlayer(arguments[0]);
				
				if(p != null && Server.freeUser(p)) {
					Connection c = Server.getConnection(p);
					
					String id = Utils.randomId(10);
					
					Game g = new Game(id);
				
					Server.newGame(id, g);
					
					g.joinGame(p);
					g.joinGame(con.getPlayer());
					
					c.sendMessage("E_GAME_ACCEPTED: " + con.getPlayer().getName());
					con.sendMessage("E_OK: " + id);
				} else {
					con.sendMessage("E_NO_PLAYER");
				}
			} else {
				con.sendMessage("E_NO_PLAYER");
			}
		}
	},
	GAME_DECLINE(1) {
		@Override
		public void run(Connection con, String... arguments) throws Exception {	 	
			if(con.getPlayer() != null) {
				Player p = Server.getPlayer(arguments[0]);
				
				if(p != null) {
					Connection c = Server.getConnection(p);
					
					c.sendMessage("E_GAME_DECLINED: " + con.getPlayer().getName());
				} else {
					con.sendMessage("E_NO_PLAYER");
				}
			} else {
				con.sendMessage("E_NO_PLAYER");
			}
		}
	},
	LEAVE_GAME(1) {
		@Override
		public void run(Connection con, String... arguments) throws Exception {	 	
			if(con.getPlayer() != null) {
				Server.getGame(arguments[0]).leaveGame(con.getPlayer());
			
				con.sendMessage("E_OK");
			} else {
				con.sendMessage("E_NO_PLAYER");
			}
		}
	},
	GAMEMSG(1) {
		@Override
		public void run(Connection con, String... arguments) throws Exception {
			if(con.getPlayer() != null) {
				Game g = Server.getPlayerGame(con.getPlayer());
				if(g != null) {
					Server.sendMessage(con.getPlayer(), g, arguments[0]);
					
					con.sendMessage("E_OK");
				} else {
					con.sendMessage("E_NO_GAME");
				}
			} else {
				con.sendMessage("E_NO_PLAYER");
			}
		}
	},
	LOBBYMSG(1) {
		@Override
		public void run(Connection con, String... arguments) throws Exception {
			if(con.getPlayer() != null) {
				Server.sendMessage(con.getPlayer(), arguments[0]);
					
				con.sendMessage("E_OK");
			} else {
				con.sendMessage("E_NO_PLAYER");
			}
		}
	},
	MOVE(6) {
		@Override
		public void run(Connection con, String... arguments) throws Exception {
			if(con.getPlayer() != null) {
				Game g = Server.getGame(arguments[0]);
				Player p = con.getPlayer();
				 
				if(g != null) {
					if(g.getCurrentPlayer() == p) {
						g.changePlayer();
						
						try {
							g.playMove(Integer.parseInt(arguments[1]), Integer.parseInt(arguments[2]), Integer.parseInt(arguments[3]), Integer.parseInt(arguments[4]), arguments[5].equals("true"));
							
							if(g.isOver())
								Server.sendMessage(null, g, "Game is over!");
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
		public void run(Connection con, String... arguments) throws Exception {
			con.setLag((System.currentTimeMillis()-con.getLastPingTime())/1000.0);
			con.sendMessage("E_LAG: " + con.getLag());
		} 
	},
	//for fun
	HELP(0) {
		@Override
		public void run(Connection con, String... arguments) throws Exception {
			con.sendMessage("E_COMMANDS:\r\nLOGIN\r\nPONG\r\nCREATE GAME\r\nJOIN GAME\r\nLEAVE GAME\r\nPRIVMSG\r\nMOVE\r\nUSERS\r\nHELP");
		}		 
	},
	USERS(0) {
		@Override
		public void run(Connection con, String... arguments) throws Exception {
			con.sendMessage("E_USERS:");
			Server.getPlayers().stream().forEach(i -> {
				con.sendMessage(i.getName());
			});
		}		 
	},
	FREE_USERS(0) {
		@Override
		public void run(Connection con, String... arguments) throws Exception {
			con.sendMessage("E_USERS:");
			Server.getPlayers().stream().filter(i -> {
				return Server.getPlayerGame(i) == null;
			}).forEach(i -> {
				con.sendMessage(i.getName());
			});			
		}		 
	},
	GAMES(0) {
		@Override
		public void run(Connection con, String... arguments) throws Exception {
			con.sendMessage("E_GAMES:");
			Server.getGames().stream().forEach(con::sendMessage);
		}		 
	},
	STATE(1) {
		@Override
		public void run(Connection con, String... arguments) throws Exception {
			if(con.getPlayer() != null) {
				Game g = Server.getGame(arguments[0]);
				 
				if(g != null) {
					con.sendMessage("E_STATE:");
					con.sendMessage(Utils.toString(g));
				} else 
					con.sendMessage("E_NO_GAME");				
			} else
				con.sendMessage("E_NO_PLAYER");
		}
	};

	private int argumentCount;

	private Command(int argumentCount) {
		this.argumentCount = argumentCount;
	}

	public int getArgumentCount() {
		return argumentCount;
	}

	public abstract void run(Connection con, String... arguments) throws Exception;
}
