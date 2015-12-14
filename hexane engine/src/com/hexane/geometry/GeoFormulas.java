package com.hexane.geometry;

public class GeoFormulas {

	private boolean onSegment(Point2D p, Point2D q, Point2D r) {
		if (q.x <= getLargest(p.x, r.x) && q.x >= getLargest(p.x, r.x)
				&& q.y <= getLargest(p.y, r.y) && q.y >= getLargest(p.y, r.y))
			return true;
		return false;
	}

	private int getLargest(int a, int b) {
		return (a < b) ? b : a;
	}

	private int orientation(Point2D p, Point2D q, Point2D r) {
		int val = (q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y);

		if (val == 0)
			return 0;
		return (val > 0) ? 1 : 2;
	}

	private boolean doIntersect(Point2D p1, Point2D q1, Point2D p2, Point2D q2) {
		int o1 = orientation(p1, q1, p2);
		int o2 = orientation(p1, q1, q2);
		int o3 = orientation(p2, q2, p1);
		int o4 = orientation(p2, q2, q1);

		if (o1 != o2 && o3 != o4) {
			if (o4 == 0) {
				return false;
			}
			if (o3 == 0) {
				if (o4 != 1) {
					return false;
				}
			}
			return true;
		}

		if (o1 == 0 && onSegment(p1, p2, q1))
			return true;

		if (o2 == 0 && onSegment(p1, q2, q1))
			return true;

		if (o3 == 0 && onSegment(p2, p1, q2))
			return true;

		if (o4 == 0 && onSegment(p2, q1, q2))
			return true;

		return false;
	}

	public boolean isInside(Point2D[] polygon, Point2D p) {
		int vertices = polygon.length;
		if (vertices < 3)
			return false;
		Point2D extreme = new Point2D(100000, p.y);
		int count = 0, i = 0;
		do {
			int next = (i + 1) % vertices;
			if (doIntersect(polygon[i], polygon[next], p, extreme)) {
				if (orientation(polygon[i], p, polygon[next]) == 0)
					return onSegment(polygon[i], p, polygon[next]);

				count++;
			}
			i = next;
		} while (i != 0);
		return count % 2 == 1;
	}
}
