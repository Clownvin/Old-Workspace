package com.dew.gui;

public enum Alignment {
	TOP_LEFT("TOP_LEFT"), TOP_RIGHT("TOP_RIGHT"), BOTTOM_LEFT("BOTTOM_LEFT"), BOTTOM_RIGHT(
			"BOTTOM_RIGHT"), CENTER("CENTER"), LEADING("LEADING"), FOLLOWING(
			"FOLLOWING"), HORIZONTAL("HORIZONTAL"), VERTICAL("VERTICAL"), NONE(
			"NONE"), RIGHT("RIGHT"), LEFT("LEFT"), TOP("TOP"), BOTTOM("BOTTOM");

	private final String text;

	private Alignment(final String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return text;
	}
}
