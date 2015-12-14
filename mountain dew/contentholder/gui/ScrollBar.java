package com.dew.gui;

import java.awt.Graphics;

import com.dew.io.InteractableManager;
import com.dew.io.MouseDragAffected;
import com.dew.io.MouseState;
import com.dew.util.ColorPalette;

public class ScrollBar extends GUIComponent implements MouseDragAffected {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1228681292539489828L;
	protected GUIComponent component;
	protected double baseOffset = 0.0D;
	protected int tick = 0;
	protected int destTick = 0;
	protected int maxTick = 0;
	protected int scrollerSize = 0;
	protected boolean mouseDragAffected = true;
	protected final Thread dragHandleThread = new Thread(new Runnable() {

		@Override
		public void run() {
			while (true) {
				if (tick != destTick) {
					setTick((int) (tick + ((destTick - tick) / 4.5)));
				}
				if (tick == destTick + 1) {
					setTick(destTick);
				}
				if (tick == destTick - 1) {
					setTick(destTick);
				}
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	});

	public ScrollBar(int x, int y, int width, int height,
			GUIComponent component, Alignment position,
			InteractableManager interactableManager) {
		super(x, y, width, height);
		this.component = component;
		this.position = position;
		interactableManager.addInteractable(this);
		dragHandleThread.start();
	}

	public ScrollBar setMouseDragAffected(boolean mouseDragAffected) {
		this.mouseDragAffected = mouseDragAffected;
		return this;
	}

	public boolean getMouseDragAffected() {
		return mouseDragAffected;
	}

	public void handleMouseDrag(int mx, int my, int lastX, int lastY) {
		int diff = 0;
		switch (position) {
		case VERTICAL:
			diff = (int) ((my - lastY));
			destTick += diff;
			break;
		case HORIZONTAL:
			diff = (int) ((mx - lastX));
			destTick += diff;
			break;
		default:
			System.out.println("Invalid position");
			return;
		}
	}

	public ScrollBar setTick(int tick) {
		this.tick = tick;
		if (this.tick > maxTick) {
			this.tick = maxTick;
			this.destTick = maxTick;
		}
		if (this.tick < 0) {
			this.tick = 0;
			this.destTick = 0;
		}
		return this;
	}

	public int getTick() {
		return tick;
	}

	public int getTickOffset() {
		return (int) (tick * baseOffset);
	}

	@Override
	public int getY() {
		if (position == Alignment.HORIZONTAL)
			return (component.getY() + component.getHeight()) - getHeight();
		else
			return (int) (component.getY() + (1 * tick));
	}

	@Override
	public int getX() {
		if (position == Alignment.VERTICAL)
			return (component.getX() + component.getWidth()) - getWidth();
		else
			return (int) (component.getX() + (1 * tick));
	}

	@Override
	public int getWidth() {
		if (position == Alignment.HORIZONTAL)
			return scrollerSize;
		else
			return width;
	}

	@Override
	public int getHeight() {
		if (position == Alignment.HORIZONTAL)
			return height;
		else
			return scrollerSize;
	}

	@Override
	public void draw(Graphics graphics) {
		switch (position) {
		case HORIZONTAL:
			baseOffset = (double) ((MultiItem) component).getTotalWidth()
					/ (double) component.getWidth();
			scrollerSize = (int) (component.getWidth() / baseOffset);
			if (scrollerSize >= component.getWidth()) {
				setVisible(false);
				return;
			}
			setVisible(true);
			maxTick = component.getWidth() - scrollerSize;
			graphics.setColor(ColorPalette.darken(background, .05));
			graphics.fillRect(component.getX(), getY(), component.getWidth(),
					component.getHeight());
			if (getMouseState() == MouseState.PRESSED)
				graphics.setColor(ColorPalette.darken(background, .3));
			else if (hasFocus)
				graphics.setColor(ColorPalette.darken(background, .2));
			else
				graphics.setColor(ColorPalette.darken(background, .1));
			graphics.fillRect(getX(), getY(), scrollerSize,
					component.getHeight());
			if (getMouseState() == MouseState.MOUSEOVER
					|| getMouseState() == MouseState.PRESSED) {
				if (getMouseState() == MouseState.PRESSED)
					graphics.setColor(ColorPalette.darken(background, .60));
				else
					graphics.setColor(ColorPalette.darken(background, .40));
				for (int i = 0; i < borderWidth / 2; i++) {
					graphics.drawRect(getX() + i, getY() + i, getWidth() - 1
							- (i * 2), getHeight() - 1 - (i * 2));
				}
			}
			break;
		case VERTICAL:
			baseOffset = (double) ((MultiItem) component).getTotalHeight()
					/ (double) component.getHeight();
			scrollerSize = (int) (component.getHeight() / baseOffset);
			if (scrollerSize >= component.getHeight()) {
				setVisible(false);
				return;
			}
			setVisible(true);
			maxTick = component.getHeight() - scrollerSize;
			graphics.setColor(ColorPalette.darken(background, .05));
			graphics.fillRect(getX(), component.getY(), component.getWidth(),
					component.getHeight());
			if (getMouseState() == MouseState.PRESSED)
				graphics.setColor(ColorPalette.darken(background, .3));
			else if (hasFocus)
				graphics.setColor(ColorPalette.darken(background, .2));
			else
				graphics.setColor(ColorPalette.darken(background, .1));
			graphics.fillRect(getX(), getY(), component.getWidth(),
					scrollerSize);
			if (getMouseState() == MouseState.MOUSEOVER
					|| getMouseState() == MouseState.PRESSED) {
				if (getMouseState() == MouseState.PRESSED)
					graphics.setColor(ColorPalette.darken(background, .60));
				else
					graphics.setColor(ColorPalette.darken(background, .40));
				for (int i = 0; i < borderWidth / 2; i++) {
					graphics.drawRect(getX() + i, getY() + i, getWidth() - 1
							- (i * 2), getHeight() - 1 - (i * 2));
				}
			}
			break;
		default:
			System.out.println("Invalid position");
			break;
		}
	}

	@Override
	public boolean canReleaseFocus() {
		return false;
	}
}
