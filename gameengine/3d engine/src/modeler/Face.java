package modeler;

import java.awt.Color;

import com.engine.util.BinaryOperations;

public class Face {
	private Point3D[] points = new Point3D[0];
	/*
	 * int R = (int) (Math.random() * 256); int G = (int) (Math.random() * 256);
	 * int B = (int) (Math.random() * 256);
	 */
	private Color c = Color.LIGHT_GRAY;
	private boolean showBackFace = false;
	private Point3D averagePoint = new Point3D(0, 0, 0);
	
	public boolean showBack() {
		return showBackFace;
	}

	public Face(float[] x, float[] y, float[] z) {
		if (x.length != y.length || y.length != z.length) {
			System.err.println("Invalid face!");
			return;
		}
		points = new Point3D[x.length];
		float totZ = 0, totX = 0, totY = 0;
		for (int i = 0; i < x.length; i++) {
			totZ += z[i];
			totX += x[i];
			totY += y[i];
		}
		averagePoint = new Point3D(totX / x.length, totY / y.length, totZ / z.length);
		for (int i = 0; i < x.length; i++) {
			points[i] = new Point3D(x[i], y[i], z[i]);
		}
	}

	public static float absoluteValue(float i) {
		if (i < 0) {
			return i * -1;
		}
		return i;
	}

	public Face(float[] x, float[] y, float[] z, Color c) {
		if (x.length != y.length || y.length != z.length) {
			System.err.println("Invalid face!");
			return;
		}
		points = new Point3D[x.length];
		float totZ = 0, totX = 0, totY = 0;
		for (int i = 0; i < x.length; i++) {
			totZ += z[i];
			totX += x[i];
			totY += y[i];
		}
		averagePoint = new Point3D(totX / x.length, totY / y.length, totZ / z.length);
		for (int i = 0; i < x.length; i++) {
			points[i] = new Point3D(x[i], y[i], z[i]);
		}
		this.c = c;
	}
	
	public Face scaleFace(double modifier) {
		float totZ = 0, totX = 0, totY = 0;
		for (int i = 0; i < points.length; i++) {
			points[i].setX((float) (points[i].getX() * modifier));
			points[i].setY((float) (points[i].getY() * modifier));
			points[i].setZ((float) (points[i].getZ() * modifier));
			totZ += points[i].getZ();
			totX += points[i].getX();
			totY += points[i].getY();
		}
		averagePoint = new Point3D(totX / points.length, totY / points.length, totZ / points.length);
		return this;
	}

	public Face setBackFaceVisibility(boolean visibility) {
		showBackFace = visibility;
		return this;
	}

	public Point3D[] getPoints() {
		return points;
	}

	public Color getColor() {
		return c;
	}
	
	public Point3D getAveragePoint() {
		return averagePoint;
	}

	public byte[] toBytes() {
		byte[] bytes = new byte[4 + (12 * points.length)];
		bytes[0] = (byte) (showBackFace ? 1 : 0);
		bytes[1] = BinaryOperations.toBytes(c.getRed())[3];
		bytes[2] = BinaryOperations.toBytes(c.getGreen())[3];
		bytes[3] = BinaryOperations.toBytes(c.getBlue())[3];
		int index = 4;
		for (int i = 0; i < points.length; i++) {
			byte[] pBytes = points[i].toBytes();
			for (int j = 0; j < pBytes.length; j++) {
				bytes[index++] = pBytes[j];
			}
		}
		return bytes;
	}
}
