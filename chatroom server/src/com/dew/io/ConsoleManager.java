package com.dew.io;

import com.dew.server.Server;

/**
 * 
 * @author Calvin Gene Hall
 *
 */

public final class ConsoleManager {
	// TODO Document
	/**
	 * 
	 * @param name
	 * @param maxSize
	 */
	public static void addChannel(String name, int maxSize) {
		if (channelIDReference + 1 >= 200) {
			ServerIO.printErr("[ConsoleManager] Cannot create new channel. Too many exist already!");
			return;
		}
		channels[channelIDReference] = new ConsoleChannel(name, maxSize,
				channelIDReference);
		channelIDReference++;
	}

	// TODO Document
	/**
	 * 
	 * @param message
	 */
	public static void addConsoleMessage(String message) {
		if (Server.getShowGUI()) {
			if (serverConsole == null)
				serverConsole = new Console();
			addMessage(message + "\n", ConsoleManager.CHANNEL_ALL);
		}
	}

	// TODO Document
	/**
	 * 
	 * @param message
	 * @param channel
	 */
	public static void addConsoleMessage(String message, int channel) {
		if (Server.getShowGUI()) {
			if (serverConsole == null)
				serverConsole = new Console();
			addMessage(message + "\n", channel);
		}
	}

	// TODO Document
	/**
	 * 
	 * @param message
	 * @param channelID
	 */
	private static void addMessage(String message, int channelID) {
		if (channelID >= channelIDReference) {
			ServerIO.printErr("[ConsoleManager] No channel for ID: "
					+ channelID);
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
			ServerIO.writeException(e);
		}
	}

	// TODO Document
	/**
	 * 
	 * @return
	 */
	public static Console getConsole() {
		return serverConsole;
	}

	// TODO Document
	/**
	 * 
	 * @return
	 */
	public static ConsoleManager getSingleton() {
		return SINGLETON;
	}

	// TODO Document
	/**
	 * 
	 * @param channelID
	 */
	public static void setActive(int channelID) {
		if (channelID >= channelIDReference) {
			ServerIO.printErr("[ConsoleManager] No channel for ID: "
					+ channelID);
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

	// TODO Document
	/**
	 * 
	 * @param state
	 */
	public static void setVisible(boolean state) {
		serverConsole.setVisible(state);
	}

	private static ConsoleChannel[] channels = new ConsoleChannel[200];

	private static int channelIDReference = 0;

	private static Console serverConsole;

	public static int CHANNEL_ALL = 0;

	public static int CHANNEL_SERVER = 1;

	public static int CHANNEL_GLOBAL_CHAT = 2;

	private static final ConsoleManager SINGLETON = new ConsoleManager();

	static {
		serverConsole = new Console();
		addChannel("All", 500);
		addChannel("Server", 500);
		addChannel("Global Chat", 400);
		setActive(ConsoleManager.CHANNEL_ALL);
	}

	// TODO Document
	/**
	 * 
	 */
	private ConsoleManager() {
		// To prevent instantiation.
	}
}
