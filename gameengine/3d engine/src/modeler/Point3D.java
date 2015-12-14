package modeler;

import com.engine.util.BinaryOperations;

public class Point3D {
	public float x = 0f, y = 0f, z = 0f;

	public Point3D(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getZ() {
		return z;
	}

	public Point3D setX(float x) {
		this.x = x;
		return this;
	}

	public Point3D setY(float y) {
		this.y = y;
		return this;
	}

	public Point3D setZ(float z) {
		this.z = z;
		return this;
	}

	public byte[] toBytes() {
		return BinaryOperations.toBytes(new float[] { x, y, z });
	}
}
