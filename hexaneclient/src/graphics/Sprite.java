package graphics;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import util.Bounds;
import util.InterfaceAlignment;
import client.Client;

public class Sprite {
	public final Client client;
	public int x, y, width, height;
	public int alignModX, alignModY;
	public InterfaceAlignment alignment = InterfaceAlignment.NONE;
	public BufferedImage image = null;
	public BufferedImage mouseoverImage = null;
	public boolean mouseover = false;
	public boolean loginScreen = false;
	public boolean visible = true;

	@SuppressWarnings("static-access")
	public Sprite(final Client client, int x, int y, int width, int height,
			int imageId) {
		this.client = client;
		this.image = ImageCache.getPermanentBufferedImage(imageId);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	@SuppressWarnings("static-access")
	public Sprite(final Client client, int x, int y, int width, int height,
			int imageId, InterfaceAlignment alignment) {
		this.client = client;
		this.image = ImageCache.getPermanentBufferedImage(imageId);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.alignment = alignment;
	}

	@SuppressWarnings("static-access")
	public Sprite(final Client client, int x, int y, int imageId) {
		this.client = client;
		this.image = ImageCache.getPermanentBufferedImage(imageId);
		this.x = x;
		this.y = y;
		this.width = ImageCache.getPermanentImage(imageId).getWidth();
		this.height = ImageCache.getPermanentImage(imageId).getHeight();
	}

	@SuppressWarnings("static-access")
	public Sprite(final Client client, int x, int y, int imageId,
			InterfaceAlignment alignment) {
		this.client = client;
		this.image = ImageCache.getPermanentBufferedImage(imageId);
		this.x = x;
		this.y = y;
		this.width = ImageCache.getPermanentImage(imageId).getWidth();
		this.height = ImageCache.getPermanentImage(imageId).getHeight();
		this.alignment = alignment;
	}

	public void paint(Graphics g) {
		Bounds b = getDimensions();
		if (loginScreen != client.user.loggedIn && visible)
			if (mouseover)
				g.drawImage(mouseoverImage, b.x, b.y, null);
			else
				g.drawImage(image, b.x, b.y, null);
	}

	public void paintGrid(Graphics g) {
		Bounds b = getDimensions();
		if (loginScreen != client.user.loggedIn)
			g.drawRect(b.x, b.y, b.width, b.height);
	}

	public boolean coordinateInBounds(int x, int y) {
		Bounds b = getDimensions();
		return ((b.x <= x && x <= b.x + b.width) && (b.y <= y && y <= b.y
				+ b.height));
	}

	private Bounds getDimensions() {
		switch (alignment) {
		case LEFT:
			return new Bounds(0, y, width, height);
		case TOP_LEFT:
			return new Bounds(0, 0, width, height);
		case TOP:
			return new Bounds(x, 0, width, height);
		case BOTTOM_LEFT:
			return new Bounds(0, (client.height - height) + y, width, height);
		case BOTTOM:
			return new Bounds(x, (client.height - height) + y, width, height);
		case BOTTOM_RIGHT:
			return new Bounds((client.width - width) + x,
					(client.height - height) + y, width, height);
		case RIGHT:
			return new Bounds((client.width - width) + x, y, width, height);
		case TOP_RIGHT:
			return new Bounds((client.width - width) + x, 0, width, height);
		case CENTER:
			return new Bounds(((client.width - width) / 2) + x,
					((client.height - height) / 2) + y, width, height);
		case NONE:
		default:
			return new Bounds(x, y, width, height);
		}
	}
}
