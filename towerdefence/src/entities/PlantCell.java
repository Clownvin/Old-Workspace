package entities;

import java.awt.Image;

public class PlantCell extends Cell {
	public int health = 100;

	public PlantCell(Image[] imagesForResources, int x, int y, double d) {
		super(imagesForResources, x, y, d);
	}

	public PlantCell(Image imageForResource, int x, int y) {
		super(imageForResource, x, y);
	}

	public void decreaseHealth(int amount) {
		this.health -= amount;
		// System.out.println("Getting hurt");
		if (isDead()) {
			this.setUse(false);
			this.x = 0;
			this.y = 0;
			gh.game.score += gh.game.roundScore;
			gh.game.money += gh.game.roundMoney;
		}

	}

	public void followPath() {
		if (path != null) {
			if (this.x != path.getNextBoundsX())
				this.x += path.getNextX() * speed;
			if (this.y != path.getNextBoundsY())
				this.y += path.getNextY() * speed;
			if (this.x == path.getNextBoundsX()
					&& this.y == path.getNextBoundsY()) {
				// System.out.println("Getting next stage");
				int preStage = path.stage;
				path.nextStage();
				if (path.stage == preStage) {
					gh.game.life--;
					this.health = -1;
					this.setUse(false);
					this.path = null;
				}
			}
		}
	}

	public boolean isDead() {
		return health <= 0;
	}

}
