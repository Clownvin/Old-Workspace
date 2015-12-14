package com.limeapi.connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.limeapi.server.AbstractServer;

public final class ConnectionAcceptor extends Thread {
    private static volatile boolean kill = false;
    private static AbstractServer serverObject = null;
    private static int port = 0;
    private static final ConnectionAcceptor SINGLETON = new ConnectionAcceptor();

    private ConnectionAcceptor() {
	// To prevent instantion
    }

    public static void start(final AbstractServer newServerObject,
	    final int newPort) {
	serverObject = newServerObject;
	port = newPort;
	kill = false;
	SINGLETON.start();
    }
    
    public static void kill() {
	kill = true;
    }

    @Override
    public void run() {
	try {
	    ServerSocket acceptor = new ServerSocket(port, 10);
	    System.out
		    .println("[ConnectionAcceptor] Acceptor running on port: "
			    + port + ".");
	    while (!kill) {
		try {
		    Socket socket = acceptor.accept();
		    serverObject.addConnection(socket);
		} catch (IOException e) {
		    System.err
			    .println("[ConnectionAcceptor] Error accepting new connection.");
		    e.printStackTrace();
		}
	    }
	    acceptor.close();
	    System.out.println("[ConnectionAcceptor] ConnectionAcceptor killed.");
	} catch (IOException e) {
	    System.err
		    .println("[ConnectionAcceptor] Failed to bind acceptor. Is the port "
			    + port + " already in use?");
	    e.printStackTrace();
	}
    }
}
