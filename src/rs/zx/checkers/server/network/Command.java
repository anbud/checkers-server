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
    MOVE(3) {
    	@Override
    	public void run(Connection con, String[] arguments) throws Exception {
    		if(con.getPlayer() != null) {
    			if(Server.getGame(arguments[0]) != null) {
    				
	    		
    				con.sendMessage("E_OK");
    			} else {
    				con.sendMessage("E_NO_GAME");
    			}
	    	} else {
				con.sendMessage("E_NO_PLAYER");
			}
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
