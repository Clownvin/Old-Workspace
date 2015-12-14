package com.limeapi.graphics;

public final class GraphicUtils {
    public static double[][] plotCircle(final int numberOfSteps, final double radius, float x, float y) {
	double[][] xy = new double[2][numberOfSteps];
	double step = (2 * Math.PI) / numberOfSteps;
	for (int i = 0; i < numberOfSteps; i++) {
	    xy[0][i] = x + (radius * Math.cos((i + 1) * step));
	    xy[1][i] = y + (radius * Math.sin((i + 1) * step));
	}
	return xy;
    }
}
