package com.hexane.io;

import com.hexane.server.Server;

/**
 * 
 * @author Calvin Gene Hall
 *
 */

public final class ConsoleManager {

	private final Server server;
	private ConsoleChannel[] channels = new ConsoleChannel[200];
	private int channelIDReference = 0;
	private Console serverConsole;
	public static int CHANNEL_ALL = 0;
	public static int CHANNEL_SERVER = 1;
	public static int CHANNEL_GLOBAL_CHAT = 2;

	public ConsoleManager(final Server server) {
		this.server = server;
		serverConsole = new Console(server);
		addChannel("All", 500);
		addChannel("Server", 500);
		addChannel("Global Chat", 400);
		setActive(ConsoleManager.CHANNEL_ALL);
	}

	public void addChannel(String name, int maxSize) {
		if (channelIDReference + 1 >= 200) {
			server.getServerIO()
					.printErr(
							"["
									+ this
									+ "] Cannot create new channel. Too many exist already!");
			return;
		}
		channels[channelIDReference] = new ConsoleChannel(name, maxSize,
				channelIDReference);
		channelIDReference++;
	}

	public void addConsoleMessage(String message) {
		if (server.getShowGUI()) {
			if (serverConsole == null)
				serverConsole = new Console(server);
			addMessage(message + "\n", ConsoleManager.CHANNEL_ALL);
		}
	}

	public void addConsoleMessage(String message, int channel) {
		if (server.getShowGUI()) {
			if (serverConsole == null)
				serverConsole = new Console(server);
			addMessage(message + "\n", channel);
		}
	}

	private void addMessage(String message, int channelID) {
		if (channelID >= channelIDReference) {
			server.getServerIO().printErr(
					"[" + this + "] No channel for ID: " + channelID);
			return;
		}
		try {
			channels[channelID].addMessage(message);
		} catch (NullPointerException e) {
			channels[channelID].setConsole(serverConsole.getConsoleArea());
			try {
				channels[channelID].addMessage(message);
			} catch (NullPointerException e2) {
				throw e2;
			}
		}
		if (channelID != CHANNEL_ALL) {
			try {
				channels[CHANNEL_ALL].addMessage(message);
			} catch (NullPointerException e) {
				channels[channelID].setConsole(serverConsole.getConsoleArea());
				try {
					channels[channelID].addMessage(message);
				} catch (NullPointerException e2) {
					return;
				}
			}
		}
		try {
			serverConsole.getConsoleArea()
					.setCaretPosition(
							serverConsole.getConsoleArea().getDocument()
									.getLength() - 1);
		} catch (IllegalArgumentException e) {
			server.getServerIO().writeException(e);
		}
	}

	public Console getConsole() {
		return serverConsole;
	}

	public void setActive(int channelID) {
		if (channelID >= channelIDReference) {
			server.getServerIO().printErr(
					"[" + this + "] No channel for ID: " + channelID);
			return;
		}
		channels[channelID].setConsole(serverConsole.getConsoleArea());
		for (int i = 0; i < channelIDReference; i++) {
			channels[i].setActive(false);
		}
		channels[channelID].setActive(true);
		channels[channelID].displayOnConsole();
		serverConsole.setTitle(channels[channelID].toString());
		serverConsole.setCurrentChannel(channels[channelID]);
	}

	public void setVisible(boolean state) {
		serverConsole.setVisible(state);
	}

	@Override
	public String toString() {
		return "ConsoleManager";
	}
}
