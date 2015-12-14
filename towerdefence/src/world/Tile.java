package world;

public class Tile<T> {
	public T contains;
	public int height = 0;
	public Object o = null;
	public int width = 0;
	public int x = 0;
	public int y = 0;

	public Tile(int _x, int _y, int width, int height, int containLength) {
		this.x = _x;
		this.y = _y;
		this.width = width;
		this.height = height;
		/*
		 * contains = new DynamicArrayObject[containLength]; for(int i = 0; i <
		 * contains.length; i++){ contains[i] = null; }
		 */
	}

	public int addToContained(T t) {
		/*
		 * for(int i = 0; i < contains.length; i++){
		 * if(contains[i]==null||contains[i]==null){ contains[i] = t; return i;
		 * } }
		 */
		contains = t;
		return -1;
	}

	public T contains() {
		return contains;
	}

	public boolean containsCoord(int x2, int y2) {
		return ((x2 >= x && x2 <= x + width) && (y2 >= y && y2 <= y + height));
	}

	public boolean containsPoint(Point p) {
		return ((p.x >= x && p.x <= x + width) && (p.y >= y && p.y <= y
				+ height));
	}

	public void removeFromContained(int i) {
		contains = null;
	}
}
