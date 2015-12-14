package graphics;

import java.awt.Component;
import java.awt.Image;

public class BasicSprite extends Component implements Sprite {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public boolean changingSprite = false;
	private int cycle;
	private int cycles;
	public Image image;
	public Image[] images;
	public boolean inUse = false;
	public int nextSprite = 0;
	public boolean stagingSpriteX = false;
	public boolean stagingSpriteY = false;
	public boolean useCellX = false;
	public boolean useCellY = false;
	public Sprite usingXOf = null;
	public Sprite usingYOf = null;
	public int x = 0;
	private int xStage = 0;
	public int[] xStages;
	public int y = 0;
	private int yStage = 0;
	public boolean onScreen = false;

	public int[] yStages;

	public int width() {
		return getNextImage().getWidth(null);
	}

	public int height() {
		return getNextImage().getWidth(null);
	}

	public BasicSprite(Image imageForResource, int xPos, int yPos) {
		image = imageForResource;
		changingSprite = false;
		this.x = xPos;
		this.y = yPos;
	}

	public BasicSprite(Image[] imageForResources, int xPos, int yPos,
			double rate) {
		images = imageForResources;
		changingSprite = true;
		cycles = (int) ((int) imageForResources.length * rate);
		cycle = 0;
		this.x = xPos;
		this.y = yPos;
	}

	public Image getNextImage() {
		if (changingSprite) {
			Image toReturn = images[nextSprite];
			cycle++;
			if (cycle > (cycles)) {
				nextSprite++;
				if (nextSprite > images.length - 1) {
					nextSprite = 0;
				}
				cycle = 0;
			}
			return toReturn;
		}
		return image;
	}

	public int getNextX(boolean increase) {
		if (!stagingSpriteX)
			return 0;
		int toReturn = xStages[xStage];
		if (increase) {
			xStage++;
			if (xStage + 1 > xStages.length)
				xStage = 0;
		}
		return toReturn;
	}

	public int getNextY(boolean increase) {
		if (!stagingSpriteY)
			return 0;
		int toReturn = yStages[yStage];
		if (increase) {
			yStage++;
			if (yStage + 1 > yStages.length)
				yStage = 0;
		}
		return toReturn;
	}

	@Override
	public boolean inUse() {
		return this.inUse;
	}

	public void newImage(Image imageForResource, int xPos, int yPos) {
		image = imageForResource;
		changingSprite = false;
		this.x = xPos;
		this.y = yPos;
	}

	public void newImage(Image[] imageForResources, int xPos, int yPos,
			double rate) {
		images = imageForResources;
		changingSprite = true;
		cycles = (int) ((int) imageForResources.length * rate);
		cycle = 0;
		this.x = xPos;
		this.y = yPos;
	}

	public int retrieveX() {
		if (useCellX)
			return usingXOf.getX() + usingXOf.getNextX(false);
		return x;
	}

	public int retrieveY() {
		if (useCellY)
			return usingYOf.getY() + usingYOf.getNextY(false);
		return y;
	}

	@Override
	public void setUse(final boolean state) {
		this.inUse = state;
	}
}
