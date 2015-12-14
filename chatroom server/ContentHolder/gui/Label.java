package com.dew.gui;

import java.awt.FontMetrics;
import java.awt.Graphics;

public class Label extends GUIComponent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4431889918335220247L;

	public Label(int x, int y) {
		super(x, y, 0, 0);
	}

	public Label(int x, int y, String text) {
		super(x, y, 0, 0);
		this.text = text;
	}

	@Override
	public void draw(Graphics graphics) {
		if (!visible)
			return;
		graphics.setFont(graphics.getFont().deriveFont(fontSize));
		FontMetrics metric = graphics.getFontMetrics();
		checkWidthAndHeight(metric);
		graphics.setColor(foreground);
		graphics.drawString(text,
				getX() + ((getWidth() - metric.stringWidth(text)) / 2),
				(int) (getY() + ((metric.getAscent() + metric.getDescent()))));
	}

	@Override
	public boolean getMouseDragAffected() {
		return false;
	}
}
