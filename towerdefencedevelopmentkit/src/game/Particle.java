package game;

import game2d.Entity;
import game2d.Event;

public class Particle extends Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2410395930282480858L;

	public Particle(int ID, String NAME, Entity toFollow) {
		super(ID, NAME);
		this.toFollow = toFollow;

		if (this.toFollow == null) {
			System.err
					.println("Error: toFollow is null. This could cause errors later on.");
		}
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	protected Entity toFollow;
	protected int speed;
	protected boolean usedUp = false;

	public void follow(Entity toFollow, int speed) {
		this.toFollow = toFollow;
		this.speed = speed;
	}

	@Override
	public void event(final Event EVENT) {
		if (toFollow != null) {
			double slopeY = toFollow.getY() + (toFollow.getHeight() / 2) - y
					+ (this.getHeight() / 2);
			double slopeX = toFollow.getX() + (toFollow.getWidth() / 2) - x
					+ (this.getWidth() / 2);
			if (absValue(slopeY) > absValue(slopeX)) {
				slopeY /= absValue(slopeY);
				slopeX /= absValue(slopeY);
			} else {
				slopeY /= absValue(slopeX);
				slopeX /= absValue(slopeX);
			}
			slopeX *= speed;
			slopeY *= speed;
			// System.out.println(slopeX + ", " + slopeY);
			if (distanceBetween(this.x + (this.getWidth() / 2) + slopeX, this.y
					+ (this.getHeight() / 2) + slopeY) > distanceBetween(
					toFollow.getX(), toFollow.getY())) {
				System.out.println("Done");
				this.x = toFollow.getX();
				this.y = toFollow.getY();
				System.out.println(x + ", " + y);
				this.usedUp = true;
				return;
			}
			this.x += slopeX;
			this.y += slopeY;
		}
	}

	protected double distanceBetween(double x, double y) {
		return Math.sqrt(((x - this.x) * (x - this.x))
				+ ((y - this.y) * (y - this.y)));
	}

	protected double absValue(double value) {
		if (value < 0) {
			return value * -1;
		}
		return value;
	}
}
