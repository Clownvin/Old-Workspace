package modeler;

import java.awt.Color;

import com.engine.util.BinaryOperations;

public class Face {
    public Point3D[] points = new Point3D[0];
    /*
     * int R = (int) (Math.random() * 256); int G = (int) (Math.random() * 256);
     * int B = (int) (Math.random() * 256);
     */
    int R = 0;
    int G = 255;
    int B = 0;
    Color randomColor = new Color(R, G, B);
    public Color c = Color.LIGHT_GRAY;
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
    
    public int averageX, averageY, averageZ;

    public Face(int[] x, int[] y, int[] z) {
	if (x.length != y.length || y.length != z.length) {
	    System.err.println("Invalid face!!!!!");
	    return;
	}
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
	int totZ = 0, totX = 0, totY = 0;
	for (int i = 0; i < x.length; i++) {
	    totZ += z[i];
	    totX += x[i];
	    totY += y[i];
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
	averageZ = totZ / x.length;
	averageY = totY / x.length;
	averageX = totX / x.length;
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

    public void calculateShade() {
	float slope = 0;
	if (lowestZX - highestZX != 0)
	    slope = (lowestZ - highestZ) / (lowestZX - highestZX);
	if (255 - ((absoluteValue(slope) / 2) * 255) <= 255
		&& 255 - ((absoluteValue(slope) / 2) * 255) >= 0)
	    G = (int) (255 - (255 * (absoluteValue(slope) / 2)));
	else
	    System.out.println("Slope = " + (absoluteValue(slope) / 2));
	Color randomColor = new Color(R, G, B);
	c = randomColor;
    }

    public Face(int[] x, int[] y, int[] z, Color c) {
	if (x.length != y.length || y.length != z.length) {
	    System.err.println("Invalid face!!!!!");
	    return;
	}
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
	this.c = c;
    }

    public void setLowestZ(int lowestZ) {
	this.lowestZ = lowestZ;
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

    public Color getColor() {
	return c;
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
