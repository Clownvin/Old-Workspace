package com.dew.io;

import javax.swing.JTextArea;

/**
 * 
 * @author Calvin Gene Hall
 *
 */

public final class ConsoleChannel {

	private final String channelName;
	private final int maxSize;
	private String[] strings;
	private int pointer = 0;
	private boolean active = false;
	private boolean maxReached = false;
	private JTextArea console;

	// TODO Document
	/**
	 * 
	 * @param channelName
	 * @param maxSize
	 * @param channelID
	 */
	public ConsoleChannel(final String channelName, final int maxSize,
			final int channelID) {
		this.channelName = channelName;
		this.maxSize = maxSize;
		strings = new String[maxSize];
	}

	// TODO Document
	/**
	 * 
	 * @param message
	 */
	public void addMessage(String message) {
		if (pointer == maxSize) {
			maxReached = true;
			pointer = 0;
		}
		strings[pointer] = message;
		pointer++;
		if (active) {
			try {
				displayOnConsole();
			} catch (NullPointerException e) {
				throw e;
			}
		}
	}

	// TODO Document
	/**
	 * 
	 */
	public void displayOnConsole() {
		try {
			synchronized (console) {
				console.setText("");
				if (maxReached) {
					for (int i = pointer + 1; i < maxSize; i++) {
						console.append(strings[i]);
					}
				}
				for (int i = 0; i < pointer; i++) {
					console.append(strings[i]);
				}
			}
		} catch (NullPointerException e) {
			throw e;
		}
	}

	// TODO Document
	/**
	 * 
	 * @return
	 */
	public JTextArea getConsole() {
		return console;
	}

	// TODO Document
	/**
	 * 
	 * @param active
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	// TODO Document
	/**
	 * 
	 * @param console
	 */
	public void setConsole(JTextArea console) {
		this.console = console;
	}

	// TODO Document
	/**
	 * 
	 * @return
	 */
	@Override
	public String toString() {
		return channelName;
	}

}
