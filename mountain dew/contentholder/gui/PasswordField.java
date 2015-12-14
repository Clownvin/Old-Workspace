package com.dew.gui;

import java.awt.FontMetrics;
import java.awt.Graphics;

import com.dew.io.InteractableManager;

public class PasswordField extends TextField {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1098826156341356433L;

	public PasswordField(int x, int y, int width, int height,
			InteractableManager interactableManager) {
		super(x, y, width, height, interactableManager);
	}

	@Override
	public void draw(Graphics graphics) {
		if (!isVisible())
			return;
		graphics.setFont(graphics.getFont().deriveFont(fontSize));
		String passwordText = "";
		for (int i = 0; i < text.length(); i++)
			passwordText += "*";
		FontMetrics metric = graphics.getFontMetrics();
		checkWidthAndHeight(metric);
		drawMouseEffects(graphics);
		graphics.setColor(foreground);
		if (firstFocus)
			graphics.drawString(
					text,
					getX() + ((getWidth() - metric.stringWidth(text)) / 2),
					(int) (getY()
							+ ((metric.getAscent() + metric.getDescent())) + (borderWidth / 2)));
		else
			graphics.drawString(
					passwordText,
					getX()
							+ ((getWidth() - metric.stringWidth(passwordText)) / 2),
					(int) (getY()
							+ ((metric.getAscent() + metric.getDescent()) * 1.2) + (borderWidth / 2)));
	}

}
