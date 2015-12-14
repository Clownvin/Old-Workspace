package com.chatroom.main;

import com.chatroom.connection.ConnectionManager;

public final class ChatRoom {
    private static ConnectionManager connectionManager = new ConnectionManager("localhost", 2224);
    private static GUI gui = new GUI();
    public static void main(String[] args) {
	gui.setVisible(true);
    }
    
    public static ConnectionManager getConnectionManager() {
	return connectionManager;
    }
    
    public static GUI getGUI() {
	return gui;
    }
}
