package com.server.connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.ctp.server.Server;
import com.ctp.server.SubServer;
import com.ctp.users.UserConnection;

/**
 * 
 * @author Calvin Gene Hall
 *
 */

public final class ConnectionAcceptor extends Thread implements Runnable {

	private final Server server;
	private final int port;
	private volatile boolean kill = false;

	public ConnectionAcceptor(final Server server, final int port) {
		this.server = server;
		this.port = port;
	}

	public void kill() {
		this.kill = true;
	}

	@Override
	public void run() {
		try {
			server.getServerIO().print("[" + this + "] Acceptor running.");
			ServerSocket acceptor = new ServerSocket(port, 10);
			while (!kill) {
				if (server.getSubServerManager().allSubServersFull()) {
					if (server.getSubServerManager().size() < server
							.getSubServerManager().getMaxSubs()) {
						server.getSubServerManager().add(
								new SubServer(server, server
										.getSubServerManager().size(), server
										.getSubServerManager()
										.getSubServerMaxSize()));
						server.getServerIO()
								.print("["
										+ this
										+ "] Creating new SubServer "
										+ (server.getSubServerManager().size() - 1)
										+ ".");
					}
				}
				try {
					Socket socket = acceptor.accept();
					SubServer lowestPopulated = server.getSubServerManager()
							.getLowestPopulatedServer();
					if (server.getSubServerManager().ipLoggedIn(
							socket.getInetAddress().getHostAddress())) {
						server.getServerIO().printErr(
								"["
										+ this
										+ "] IP "
										+ socket.getInetAddress()
												.getHostAddress()
										+ " is already logged in. Rejecting.");
						socket.close();
						continue;
					}
					UserConnection newConnection = new UserConnection(socket,
							lowestPopulated);
					if (lowestPopulated.getSize() >= server
							.getSubServerManager().getSubServerMaxSize()) {
						server.getServerIO()
								.printErr(
										"["
												+ this
												+ "] All servers full, rejecting connection.");
						newConnection.kill();
						continue;
					}
					lowestPopulated.addConnection(newConnection);
				} catch (IOException e) {
					server.getServerIO().printErr(
							"[" + this + "] Error accepting new connection.");
					server.getServerIO().writeException(e);
				}
				if (server.getSubServerManager().subServerEmpty()
						&& server.getSubServerManager().size() > 1) {
					for (int i = 0; i < server.getSubServerManager().size(); i++) {
						if (server.getSubServerManager().get(i).getSize() <= 0) {
							server.getSubServerManager().get(i).kill();
						}
					}
				}
			}
			acceptor.close();
		} catch (IOException e) {
			server.getServerIO()
					.print("["
							+ this
							+ "] Error. Failed to bind acceptor. Already in use?");
			server.getServerIO().writeException(e);
		}
	}

	@Override
	public String toString() {
		return "ConnectionAcceptor";
	}
}
