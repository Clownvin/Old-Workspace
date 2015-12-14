package world;

public class Path {
	public int maxStage = 0;
	public int[][] path = null;
	public int stage = 0;

	// public int speed = 1;

	public Path(int[][] i, int max) {
		this.path = i;
		this.maxStage = max;
	}

	public int getNextBoundsX() {
		return path[stage][2];
	}

	public int getNextBoundsY() {
		return path[stage][3];
	}

	public int getNextX() {
		return path[stage][0];
	}

	public int getNextY() {
		return path[stage][1];
	}

	public void nextStage() {
		if (stage < maxStage)
			stage++;
	}
}
