package graphics;

import java.awt.Image;
import java.io.Serializable;

public class GTile implements Sprite, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3145620159540632346L;
	public int x, y;
	public boolean onScreen = false;
	public boolean inUse = false;

	public int width() {
		return 32;
	}

	public int height() {
		return 32;
	}

	public GTile(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public boolean inUse() {
		return inUse;
	}

	@Override
	public void setUse(boolean state) {
		inUse = state;
	}

	@Override
	public Image getNextImage() {
		System.out.println("DAME");
		return null;
	}

	@Override
	public int getNextY(boolean b) {
		return 0;
	}

	@Override
	public int getNextX(boolean b) {
		return 0;
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}
}
