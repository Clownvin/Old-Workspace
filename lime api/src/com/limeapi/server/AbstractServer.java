package com.limeapi.server;

import java.net.Socket;

public abstract class AbstractServer {
    
    public abstract void addConnection(Socket socket);

}
