package world;

import graphics.Face;
import client.Client;

public class Tile {
	public final Client client;
	public int gridX, gridY;
	public int textureId;
	public static final int HEIGHT = 48;
	public static final int WIDTH = 48;
	public Face face;

	public Tile(final Client client, int x, int y, int textureId) {
		this.client = client;
		gridX = x;
		gridY = y;
		this.textureId = textureId;
		face = new Face(new int[] { x, x + WIDTH, x + WIDTH, x }, new int[] {
				y, y, y + HEIGHT, y + HEIGHT }, new int[] { 1, 1, 1, 1 });
	}
}
