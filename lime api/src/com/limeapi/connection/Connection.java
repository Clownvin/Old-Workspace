package com.limeapi.connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.limeapi.util.IDTagDistributer.IDTag;

public final class Connection {
    private OutputStream out = null;
    private InputStream in = null;
    private final Socket socket;
    private final IDTag connectionIDTag;
    private final ConnectionAddress connectionAddress;
    private final String ipAddress;
    
    public Connection(final Socket socket, final IDTag connectionIDTag, final int subServerIndex) {
	this.socket = socket;
	this.ipAddress = socket.getInetAddress().getHostAddress();
	this.connectionIDTag = connectionIDTag;
	this.connectionAddress = new ConnectionAddress(connectionIDTag.getId(), subServerIndex);
	try {
	    this.in = socket.getInputStream();
	    this.out = socket.getOutputStream();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
    
    public ConnectionAddress getConnectionAddress() {
	return connectionAddress;
    }
    
    public int getConnectionID() {
	return connectionIDTag.getId();
    }
    
    public String getIPAddress() {
	return ipAddress;
    }
    
    public InputStream getInputStream() {
	return in;
    }
    
    public OutputStream getOutputStream() {
	return out;
    }
    
    public Socket getSocket() {
	return socket;
    }
    
    public void close() {
	try {
	    in.close();
	    out.close();
	    socket.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	connectionIDTag.returnToDistributer();
    }
}
