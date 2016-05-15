package rs.zx.checkers.server.network;

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
	    		
	    		con.sendMessage("E_OK");
    		} else {
    			con.sendMessage("E_NO_PLAYER");
    		}
    	}
    },
    JOIN_GAME(1) {
    	@Override
    	public void run(Connection con, String[] arguments) throws Exception {    	
    		if(con.getPlayer() != null) {
	    		Server.getGame(arguments[0]).joinGame(con.getPlayer());
	    		
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
