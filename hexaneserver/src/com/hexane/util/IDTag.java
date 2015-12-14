package com.hexane.util;

public final class IDTag {
	public final int id;

	public IDTag(final int id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return ""+id;
	}
}
