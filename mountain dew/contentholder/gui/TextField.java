package com.dew.gui;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;

import com.dew.io.Interactable;
import com.dew.io.InteractableManager;

public class TextField extends GUIComponent implements Interactable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3361607614430768565L;
	protected int currentClippingWidth = 0;
	protected int destClippingWidth = 0;
	protected int maxClippingWidth = 0;
	protected String lastText = "";
	protected boolean insertPointBlink = false;
	protected Alignment textAlignment = Alignment.CENTER;
	protected final Thread handleClippingThread = new Thread(new Runnable() {

		@Override
		public void run() {
			int interval = 0;
			while (true) {
				interval++;
				interval %= 30;
				if (interval == 0)
					insertPointBlink = insertPointBlink ? false : true;
				if (currentClippingWidth != destClippingWidth) {
					setClippingWidth((int) (currentClippingWidth + ((destClippingWidth - currentClippingWidth) / 4)));
					insertPointBlink = true;
				}
				if (currentClippingWidth == destClippingWidth + 3) {
					setClippingWidth(destClippingWidth);
					insertPointBlink = true;
				}
				if (currentClippingWidth == destClippingWidth - 3) {
					setClippingWidth(destClippingWidth);
					insertPointBlink = true;
				}
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	});

	public TextField setClippingWidth(int width) {
		this.currentClippingWidth = width;
		if (this.currentClippingWidth > this.getWidth()) {
			this.currentClippingWidth = this.getWidth();
			this.destClippingWidth = this.getWidth();
			insertPointBlink = false;
		}
		if (this.currentClippingWidth < 0) {
			this.currentClippingWidth = 0;
			this.destClippingWidth = 0;
			insertPointBlink = false;
		}
		if (this.currentClippingWidth == this.destClippingWidth) {
			insertPointBlink = false;
		}
		return this;
	}

	public TextField(int x, int y, int perferedWidth, int perferedHeight,
			InteractableManager interactableManager) {
		super(x, y, perferedWidth, perferedHeight);
		interactableManager.addInteractable(this);
		handleClippingThread.start();
	}

	public TextField(int x, int y, InteractableManager interactableManager) {
		super(x, y, 0, 0);
		interactableManager.addInteractable(this);
		handleClippingThread.start();
	}

	protected volatile boolean firstFocus = true;
	protected volatile boolean editable = true;

	@Override
	public void performAction() {
		hasFocus = true;
		if (firstFocus) {
			firstFocus = false;
			text = "";
		}
	}

	public TextField setTextAlignment(Alignment alignment) {
		this.textAlignment = alignment;
		return this;
	}

	public Alignment getTextAlignment() {
		return textAlignment;
	}

	@Override
	public TextField setText(String text) {
		if (editable) {
			this.lastText = this.text;
			this.text = text;
		}
		if (lastFontMetric != null) {
			destClippingWidth = (int) lastFontMetric
					.getStringBounds(text, null).getWidth();
			maxClippingWidth = destClippingWidth;
		}
		return this;
	}

	@Override
	protected void checkWidthAndHeight(FontMetrics metric) {
		switch (textAlignment) {
		case LEFT:
			setWidth(getX() + currentClippingWidth + 3 + borderWidth
					+ borderWidth);
			break;
		case CENTER:
			setWidth(metric.stringWidth(getText()) + (borderWidth * 2));
			break;
		case RIGHT:
			setWidth(metric.stringWidth(getText()) + (borderWidth * 2));
			break;
		default:
			break;
		}
		if (getWidth() < perferedWidth)
			setWidth(perferedWidth);
		height = metric.getHeight() + (borderWidth * 2);
		if (getHeight() < perferedHeight)
			height = perferedHeight;
	}

	@Override
	public void draw(Graphics graphics) {
		if (!visible)
			return;

		graphics.setFont(graphics.getFont().deriveFont(fontSize));
		FontMetrics metric = graphics.getFontMetrics();
		lastFontMetric = metric;
		destClippingWidth = (int) lastFontMetric.getStringBounds(text, null)
				.getWidth();
		maxClippingWidth = destClippingWidth;
		checkWidthAndHeight(metric);
		drawMouseEffects(graphics);
		switch (textAlignment) {
		case CENTER:
			if (insertPointBlink && hasFocus) {
				graphics.setColor(Color.DARK_GRAY);
				graphics.drawLine(
						getX()
								+ ((getWidth() - metric.stringWidth(getText())) / 2)
								+ currentClippingWidth + 3,
						getY() + borderWidth,
						getX()
								+ ((getWidth() - metric.stringWidth(getText())) / 2)
								+ currentClippingWidth + 3,
						(getY() + getHeight()) - borderWidth);
				graphics.drawLine(
						getX()
								+ ((getWidth() - metric.stringWidth(getText())) / 2)
								+ currentClippingWidth + 4,
						getY() + borderWidth,
						getX()
								+ ((getWidth() - metric.stringWidth(getText())) / 2)
								+ currentClippingWidth + 4,
						(getY() + getHeight()) - borderWidth);
			}
			graphics.setColor(foreground);
			graphics.setClip(
					getX() + ((getWidth() - metric.stringWidth(getText())) / 2)
							- 30, getY(), currentClippingWidth + 30,
					getHeight());
			graphics.drawString(
					getText().length() >= lastText.length() ? getText()
							: lastText,
					getX() + ((getWidth() - currentClippingWidth) / 2),
					(int) (getY() + (metric.getAscent() + metric.getDescent()) + (borderWidth / 2)));
			graphics.setClip(0, 0, 4000, 4000);
			break;
		case RIGHT:
			if (insertPointBlink && hasFocus) {
				graphics.setColor(Color.DARK_GRAY);
				graphics.drawLine(getX() + getWidth() - (borderWidth), getY()
						+ borderWidth, getX() + getWidth() - (borderWidth),
						(getY() + getHeight()) - borderWidth);
				graphics.drawLine(getX() + getWidth() - (borderWidth) + 1,
						getY() + borderWidth, getX() + getWidth()
								- (borderWidth) + 1, (getY() + getHeight())
								- borderWidth);
			}
			graphics.setColor(foreground);
			graphics.setClip(getX() + getWidth() - borderWidth
					- currentClippingWidth, getY(), currentClippingWidth,
					getHeight());
			graphics.drawString(
					getText().length() >= lastText.length() ? getText()
							: lastText,
					getX() + getWidth() - borderWidth - currentClippingWidth,
					(int) (getY() + (metric.getAscent() + metric.getDescent()) + (borderWidth / 2)));
			graphics.setClip(0, 0, 4000, 4000);
			break;
		case LEFT:
			if (insertPointBlink && hasFocus) {
				graphics.setColor(Color.DARK_GRAY);
				graphics.drawLine(getX() + currentClippingWidth + 3
						+ borderWidth, getY() + borderWidth, getX()
						+ currentClippingWidth + 3 + borderWidth,
						(getY() + getHeight()) - borderWidth);
				graphics.drawLine(getX() + currentClippingWidth + 4
						+ borderWidth, getY() + borderWidth, getX()
						+ currentClippingWidth + 4 + borderWidth,
						(getY() + getHeight()) - borderWidth);
			}
			graphics.setColor(foreground);
			graphics.setClip(getX() + borderWidth, getY(),
					currentClippingWidth, getHeight());
			graphics.drawString(
					getText().length() >= lastText.length() ? getText()
							: lastText,
					getX() + borderWidth,
					(int) (getY() + (metric.getAscent() + metric.getDescent()) + (borderWidth / 2)));
			graphics.setClip(0, 0, 4000, 4000);
			break;
		}
	}

	public boolean getEditable() {
		return editable;
	}

	public TextField setEditable(boolean editable) {
		this.editable = editable;
		return this;
	}

	@Override
	public TextField resetClickedMouseEffects() {
		hasFocus = false;
		return this;
	}

	@Override
	public boolean getMouseDragAffected() {
		return false;
	}
}
