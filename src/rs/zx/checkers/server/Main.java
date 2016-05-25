package rs.zx.checkers.server;

import java.io.IOException;
import java.net.ServerSocket;

import rs.zx.checkers.server.network.Connection;

public class Main {
	public static void main(String args[]) throws IOException {
		try(ServerSocket socket = new ServerSocket(Integer.parseInt(args[0]))) {
			while(true) {
				new Thread(new Connection(socket.accept())).start();
			}
		}
	}
}
