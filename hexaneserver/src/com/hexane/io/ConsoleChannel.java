package com.hexane.io;

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

	public ConsoleChannel(final String channelName, final int maxSize,
			final int channelID) {
		this.channelName = channelName;
		this.maxSize = maxSize;
		strings = new String[maxSize];
		for (@SuppressWarnings("unused")
		String s : strings) {
			s = "";
		}
	}

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

	public void displayOnConsole() {
		try {
			console.setText("");
			if (maxReached) {
				for (int i = pointer + 1; i < maxSize; i++) {
					console.append(strings[i]);
				}
			}
			for (int i = 0; i < pointer; i++) {
				console.append(strings[i]);
			}
		} catch (NullPointerException e) {
			throw e;
		}
	}

	public JTextArea getConsole() {
		return console;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public void setConsole(JTextArea console) {
		this.console = console;
	}

	@Override
	public String toString() {
		return channelName;
	}

}
