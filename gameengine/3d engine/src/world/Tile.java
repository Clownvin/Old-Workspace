package world;

import java.awt.Color;

import modeler.Face;
import client.Client;

public class Tile {
	public int gridX, gridY;
	public int textureId;
	public static final int HEIGHT = 48;
	public static final int WIDTH = 48;
	public Face face;

	public Tile(int x, int y, int textureId) {
		gridX = x;
		gridY = y;
		this.textureId = textureId;
		face = new Face(new float[] { x, x + WIDTH, x + WIDTH, x },
				new float[] { y, y, y + HEIGHT, y + HEIGHT }, new float[] { 1,
						1, 1, 1 }, Color.GRAY);
	}
}
