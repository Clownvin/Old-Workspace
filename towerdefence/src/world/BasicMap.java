package world;

public class BasicMap<T> {
	public Tile<T>[][] map = null;

	@SuppressWarnings("unchecked")
	public BasicMap(int rows, int collumns, int tileXSize, int tileYSize) {
		map = new Tile[rows][collumns];
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				map[i][j] = new Tile<T>(i * tileXSize, j * tileYSize,
						tileXSize, tileYSize, 1);
			}
		}
	}

	public MapPacket addToTile(int x, int y, T t) {
		try {
			return new MapPacket(x, y, map[x][y].addToContained(t));
		} catch (ArrayIndexOutOfBoundsException aiobe) {
			System.err.println(aiobe.toString());
			System.err.println("addToTile X: " + x + ", Y: " + y + ", T: " + t);
			System.err.println(this);
		}
		return null;
	}

	public MapPacket addToTileContainingCoord(int x, int y, T t) {
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				if (map[i][j].containsCoord(x, y)) {
					return new MapPacket(i, j, map[i][j].addToContained(t));
				}
			}
		}
		return null;
	}

	public MapPacket addToTileContainingPoint(Point p, T t) {
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				if (map[i][j].containsPoint(p)) {
					return new MapPacket(i, j, map[i][j].addToContained(t));
				}
			}
		}
		return null;
	}

	public Tile<T> getTileAt(int x, int y) {
		return map[x][y];
	}

	public Tile<T> getTileAt(Point p) {
		return map[p.x][p.y];
	}

	public Tile<T> getTileContainingCoord(int x, int y) {
		x -= 3;
		if (x < 0)
			x = 0;
		y -= 26;
		if (y < 0)
			y = 0;
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				if (map[i][j].containsCoord(x, y)) {
					return map[i][j];
				}
			}
		}
		return null;
	}

	public Tile<T> getTileContainingPoint(Point p) {
		int x = p.x;
		int y = p.y;
		x -= 3;
		if (x < 0)
			x = 0;
		y += 7;
		if (y < 0)
			y = 0;
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				if (map[i][j].containsCoord(x, y)) {
					return map[i][j];
				}
			}
		}
		return null;
	}

	public void removeFromTile(int x, int y, int index) {
		map[x][y].removeFromContained(index);
	}

	public void removeFromTile(Point p, int index) {
		map[p.x][p.y].removeFromContained(index);
	}

	@Override
	public String toString() {
		return "Map length X: " + map.length + ", Map length Y" + map[0].length;
	}
}
