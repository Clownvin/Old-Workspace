package graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

public class PaintableObject {
	private final boolean IS_IMAGE;
	private final Image IMAGE;
	private final Shape SHAPE;
	private final Color COLOR;
	private final String MESSAGE;
	private final int PAINT_STYLE;
	public final int FILL = 0, DRAW = 1;
	private final int WIDTH, HEIGHT;

	public PaintableObject(Image image) {
		this.IMAGE = image;
		this.IS_IMAGE = true;
		this.SHAPE = null;
		this.COLOR = null;
		this.MESSAGE = null;
		this.PAINT_STYLE = 0;
		this.WIDTH = image.getWidth(null);
		this.HEIGHT = image.getHeight(null);
	}

	public PaintableObject(Image image, int width, int height) {
		this.IMAGE = image;
		this.IS_IMAGE = true;
		this.SHAPE = null;
		this.COLOR = null;
		this.MESSAGE = null;
		this.PAINT_STYLE = 0;
		this.WIDTH = width;
		this.HEIGHT = height;
	}

	public PaintableObject(String s) {
		this.IMAGE = null;
		this.IS_IMAGE = false;
		this.SHAPE = null;
		this.COLOR = Color.WHITE;
		this.MESSAGE = s;
		this.PAINT_STYLE = 0;
		this.WIDTH = 0;
		this.HEIGHT = 0;
	}

	public PaintableObject(String s, Color color) {
		this.IMAGE = null;
		this.IS_IMAGE = false;
		this.SHAPE = null;
		this.COLOR = color;
		this.MESSAGE = s;
		this.PAINT_STYLE = 0;
		this.WIDTH = 0;
		this.HEIGHT = 0;
	}

	public PaintableObject(Shape s, int width, int height) {
		this.IMAGE = null;
		this.IS_IMAGE = false;
		this.SHAPE = s;
		this.COLOR = Color.WHITE;
		this.MESSAGE = null;
		this.PAINT_STYLE = 0;
		this.WIDTH = width;
		this.HEIGHT = height;
	}

	public PaintableObject(Shape s, int width, int height, Color color) {
		this.IMAGE = null;
		this.IS_IMAGE = false;
		this.SHAPE = s;
		this.COLOR = color;
		this.MESSAGE = null;
		this.PAINT_STYLE = 0;
		this.WIDTH = width;
		this.HEIGHT = height;
	}

	public PaintableObject(Shape s, int width, int height, int paintStyle) {
		this.IMAGE = null;
		this.IS_IMAGE = false;
		this.SHAPE = s;
		this.COLOR = Color.WHITE;
		this.MESSAGE = null;
		this.PAINT_STYLE = paintStyle;
		this.WIDTH = width;
		this.HEIGHT = height;
	}

	public PaintableObject(Shape s, int width, int height, Color color,
			int paintStyle) {
		this.IMAGE = null;
		this.IS_IMAGE = false;
		this.SHAPE = s;
		this.COLOR = color;
		this.MESSAGE = null;
		this.PAINT_STYLE = paintStyle;
		this.WIDTH = width;
		this.HEIGHT = height;
	}

	public void paint(Graphics g, int x, int y) {
		if (IS_IMAGE) {
			g.drawImage(IMAGE, x, y, null);
		} else {
			g.setColor(COLOR);
			switch (SHAPE) {
			case RECTANGLE:
				switch (PAINT_STYLE) {
				case 0:
					g.fillRect(x, y, WIDTH, HEIGHT);
					break;
				case 1:
					g.drawRect(x, y, WIDTH, HEIGHT);
					break;
				}
				break;
			}
			if (MESSAGE != null) {
				g.drawString(MESSAGE, x, y);
			}
		}
	}
}
