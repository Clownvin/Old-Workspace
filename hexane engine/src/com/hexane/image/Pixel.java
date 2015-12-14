package com.hexane.image;

import java.awt.Color;

public final class Pixel {
	public final int x, y;
	private int color;

	public Pixel(final int x, final int y) {
		this.x = x;
		this.y = y;
	}

	public int getAlpha() {
		return (color >> 24) & 0xff;
	}

	public int getRed() {
		return (color >> 16) & 0xff;
	}

	public int getGreen() {
		return (color >> 8) & 0xff;
	}

	public int getBlue() {
		return color & 0xff;
	}

	public Color getColor() {
		return new Color(getRed(), getGreen(), getBlue());
	}

	public void setColor(Color color) {
		int red = 
	}
}
