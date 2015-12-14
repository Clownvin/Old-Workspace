package modeler;

import com.engine.util.BinaryOperations;

public class Point3D {
    public int x = 0, y = 0, z = 0;

    public Point3D(int x, int y, int z) {
	this.x = x;
	this.y = y;
	this.z = z;
    }

    public int getX() {
	return x;
    }

    public int getY() {
	return y;
    }

    public int getZ() {
	return z;
    }

    public void setX(int x) {
	this.x = x;
    }

    public void setY(int y) {
	this.y = y;
    }

    public void setZ(int z) {
	this.z = z;
    }
    
    public byte[] toBytes() {
	return BinaryOperations.toBytes(new int[] {x, y, z});
    }
}
