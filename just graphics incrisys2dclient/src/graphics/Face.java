package graphics;

import java.awt.Color;

import util.Point3D;

public class Face {
	public Point3D[] points = new Point3D[0];
	public Color c = Color.GREEN;
	public boolean background;
	public boolean showBackFace = false;
	public int lowestZ;
	public int largestX;
	public int smallestX;
	public int largestY;
	public int smallestY;
	public int highestZ;
	public int lowestZX;
	public int lowestZY;
	public int highestZX;
	public int highestZY;

	public Face(int[] x, int[] y, int[] z) {
		points = new Point3D[x.length];
		int cLX = Integer.MIN_VALUE;
		int cLY = Integer.MIN_VALUE;
		int cSX = Integer.MAX_VALUE;
		int cSY = Integer.MAX_VALUE;
		int lZ = Integer.MAX_VALUE;
		int hZ = Integer.MIN_VALUE;
		int hZX = 0;
		int hZY = 0;
		int lZY = 0;
		int lZX = 0;
		for (int i = 0; i < x.length; i++) {
			if (x[i] > cSX) {
				cSX = x[i];
			}
			if (y[i] > cSY) {
				cSY = y[i];
			}
			if (y[i] < cLY) {
				cLY = y[i];
			}
			if (x[i] < cLX) {
				cLX = x[i];
			}
			if (z[i] < lZ) {
				lZ = z[i];
				lZY = y[i];
				lZX = x[i];
			}
			if (z[i] > hZ) {
				hZ = z[i];
				hZY = y[i];
				hZX = x[i];
			}
		}
		lowestZ = lZ;
		highestZ = hZ;
		smallestX = cSX;
		smallestY = cSY;
		largestX = cLX;
		largestY = cLY;
		highestZX = hZX;
		highestZY = hZY;
		lowestZX = lZX;
		lowestZY = lZY;
		for (int i = 0; i < x.length; i++) {
			points[i] = new Point3D(x[i], y[i], z[i]);
		}
	}

	public Face setBackFaceVisibility(boolean visibility) {
		showBackFace = visibility;
		return this;
	}

	public Face setBackground(boolean b) {
		background = b;
		return this;
	}

	public Point3D[] getPoints() {
		return points;
	}
}
