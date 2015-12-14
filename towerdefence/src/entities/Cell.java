package entities;

import graphics.BasicSprite;
import graphics.GraphicsHandler;

import java.awt.Image;

import util.BasicObject;
import world.Point;

public class Cell extends BasicObject implements Entity {

	public final int BUY_LEVEL = 30;
	public GraphicsHandler gh;
	public boolean inUse = false;
	public int level = 1;
	public world.Path path = null;
	public BasicSprite s;
	public boolean selected = false;
	public int speed = 1;
	public int x = 0;
	public int x2 = 32;
	public int y = 0;
	public int y2 = 32;

	public Cell(Image imageForResource, int xPos, int yPos) {
		s = new BasicSprite(imageForResource, xPos, yPos);
		x = xPos;
		y = yPos;
	}

	public Cell(Image[] imageForResources, int xPos, int yPos, double rate) {
		s = new BasicSprite(imageForResources, xPos, yPos, rate);
		x = xPos;
		y = yPos;
	}

	public void addGH(GraphicsHandler gH) {
		gh = gH;
	}

	public int buyNextLevel() {
		int toReturn = BUY_LEVEL;
		for (int i = 0; i < level; i++) {
			toReturn *= 2;
		}
		return toReturn;
	}

	public void followPath() {
		if (path != null) {
			if (this.x != path.getNextBoundsX())
				this.x += path.getNextX() * speed;
			if (this.y != path.getNextBoundsY())
				this.y += path.getNextY() * speed;
			if (this.x == path.getNextBoundsX()
					&& this.y == path.getNextBoundsY()) {
				int preStage = path.stage;
				path.nextStage();
				if (path.stage == preStage) {
					gh.game.life--;
					inUse = false;
					this.path = null;
				}
			}
		}
	}

	public Image getNextImage() {
		return s.getNextImage();
	}

	public int getNextX(boolean increase) {
		return s.getNextX(increase);
	}

	public int getNextY(boolean increase) {
		return s.getNextY(increase);
	}

	public int getX(boolean increase) {
		return x + s.getNextX(increase) - gh.game.frameX;
	}

	public int getY(boolean increase) {
		return y + s.getNextY(increase) - gh.game.frameY;
	}

	public boolean inUse() {
		return s.inUse;
	}

	public boolean isOnScreen() {
		return (getX(false) >= 0 && getY(false) >= 0 && getX(false) <= 1024 && getY(false) <= 700)
				|| (getX(false) + s.width() >= 0
						&& getY(false) + s.height() >= 0
						&& getX(false) + s.width() <= 1024 && getY(false)
						+ s.height() <= 700);
	}

	public Point middle() {
		return new Point((x + s.width() / 2) - gh.game.frameX,
				(y + s.height() / 2) - gh.game.frameY);
	}

}
