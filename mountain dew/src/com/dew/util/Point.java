package com.dew.util;

public class Point extends AbstractPoint {
	protected int x = 0, y = 0;

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Point setX(int x) {
		this.x = x;
		return this;
	}

	public Point setY(int x) {
		this.x = x;
		return this;
	}
}
