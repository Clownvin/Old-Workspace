package com.dew.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.Graphics;

import com.dew.io.Action;
import com.dew.io.Interactable;
import com.dew.io.MouseState;
import com.dew.io.Parentable;
import com.dew.util.BindingPoint;
import com.dew.util.ColorPalette;
import com.dew.util.Point;

public abstract class GUIComponent extends Component implements Interactable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6056785355408649602L;
	protected boolean visible = true;
	protected int x, y, width, height;
	protected MouseState mouseState = MouseState.NONE;
	protected Action action = new Action() {
		public void perform() {
			System.out.println("Action not added.");
		}
	};
	protected Alignment position = Alignment.CENTER;
	protected float fontSize = 32.0f;
	protected Color foreground = Color.BLACK;
	protected Color background = ColorPalette.GRAY_9.getColor();
	protected boolean hasFocus = false;
	protected int borderWidth = 12;
	protected int perferedWidth = 0, perferedHeight = 0;
	protected String text = "";
	protected Parentable parent;
	protected boolean hasParent = false;
	protected FontMetrics lastFontMetric = null;

	public GUIComponent(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.perferedWidth = width;
		this.perferedHeight = height;
	}

	public void setParent(Parentable parent) {
		this.parent = parent;
		if (parent != null)
			hasParent = true;
	}

	public Parentable getGUIParent() {
		return parent;
	}

	public boolean hasParent() {
		return hasParent;
	}

	public boolean getMouseDragAffected() {
		return false;
	}

	public GUIComponent setPerferedWidth(int perferedWidth) {
		this.perferedWidth = perferedWidth;
		return this;
	}

	protected void checkWidthAndHeight(FontMetrics metric) {
		setWidth(metric.stringWidth(getText()) + (borderWidth * 2));
		if (getWidth() < perferedWidth)
			setWidth(perferedWidth);
		height = metric.getHeight() + (borderWidth * 2);
		if (getHeight() < perferedHeight)
			height = perferedHeight;
	}

	protected void drawMouseEffects(Graphics graphics) {
		if (getMouseState() == MouseState.PRESSED)
			graphics.setColor(ColorPalette.darken(background, .30));
		else if (hasFocus)
			graphics.setColor(ColorPalette.darken(background, .10));
		else
			graphics.setColor(ColorPalette.darken(background, .025));
		graphics.fillRect(getX(), getY(), getWidth(), getHeight());
		if (getMouseState() == MouseState.MOUSEOVER
				|| getMouseState() == MouseState.PRESSED) {
			if (getMouseState() == MouseState.PRESSED)
				graphics.setColor(ColorPalette.darken(background, .50));
			else
				graphics.setColor(ColorPalette.darken(background, .20));
			for (int i = 0; i < borderWidth / 2; i++) {
				graphics.drawRect(getX() + i, getY() + i, getWidth() - 1
						- (i * 2), getHeight() - 1 - (i * 2));
			}
		}
	}

	public MouseState getMouseState() {
		return mouseState;
	}

	public GUIComponent setFontSize(float fontSize) {
		this.fontSize = fontSize;
		return this;
	}

	public float getFontSize(float fontSize) {
		return fontSize;
	}

	public GUIComponent setPosition(Alignment position) {
		this.position = position;
		return this;
	}

	public Alignment getPosition() {
		return position;
	}

	@Override
	public GUIComponent setMouseState(MouseState mouseState) {
		this.mouseState = mouseState;
		return this;
	}

	@Override
	public GUIComponent setReleased(boolean released) {
		if (released) {
			performAction();
		}
		return this;
	}

	public void draw(Graphics graphics) {
		if (!visible)
			return;
		graphics.setFont(graphics.getFont().deriveFont(fontSize));
		FontMetrics metric = graphics.getFontMetrics();
		lastFontMetric = metric;
		checkWidthAndHeight(metric);
		drawMouseEffects(graphics);
		graphics.setColor(foreground);
		graphics.drawString(
				getText(),
				getX() + ((getWidth() - metric.stringWidth(getText())) / 2),
				(int) (getY() + (metric.getAscent() + metric.getDescent()) + (borderWidth / 2)));
	}

	@Override
	public GUIComponent resetClickedMouseEffects() {
		return this;
	}

	public GUIComponent setBorderWidth(int borderWidth) {
		this.borderWidth = borderWidth;
		return this;
	}

	public int getBorderWidth() {
		return borderWidth;
	}

	@Override
	public boolean isVisible() {
		return hasParent ? parent.isVisible() && visible : visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public GUIComponent setX(int x) {
		this.x = x;
		return this;
	}

	@Override
	public int getX() {
		return x;
	}

	public GUIComponent setY(int y) {
		this.y = y;
		return this;
	}

	@Override
	public int getY() {
		return y;
	}

	public GUIComponent setWidth(int width) {
		this.width = width;
		return this;
	}

	@Override
	public int getWidth() {
		return width;
	}

	public GUIComponent setHeight(int height) {
		this.height = height;
		return this;
	}

	@Override
	public int getHeight() {
		return height;
	}

	public BindingPoint getBoundPoint(Alignment point) {
		switch (point) {
		case BOTTOM_LEFT:
			return new BindingPoint(this) {

				@Override
				public int getX() {
					return getComponent().getX();
				}

				@Override
				public int getY() {
					return getComponent().getHeight();
				}

			};
		case BOTTOM_RIGHT:
			return new BindingPoint(this) {

				@Override
				public int getX() {
					return getComponent().getWidth();
				}

				@Override
				public int getY() {
					return getComponent().getHeight();
				}

			};
		case TOP_LEFT:
			return new BindingPoint(this) {

				@Override
				public int getX() {
					return getComponent().getX();
				}

				@Override
				public int getY() {
					return getComponent().getY();
				}

			};
		case TOP_RIGHT:
			return new BindingPoint(this) {

				@Override
				public int getX() {
					return getComponent().getWidth();
				}

				@Override
				public int getY() {
					return getComponent().getY();
				}

			};
		case CENTER:
			return new BindingPoint(this) {

				@Override
				public int getX() {
					return getComponent().getWidth() / 2;
				}

				@Override
				public int getY() {
					return getComponent().getHeight() / 2;
				}

			};
		default:
			System.out.println("No case for alignment " + point
					+ " in \"getBoundPoint\"");
			return null;

		}
	}

	@Override
	public void performAction() {
		action.perform();
	}

	public Action getAction() {
		return action;
	}

	public GUIComponent setAction(Action action) {
		this.action = action;
		this.action.setComponent(this);
		return this;
	}

	@Override
	public boolean getFocus() {
		return hasFocus;
	}

	public String getText() {
		return text;
	}

	public GUIComponent setText(String text) {
		this.text = text;
		return this;
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
}
