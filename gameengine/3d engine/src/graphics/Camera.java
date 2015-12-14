package graphics;

import client.Client;
import modeler.Point2D;
import modeler.Point3D;

public final class Camera {
	private static final double DEG_TO_RAD = 0.017453292;
	private Point3D coordinates = new Point3D(0, 0, 0f);
	private Point2D screenCenter = new Point2D(0, 0);
	private Point3D screenPosition = new Point3D(0, 0, 40 * 48);
	private float zoom = 10f * 48f;
	private float degTheta = 180f;
	private float degPhi = 180f;
	private float radTheta = (float) ((180) * DEG_TO_RAD);
	private float radPhi = (float) ((180) * DEG_TO_RAD);
	private float ctcp = 0f;
    private float ctsp = 0f;
    private float stcp = 0f;
    private float stsp = 0f;
    private float ct = 0f;
	private float st = 0f;
	private float cp = 0f;
	private float sp = 0f;
	public Point3D viewAngle = new Point3D(0, 180 - 25, 400);

	public Camera() {
	updateCoordinates();
		System.out.println(coordinates.getX() + ", "
				+ coordinates.getY() + ", " + coordinates.getX());
	}
	
	public void changeZoom(int i) {
		System.out.println("Changing zoom");
		zoom += i;
		if (zoom < 40) {
			zoom = 40;
		}
		if (zoom > 2000) {
			zoom = 2000;
		}
	}

	public void updateCameraCoordinates(final float clientX,
			final float clientY, final float clientZ) {
		updateCoordinates();
	}

	public float getZoom() {
		return zoom / 48;
	}

	public void updateTheta(float change) { // In degrees
		degTheta += change;
		degTheta %= 360;
		radTheta = (float) (degTheta * DEG_TO_RAD);
		updateCoordinates();
	}

	public void updatePhi(float change) { // In degrees
		degPhi += change;
		degPhi %= 360;
		radPhi = (float) (degPhi * DEG_TO_RAD);
		updateCoordinates();
	}
	
	public void setScreenCenter(Point2D screenCenter) {
		this.screenCenter = screenCenter;
	}
	
	public Point2D translatePoint3D(Point3D point) {
		//TODO figure out what screen pos is
		/*
		double x = point.getX() * ct - point.getY() * st;
		double y = point.getX() * stsp + point.getY() * ctsp + point.getZ() * cp;
		double temp = 10
			/ ((40 * 48) + point.getX() * stcp + point.getY() * ctcp - point.getZ() * sp);
			 * 
			 */
		double x = screenPosition.x + point.getX() * ct - point.getY() * st;
		double y = screenPosition.y + point.getX() * stsp + point.getY() * ctsp + point.getZ() * cp;
		double temp = viewAngle.z
				/ (screenPosition.z + point.getX() * stcp + point.getY() * ctcp - point.getZ() * sp);
		return new Point2D(
				(float)(screenCenter.getX() + getZoom() * temp * x), 
				(float)(screenCenter.getY() + getZoom() * temp * y));
	}

	public void updateCoordinates() {
		float x, y, z, r = 0;
		float rad = 0;
		r = ((1/zoom) * 100000) * 3f;
		rad = (float) (r * Math.cos(getRadianPhi()));
		z = (float) (r * Math.sin(getRadianPhi()));
		y = (float) (rad * Math.sin(getReferenceAngle(getDegreeTheta())
				* DEG_TO_RAD));
		x = (float) -(rad * Math.cos(getReferenceAngle(getDegreeTheta())
				* DEG_TO_RAD));
		int q = getQuadrant(getDegreeTheta());
		switch (q) {
		case 2:
			x *= -1;
			break;
		case 3:
			x *= -1;
			y *= -1;
			break;
		case 4:
			y *= -1;
			break;
		}
		ct = (float) Math.cos(radTheta);
		st = (float) Math.sin(radTheta);
		cp = (float) Math.cos(radPhi);
		sp = (float) Math.sin(radPhi);
		ctcp = (float) (ct * cp);
		ctsp = (float) (ct * sp);
		stcp = (float) (st * cp);
		stsp = (float) (st * sp);
		coordinates.setX(x);
		coordinates.setY(y);
		coordinates.setZ(z);
	}

	public Point3D getCoordinates() {
		return coordinates;
	}

	public float getDegreeTheta() {
		return degTheta - 90;
	}

	public float getRadianTheta() {
		return (float) (radTheta - (90 * DEG_TO_RAD));
	}

	public float getDegreePhi() {
		return degPhi;
	}

	public float getRadianPhi() {
		return radPhi;
	}

	public float getDegreeTheta(float n) {
		return degTheta + n;
	}

	public float getRadianTheta(float n) {
		return radTheta + n;
	}

	public float getDegreePhi(float n) {
		return degPhi + n;
	}

	public float getRadianPhi(float n) {
		return radPhi + n;
	}

	private static float getReferenceAngle(float angle) {
		angle %= 360f;
		if ((angle - 1) < 90) {
			return angle;
		}
		if ((angle - 1) < 180) {
			return 180f - angle;
		}
		if ((angle - 181) < 90) {
			return angle - 180f;
		}
		return 360f - angle;
	}

	private static int getQuadrant(float angle) {
		angle %= 360f;
		if ((angle - 1) < 90) {
			return 1;
		}
		if ((angle - 1) < 180) {
			return 2;
		}
		if ((angle - 181) < 90) {
			return 3;
		}
		return 4;
	}
}
