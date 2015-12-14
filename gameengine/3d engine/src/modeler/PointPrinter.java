package modeler;

import java.awt.Color;
import java.awt.Graphics2D;

public final class PointPrinter {
	public static void printCircle(int x, int y, int radius, int steps) {
		float theta = 0;  // angle that will be increased each loop
		int h = x;      // x coordinate of circle center
		int k = y;      // y coordinate of circle center
		//int step = 15;  // amount to add to theta each time (degrees)
		float step = 360.0f / steps;

		do {
			System.out.println("("+(h + radius * Math.cos(theta))+", "+(k + radius*Math.sin(theta))+")");
			theta += step;
		} while (theta < 360);
	}
	
	public static Face[] createCylinder(Graphics2D g, float x, float y, float z, float radius, float height, int steps, Color c) {
		float[][] xs = new float[steps][4], ys = new float[steps][4], zs = new float[steps][4];
		float theta = 0;  // angle that will be increased each loop
		float h = x;
		float k = y;
		float step = 360.0f / steps;
		float lastX = 0f, lastY = 0f;
		
		for (int i = 0; i < steps; i++) {
			if (i == 0) {
				lastX = (float) (h + radius * Math.cos(theta));
				lastY = (float) (k + radius*Math.sin(theta));
			} else {
				System.out.println(steps+", "+i);
				float thisX = (float) (h + radius * Math.cos(theta));
				float thisY = (float) (k + radius*Math.sin(theta));
				g.drawLine((int)thisX, (int)thisY, (int)lastX, (int)lastY);
				System.out.println(thisX+", "+thisY);
				xs[i][0] = lastX;
				xs[i][1] = thisX;
				xs[i][2] = thisX;
				xs[i][3] = lastX;
				lastX = thisX;
				ys[i][0] = lastY;
				ys[i][1] = thisY;
				ys[i][2] = thisY;
				ys[i][3] = lastY;
				lastY = thisY;
				zs[i][0] = z;
				zs[i][1] = z;
				zs[i][2] = z + height;
				zs[i][3] = z + height;
			}
			theta += step;
		}
		Face[] faces = new Face[steps];
		for (int i = 0; i < faces.length; i++) {
			faces[i] = new Face(xs[i], ys[i], zs[i], c);
		}
		return faces;
	}
	
	public static Face[] createCylinder(float x, float y, float z, float radius, float height, int steps, Color c, boolean doubleSided) {
		float[][] xs = new float[steps][4], ys = new float[steps][4], zs = new float[steps][4];
		float theta = 0;  // angle that will be increased each loop
		float h = x;
		float k = y;
		float step = (float) ((360.0f / steps));
		float lastX = 0f, lastY = 0f;
		
		for (int i = 0; i < steps; i++) {
			if (i == 0) {
				lastX = (float) (h + radius * Math.cos(theta));
				lastY = (float) (k + radius*Math.sin(theta));
			} else {
				float thisX = (float) (h + radius * Math.cos(theta));
				float thisY = (float) (k + radius*Math.sin(theta));
				xs[i][0] = lastX;
				xs[i][1] = lastX;
				xs[i][2] = thisX;
				xs[i][3] = thisX;
				lastX = thisX;
				ys[i][0] = lastY;
				ys[i][1] = lastY;
				ys[i][2] = thisY;
				ys[i][3] = thisY;
				lastY = thisY;
				zs[i][0] = z;
				zs[i][1] = z + height;
				zs[i][2] = z + height;
				zs[i][3] = z;
			}
			theta += step;
		}
		Face[] faces = new Face[steps];
		for (int i = 0; i < faces.length; i++) {
			faces[i] = new Face(xs[i], ys[i], zs[i], c).setBackFaceVisibility(doubleSided);
		}
		return faces;
	}
}
