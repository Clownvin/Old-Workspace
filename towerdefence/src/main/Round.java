package main;

import world.Path;

public class Round {
	public Game game;
	public int originalTillNext;
	public Path p;
	public int roundHealth = 0;
	public boolean roundOver = false;
	public int spawnAmount;
	public int spawnX;
	public int spawnY;
	public int tillNextSpawn;

	public Round(int a, int b, int c, int x, int y, Path _p, Game g) {
		game = g;
		this.tillNextSpawn = a;
		this.originalTillNext = a;
		this.spawnAmount = b;
		this.p = _p;
		this.roundHealth = c;
		this.spawnX = x;
		this.spawnY = y;
	}

	public void process() {
		if (tillNextSpawn <= 0) {
			if (spawnAmount >= 0
					&& spawnAmount < game.graphicsHandler.pC.length) {
				game.graphicsHandler.pC[spawnAmount].setUse(true);
				game.graphicsHandler.pC[spawnAmount].s.setUse(true);
				game.graphicsHandler.pC[spawnAmount].x = spawnX;
				game.graphicsHandler.pC[spawnAmount].y = spawnY;
				game.graphicsHandler.pC[spawnAmount].path = new Path(p.path,
						p.path.length - 1);
				game.graphicsHandler.pC[spawnAmount].health = roundHealth;
				game.graphicsHandler.pC[spawnAmount].speed = 2;
				tillNextSpawn = originalTillNext + 1;
				// System.out.println("Making sprite at "+spawnAmount);
				spawnAmount--;
			} else {
				roundOver = game.graphicsHandler.roundOver();
			}
		}
		tillNextSpawn--;
	}
}
