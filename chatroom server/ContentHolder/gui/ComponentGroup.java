package com.dew.gui;

import java.awt.Graphics;

public final class ComponentGroup extends GUIComponent {
	/**
	 * 
	 */
	private static final long serialVersionUID = -497706004479712590L;
	private GUIComponent component1, component2;
	private Alignment order;
	private int middleX = 0, middleY = 0, spacerWidth = 0;

	public ComponentGroup(int spacerWidth, GUIComponent component1,
			GUIComponent component2, Alignment order, Alignment position) {
		super(0, 0, 0, 0);
		this.spacerWidth = spacerWidth;
		this.component1 = component1;
		this.component1.setParent(this);
		this.component2 = component2;
		this.component2.setParent(this);
		this.order = order;
		this.position = position;
	}

	@Override
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public ComponentGroup setOrder(Alignment order) {
		this.order = order;
		return this;
	}

	public int getTotalHeight() {
		if (order == Alignment.VERTICAL)
			return component1.getHeight() + component2.getHeight()
					+ spacerWidth;
		else if (component1.getHeight() > component2.getHeight())
			return component1.getHeight();
		else
			return component2.getHeight();
	}

	public int getTotalWidth() {
		if (order == Alignment.HORIZONTAL)
			return component1.getWidth() + component2.getWidth() + spacerWidth;
		else if (component1.getWidth() > component2.getWidth())
			return component1.getWidth();
		else
			return component2.getWidth();
	}

	@Override
	public int getWidth() {
		return getTotalWidth();
	}

	@Override
	public int getHeight() {
		return getTotalHeight();
	}

	@Override
	public void draw(Graphics g) {
		middleX = getX() + (getTotalWidth() / 2);
		middleY = getY() + (getTotalHeight() / 2);
		switch (order) {
		case VERTICAL:
			int totalHeight = getTotalHeight();
			component1.setY(middleY - (totalHeight / 2));
			component2
					.setY(middleY
							- ((totalHeight / 2) - component1.getHeight() - spacerWidth));
			component1.setX(middleX - (component1.getWidth() / 2));
			component2.setX(middleX - (component2.getWidth() / 2));
			component1.draw(g);
			component2.draw(g);
			break;
		case HORIZONTAL:
			int totalWidth = getTotalWidth();
			component1.setX(middleX - (totalWidth / 2));
			component2.setX(middleX
					- ((totalWidth / 2) - component1.getWidth() - spacerWidth));
			component1.setY(middleY - (component1.getHeight() / 2));
			component2.setY(middleY - (component2.getHeight() / 2));
			component1.draw(g);
			component2.draw(g);
			break;
		default:
			System.out.println(order + " is not a valid order.");
			break;
		}
	}
}
