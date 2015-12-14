package com.hexane.commerce;

import items.ItemInterface;

public class Offer {
	protected final ItemInterface item;
	protected final int price;
	
	public Offer(final ItemInterface item, final int price) {
		this.item = item;
		this.price = price;
	}
}
