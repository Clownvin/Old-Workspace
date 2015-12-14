package world;

import graphics.ImageCache;

import java.awt.image.BufferedImage;

import client.Client;

public class Texture {
	public final Client client;
	public int imageId = 14;
	public BufferedImage image;

	@SuppressWarnings("static-access")
	public Texture(final Client client) {
		this.client = client;
		image = ImageCache.getBufferedImage(imageId);
	}
}
