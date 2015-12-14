package com.ctp.connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.ctp.io.ServerIO;
import com.ctp.server.SubServer;
import com.ctp.server.SubServerManager;
import com.ctp.users.UserConnection;

/**
 * 
 * @author Calvin Gene Hall
 *
 */

public final class ConnectionAcceptor extends Thread implements Runnable {
	//TODO Document
	/**
	 * 
	 */
	public static void kill() {
		kill = true;
	}

	private static int port;

	private static volatile boolean kill = false;
	//TODO Document
	/**
	 * 
	 * @param _port
	 */
	public ConnectionAcceptor(final int _port) {
		port = _port;
	}
	//TODO Document
	/**
	 * 
	 */
	@Override
	public void run() {
		try {
			ServerSocket acceptor = new ServerSocket(port, 10);
			ServerIO.print("[" + this + "] Acceptor running. Port: "+port);
			while (!kill) {
				if (SubServerManager.allSubServersFull()) {
					if (SubServerManager.size() < SubServerManager.getMaxSubs()) {
						SubServerManager
								.add(new SubServer(SubServerManager.size(),
										SubServerManager.getSubServerMaxSize()));
						ServerIO.print("[" + this + "] Creating new SubServer "
								+ (SubServerManager.size() - 1) + ".");
					}
				}
				try {
					Socket socket = acceptor.accept();
					SubServer lowestPopulated = SubServerManager
							.getLowestPopulatedServer();
					if (SubServerManager.ipLoggedIn(socket.getInetAddress()
							.getHostAddress())) {
						ServerIO.printErr("[" + this + "] IP "
								+ socket.getInetAddress().getHostAddress()
								+ " is already logged in. Rejecting.");
						socket.close();
						continue;
					}
					UserConnection newConnection = new UserConnection(socket,
							lowestPopulated);
					if (lowestPopulated.getSize() >= SubServerManager
							.getSubServerMaxSize()) {
						ServerIO.printErr("[" + this
								+ "] All servers full, rejecting connection.");
						newConnection.kill();
						continue;
					}
					lowestPopulated.addConnection(newConnection);
				} catch (IOException e) {
					ServerIO.printErr("[" + this
							+ "] Error accepting new connection.");
					ServerIO.writeException(e);
				}
				if (SubServerManager.subServerEmpty()
						&& SubServerManager.size() > 1) {
					for (int i = 0; i < SubServerManager.size(); i++) {
						if (SubServerManager.get(i).getSize() <= 0) {
							SubServerManager.get(i).kill();
						}
					}
				}
			}
			acceptor.close();
		} catch (IOException e) {
			ServerIO.print("[" + this
					+ "] Error. Failed to bind acceptor. Already in use?");
			ServerIO.writeException(e);
		}
	}
	//TODO Document
	/**
	 * 
	 * @return
	 */
	@Override
	public String toString() {
		return "ConnectionAcceptor";
	}
}
