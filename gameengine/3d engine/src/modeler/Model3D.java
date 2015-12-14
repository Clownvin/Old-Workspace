package modeler;

import java.awt.Color;

import com.engine.io.FileManager;
import com.engine.io.FileManagerWriteable;
import com.engine.io.FileType;
import com.engine.util.BinaryOperations;

import util.ColorPalette;

public class Model3D implements com.engine.io.FileManagerWriteable {
	private Face[] modelFaces = new Face[12];
	private int xOffset, yOffset, zOffset;

	public Model3D() {
		Color steel = ColorPalette.mixColors(Color.WHITE, Color.LIGHT_GRAY);
		Color gold = ColorPalette.mixColors(Color.YELLOW, Color.ORANGE);
		modelFaces = new Face[20];
		// x y z
		modelFaces[0] = new Face(new float[] { 0, 400, 400, 0 }, new float[] {
				0, 0, -10, -5 },
				new float[] { 0 + 50, 0 + 50, 40 + 50, 40 + 50 }, ColorPalette.darken(steel, .20));
		modelFaces[1] = new Face(new float[] { 0, 400, 400, 0 }, new float[] {
				-5, -10, 0, 0 }, new float[] { 40 + 50, 40 + 50, 120 + 50,
				120 + 50 }, steel);
		modelFaces[2] = new Face(new float[] { 0, 400, 400, 0 }, new float[] {
				5, 10, 0, 0 },
				new float[] { 40 + 50, 40 + 50, 0 + 50, 0 + 50 }, ColorPalette.darken(steel, .20));
		modelFaces[3] = new Face(new float[] { 0, 400, 400, 0 }, new float[] {
				0, 0, 10, 5 }, new float[] { 120 + 50, 120 + 50, 40 + 50,
				40 + 50 }, steel);
		modelFaces[4] = new Face(new float[] { 400, 600, 400 }, new float[] {
				0, 0, -10 }, new float[] { 0 + 50, 120 + 50, 40 + 50 }, ColorPalette.darken(steel, .10));
		modelFaces[5] = new Face(new float[] { 400, 600, 400 }, new float[] {
				-10, 0, 0 }, new float[] { 40 + 50, 120 + 50, 120 + 50 }, steel);
		modelFaces[6] = new Face(new float[] { 400, 600, 400 }, new float[] {
				10, 0, 0 }, new float[] { 40 + 50, 120 + 50, 0 + 50 }, ColorPalette.darken(steel, .10));
		modelFaces[7] = new Face(new float[] { 400, 600, 400 }, new float[] {
				0, 0, 10 }, new float[] { 120 + 50, 120 + 50, 40 + 50 }, steel);
		modelFaces[8] = new Face(new float[] { 0, -10, -40, 0 }, new float[] {
				5, 5, 13, 17 }, new float[] { -40 + 50, -40 + 50, 40 + 50,
				40 + 50 }, ColorPalette.darken(gold, .20));
		modelFaces[9] = new Face(new float[] { 0, -40, -40, 0 }, new float[] {
				17, 13, 5, 7 }, new float[] { 40 + 50, 40 + 50, 140 + 50,
				140 + 50 }, gold);
		modelFaces[10] = new Face(new float[] { 0, -40, -10, 0 }, new float[] {
				-17, -13, -5, -5 }, new float[] { 40 + 50, 40 + 50, -40 + 50,
				-40 + 50 }, ColorPalette.darken(gold, .20));
		modelFaces[11] = new Face(new float[] { 0, -40, -40, 0 }, new float[] {
				-7, -5, -13, -17 }, new float[] { 140 + 50, 140 + 50, 40 + 50,
				40 + 50 }, gold);
		modelFaces[12] = new Face(new float[] { 0, 0, 0, 0 }, new float[] { -5,
				5, 17, -17 }, new float[] { -40 + 50, -40 + 50, 40 + 50,
				40 + 50 }, gold);
		modelFaces[13] = new Face(new float[] { 0, 0, 0, 0 }, new float[] {
				-17, 17, 7, -7 }, new float[] { 40 + 50, 40 + 50, 140 + 50,
				140 + 50 }, gold);
		modelFaces[14] = new Face(new float[] { 0, 0, -40, -40 }, new float[] {
				-7, 7, 5, -5 }, new float[] { 140 + 50, 140 + 50, 140 + 50,
				140 + 50 }, gold);
		modelFaces[15] = new Face(new float[] { -40, -40, -40, -40 },
				new float[] { -5, 5, 13, -13 }, new float[] { 140 + 50,
						140 + 50, 40 + 50, 40 + 50 }, gold);
		modelFaces[16] = new Face(new float[] { -10, -10, -40, -40 },
				new float[] { 5, -5, -13, 13 }, new float[] { -40 + 50,
						-40 + 50, 40 + 50, 40 + 50 }, ColorPalette.darken(gold, .25));
		modelFaces[17] = new Face(new float[] { -40, -40, -200, -200 },
				new float[] { 5, -5, -5, 5 }, new float[] { 40 + 50, 40 + 50,
						40 + 50, 40 + 50 });
		modelFaces[18] = new Face(new float[] { -40, -40, -200, -200 },
				new float[] { -5, 5, 5, -5 }, new float[] { 60 + 50, 60 + 50,
						60 + 50, 60 + 50 });
		modelFaces[19] = new Face(new float[] { -40, -40, -200, -200 },
				new float[] { 7, 5, 5, 7 }, new float[] { 48 + 50, 40 + 50,
						40 + 50, 48 + 50 });
		for (Face face : modelFaces) {
			face.scaleFace(0.25);
		}
		//modelFaces = PointPrinter.createCylinder(0, 0, 40, 100, 30, 10000, Color.GREEN, true);
 		FileManager.writeFile(this);
	}

	public Face[] getFaces() {
		return modelFaces;
	}

	@Override
	public FileManagerWriteable fromBytes(byte[][] bytes) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[][] toBytes() {
		byte[][] bytes = new byte[modelFaces.length + 1][];
		bytes[0] = BinaryOperations.toBytes(modelFaces.length);
		for (int i = 1; i < modelFaces.length + 1; i++) {
			bytes[i] = modelFaces[i - 1].toBytes();
		}
		return bytes;
	}

	@Override
	public String getFileName() {
		return "TestCylinder";
	}

	@Override
	public FileType getFileType() {
		return FileType.MODEL;
	}
}
