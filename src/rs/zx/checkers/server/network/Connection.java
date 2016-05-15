package rs.zx.checkers.server.network;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.LinkedBlockingQueue;
import java.io.OutputStream;

import rs.zx.checkers.server.model.Player;

public class Connection implements Runnable {    
	private Socket socket;
	private Player player;
    
    public Connection(Socket socket) {
        this.socket = socket;
    }

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
	
	private LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<String>();
	
	@Override
	public void run() {
		try {
			new Thread(() -> {
				try {
	                OutputStream out = socket.getOutputStream();
	                while(!Thread.interrupted()) {
	                    String s = queue.take();
	                    
	                    s = s.replace("\n", "").replace("\r", "");
	                    s = s + "\r\n";
	                    
	                    out.write(s.getBytes());
	                    out.flush();
	                }
	            } catch(Exception e) {	 
	            	//Again, we lost Jim
	            	queue.clear();
	            	queue = null;
	            	
	                try {
	                    socket.close();
	                } catch (Exception e1) {}
	            }

			}).start();
			
	        InputStream in = socket.getInputStream();
	        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
	        
	        String line;
	        
	        while((line = reader.readLine()) != null)
	            process(line);
	        
        } catch (Exception e) {
        	e.printStackTrace();
            try {
                socket.close();
            } catch (Exception e1) {}
        } finally {
            if(player != null && Server.getConnection(player) == this) {
                //we lost Jim
            }
        }
	}
	
	private void process(String line) throws Exception {
		String[] split = null;
		
		String commandString = "";
		String[] arguments = new String[0];
		
		if(!line.equals("")) {
			split = line.split(":");
			
			commandString = split[0];
			
			if(split.length > 1)			
				arguments = split[1].trim().split(" ");
		}
        
        Command command = null;
        
        try {
            command = Command.valueOf(commandString.toUpperCase().replace(" ", "_"));
        } catch (Exception e) {
        	sendMessage("E_COMMAND: " + commandString);
        	return;
        }
        
        if(arguments.length != command.getArgumentCount()) {
        	sendMessage("E_ARGUMENT_COUNT: " + commandString + " " + arguments.length);
            return;
        }
        
        command.run(this, arguments);
	}
	
	public void sendMessage(String s) {
        if(queue != null)
        	queue.add(s);
    }
}