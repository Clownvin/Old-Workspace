package world;

public class PathLoader {
	private static final java.util.Random random = new java.util.Random();
	public int[][] path1 = { { 1, 0, 32, 0 }, { 0, 1, 32, 192 },
			{ 1, 0, 256, 192 }, { 0, 1, 256, 480 }, { 1, 0, 384, 480 },
			{ 0, -1, 384, 256 }, { 0, -1, 384, 64 }, { 1, 0, 480, 64 },
			{ 0, 1, 480, 256 }, { 1, 0, 544, 256 }, { 1, 0, 704, 256 },
			{ 1, -1, 768, 192 } };
	int[][] path2 = { { 1, 0, 32, 0 }, { 0, 1, 32, 192 }, { 1, 0, 256, 192 },
			{ 0, 1, 256, 480 }, { 1, 0, 384, 480 }, { 0, -1, 384, 256 },
			{ 0, -1, 384, 64 }, { 1, 0, 480, 64 }, { 0, 1, 480, 256 },
			{ 1, 0, 544, 256 }, { 1, 0, 704, 256 }, { 1, -1, 768, 192 } };
	int[][] path3 = { { 1, 0, 32, 0 }, { 0, 1, 32, 192 }, { 1, 0, 256, 192 },
			{ 0, 1, 256, 480 }, { 1, 0, 384, 480 }, { 0, -1, 384, 256 },
			{ 0, -1, 384, 64 }, { 1, 0, 480, 64 }, { 0, 1, 480, 256 },
			{ 1, 0, 544, 256 }, { 1, 0, 704, 256 }, { 1, -1, 768, 192 } };
	int[][] path4 = { { 1, 0, 32, 0 }, { 0, 1, 32, 192 }, { 1, 0, 256, 192 },
			{ 0, 1, 256, 480 }, { 1, 0, 384, 480 }, { 0, -1, 384, 256 },
			{ 0, -1, 384, 64 }, { 1, 0, 480, 64 }, { 0, 1, 480, 256 },
			{ 1, 0, 544, 256 }, { 1, 0, 704, 256 }, { 1, -1, 768, 192 } };
	public Path[] paths = new Path[4];

	public PathLoader() {
		paths[0] = new Path(path1, path1.length - 1);
		paths[1] = new Path(path2, path2.length - 1);
		paths[2] = new Path(path3, path3.length - 1);
		paths[3] = new Path(path4, path4.length - 1);
	}

	public int nextInt(final int min, final int max) {
		if (max < min) {
			return max + random.nextInt(min - max);
		}
		return min + (max == min ? 0 : random.nextInt(max - min));
	}

	public int randomPath() {
		return nextInt(0, paths.length);
	}
}
