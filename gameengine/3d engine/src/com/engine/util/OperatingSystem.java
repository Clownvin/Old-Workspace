package com.engine.util;

import com.engine.util.OperatingSystem;

/**
 * 
 * @author Calvin Gene Hall
 *
 */

public enum OperatingSystem {
	LINUX("Linux", -1, -30, 14), WINDOWS8_1("Windows 8.1", -8, -31, 14), UNKNOWN(
			"Unknown", 0, 0, 0);

	public static OperatingSystem getOperatingSystem() {
		switch (System.getProperty("os.name")) {
		case "Linux":
			return LINUX;
		case "Windows 8.1":
			return WINDOWS8_1;
		default:
			System.out.println("Unknown OS Detected. "
					+ System.getProperty("os.name"));
			return UNKNOWN;
		}
	}

	private final String name;

	private final int xDiff, yDiff, endChar;

	private OperatingSystem(final String name, final int xDiff,
			final int yDiff, final int endChar) {
		this.name = name;
		this.xDiff = xDiff;
		this.yDiff = yDiff;
		this.endChar = endChar;
	}

	public int getEndChar() {
		return endChar;
	}

	public int getXDiff() {
		return xDiff;
	}

	public int getYDiff() {
		return yDiff;
	}

	@Override
	public String toString() {
		return name;
	}
}
