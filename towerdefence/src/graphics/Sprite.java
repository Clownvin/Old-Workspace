package graphics;

import java.awt.Image;

import util.GameObject;

public interface Sprite extends GameObject {
	public Image getNextImage();

	public int getNextY(boolean b);

	public int getNextX(boolean b);

	public int getX();

	public int getY();
}
