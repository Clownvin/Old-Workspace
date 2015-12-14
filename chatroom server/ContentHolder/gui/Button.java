package com.dew.gui;

import java.awt.Color;

import com.dew.io.Interactable;
import com.dew.io.InteractableManager;

public class Button extends GUIComponent implements Interactable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6296808199470681283L;
	protected int borderWidth = 12;

	public Button(int x, int y, int width, int height,
			InteractableManager interactableManager) {
		super(x, y, width, height);
		interactableManager.addInteractable(this);
		foreground = Color.WHITE;
		background = Color.GREEN;
	}

	public Button(int x, int y, String text,
			InteractableManager interactableManager) {
		super(x, y, 0, 0);
		this.text = text;
		interactableManager.addInteractable(this);
		foreground = Color.WHITE;
		background = Color.GREEN;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public Button setBorderWidth(int borderWidth) {
		this.borderWidth = borderWidth;
		return this;
	}

	public int getBorderWidth() {
		return borderWidth;
	}

	public void setForeground(Color foreground) {
		this.foreground = foreground;
	}

	public Color getForeground() {
		return foreground;
	}

	public void setBackground(Color background) {
		this.background = background;
	}

	public Color getBackground() {
		return background;
	}

	public Button setX(int x) {
		super.setX(x);
		return this;
	}

	public Button setY(int y) {
		super.setY(y);
		return this;
	}

	public Button setWidth(int width) {
		super.setWidth(width);
		return this;
	}

	public Button setHeight(int height) {
		super.setHeight(height);
		return this;
	}

	@Override
	public boolean getMouseDragAffected() {
		return false;
	}
}
