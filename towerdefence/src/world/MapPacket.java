package world;

public class MapPacket {
	public Object other;
	public int x = 0;
	public int y = 0;

	public MapPacket(int _x, int _y, Object other) {
		this.x = _x;
		this.y = _y;
		this.other = other;
	}

	@Override
	public String toString() {
		return "X = " + x + ", Y = " + y + ", Other Info = " + other;
	}
}
