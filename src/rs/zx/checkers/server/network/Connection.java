package rs.zx.checkers.server.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.io.OutputStream;

import rs.zx.checkers.server.model.Player;

public class Connection implements Runnable {    
	private Socket socket;
	private Player player;
	private double lag;
	private long ping = System.currentTimeMillis()+15000;
	private LinkedList<Player> reqs = new LinkedList<Player>();
	
	public Connection(Socket socket) {
		this.socket = socket;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public double getLag() {
		return lag;
	}

	public void setLag(double lag) {
		this.lag = lag;
	}
	
	public long getLastPingTime() {
		return ping;
	}

	public void setLastPingTime(long ping) {
		this.ping = ping;
	}
	
	public void addRequest(Player p) {
		reqs.add(p);
	}
	
	public void removeRequest(Player p) {
		reqs.remove(p);
	}
	
	public LinkedList<Player> getReqs() {
		return reqs;
	}
	
	private LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<String>();
	private Timer timer = new Timer();
	
	@Override
	public void run() {
		queue.add("Welcome to " + Server.serverName + " " + Server.version + ". Please LOGIN first or type HELP for all commands.");
		Server.addConnection(this);
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				if(queue != null)
					queue.add("PING");
				
				if(System.currentTimeMillis()-getLastPingTime() >= 14000) {
					try {
						socket.close();
					} catch (IOException e) {
					}
				} else {				
					setLastPingTime(System.currentTimeMillis());
				}
			}
		}, 15000, 15000);
		
		try {
			new Thread(() -> {
				try {
					OutputStream out = socket.getOutputStream();
					while(!Thread.interrupted()) {
						String s = queue.take();
						
						s = s + "\r\n";
						
						out.write(s.getBytes());
						out.flush();
					}
				} catch(Exception e) {	 
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
			try {
				socket.close();
			} catch (Exception e1) {}
		} finally {
			if(player != null && Server.getConnection(player) == this) {
				timer.cancel();
				Server.playerLeft(player);
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
			
			if(split.length > 1) {		
				if(!commandString.equals("GAMEMSG") && !commandString.equals("LOBBYMSG"))
					arguments = split[1].trim().split(" ");
				else {
					arguments = new String[]{split[1].trim()};
				}
			}
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