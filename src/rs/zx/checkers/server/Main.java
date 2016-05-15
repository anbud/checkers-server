package rs.zx.checkers.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import rs.zx.checkers.server.network.Connection;

public class Main {
	public static void main(String args[]) throws IOException {
		ServerSocket socket = new ServerSocket(1338);
		while(true) {
			new Thread(new Connection(socket.accept())).start();
		}
	}
}
