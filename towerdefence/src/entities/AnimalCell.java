package entities;

import java.awt.Image;

public class AnimalCell extends Cell implements Entity {
	public int defaultTillNextShot = 30;
	public int plantIndex;
	public int tillNextShot = 0;
	public int value;
	public boolean onScreen;

	public AnimalCell(Image imageForResource, int x, int y) {
		super(imageForResource, x, y);
	}

	public AnimalCell(Image[] imageForResources, int x, int y, double rate) {
		super(imageForResources, x, y, rate);
	}

	public void attack() {
		if (isAttacking())
			try {
				gh.pC[plantIndex].decreaseHealth(level * 2);
				tillNextShot = defaultTillNextShot;
			} catch (Exception e) {

			}
	}

	public boolean isAttacking() {
		if (tillNextShot <= 0) {
			try {
				plantIndex = gh.getIndexOfPlantCellInArea(x - level, y - level,
						x + x2 + level, y + y2 + level);
				if (plantIndex >= 0)
					if (!gh.pC[plantIndex].isDead())
						return true;
			} catch (NullPointerException e) {
				System.out.println("X = " + x + ", Y = " + y + ", level = "
						+ level);
				e.printStackTrace();
			}
		} else if (tillNextShot > 0) {
			tillNextShot--;
		}

		return false;
	}

	public void upgrade() {
		if (gh.game.money >= buyNextLevel()) {
			gh.game.money -= buyNextLevel();
			level += 1;
			if (level == 2) {
				s.newImage(
						gh.getImageForResource("Cache/Sprites/Orbs/AnimalCell2.png"),
						x, y);
			}
			if (tillNextShot > 1) {
				defaultTillNextShot -= 2;
			}
			System.out.println("Upgrading to level " + level);
		}
	}

}
