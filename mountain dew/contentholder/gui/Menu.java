package com.dew.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import com.dew.io.InteractableManager;

public class Menu extends GUIComponent implements MultiItem {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6075872172009330356L;
	protected ArrayList<GUIComponent> menuItems = new ArrayList<GUIComponent>();
	protected Alignment order = Alignment.VERTICAL;
	protected ScrollBar verticalScrollBar;

	public Menu(int x, int y, int width, int height, Alignment order,
			InteractableManager interactableManager) {
		super(x, y, width, height);
		this.order = order;
		verticalScrollBar = new ScrollBar(0, 0, 40, 40, this,
				Alignment.VERTICAL, interactableManager);
	}

	public Menu(int x, int y, int width, int height,
			InteractableManager interactableManager) {
		super(x, y, width, height);
		verticalScrollBar = new ScrollBar(0, 0, 40, 40, this,
				Alignment.VERTICAL, interactableManager);
	}

	public Menu addMenuItem(GUIComponent menuItem) {
		menuItems.add(menuItem);
		return this;
	}

	public ArrayList<GUIComponent> getMenuItems() {
		return menuItems;
	}

	public int getTotalHeight() {
		int totalHeight = 0;
		if (order == Alignment.VERTICAL)
			for (GUIComponent menuItem : menuItems)
				totalHeight += menuItem.getHeight();
		else
			for (GUIComponent menuItem : menuItems)
				if (menuItem.getHeight() > totalHeight)
					totalHeight = menuItem.getHeight();
		return totalHeight;
	}

	public int getTotalWidth() {
		int totalWidth = 0;
		if (verticalScrollBar.isVisible())
			totalWidth += verticalScrollBar.getWidth();
		if (order == Alignment.HORIZONTAL)
			for (GUIComponent menuItem : menuItems)
				totalWidth += menuItem.getWidth();
		else
			for (GUIComponent menuItem : menuItems)
				if (menuItem.getWidth() > totalWidth)
					totalWidth = menuItem.getWidth();
		return totalWidth;
	}

	@Override
	public int getWidth() {
		return perferedWidth;
	}

	@Override
	public int getHeight() {
		return perferedHeight;
	}

	@Override
	public void draw(Graphics graphics) {
		graphics.setClip(getX(), getY(), getWidth(), getHeight());
		graphics.setColor(Color.black);
		graphics.drawRect(0, 0, 10000, 1000);
		int middleX = getX() + (getTotalWidth() / 2);
		int middleY = getY() + (getTotalHeight() / 2);
		int sizeSoFar = 0;
		switch (order) {
		case VERTICAL:
			int totalHeight = getTotalHeight();
			for (int i = 0; i < menuItems.size(); i++) {
				menuItems.get(i).setY(
						middleY - (totalHeight / 2) + sizeSoFar
								- verticalScrollBar.getTickOffset());
				menuItems.get(i).setX(
						middleX - (menuItems.get(i).getWidth() / 2));
				sizeSoFar += menuItems.get(i).getHeight();
				menuItems.get(i).draw(graphics);
			}
			break;
		case HORIZONTAL:
			int totalWidth = getTotalWidth();
			for (int i = 0; i < menuItems.size(); i++) {
				menuItems.get(i).setX(middleX - (totalWidth / 2) + sizeSoFar);
				menuItems.get(i).setY(
						middleY - (menuItems.get(i).getHeight() / 2)
								- verticalScrollBar.getTickOffset());
				sizeSoFar += menuItems.get(i).getWidth();
				menuItems.get(i).draw(graphics);
			}
			break;
		default:
			System.out.println(order + " is not a valid order.");
			break;
		}
		verticalScrollBar.draw(graphics);
		graphics.setClip(0, 0, 4000, 4000);
	}

	@Override
	public boolean getMouseDragAffected() {
		return false;
	}
}
