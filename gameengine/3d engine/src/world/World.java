package world;

import modeler.Face;
import client.Client;

public class World {
	static {
		System.setProperty("com.sun.media.jai.disableMediaLib", "true");
	}
	public Region[][] regions = new Region[3][3];

	public World() {
		generateRegions();
		generateTiles();
		/*
		 * regions[0][0].tiles[13][7].face.getPoints()[2].setZ(-50);
		 * regions[0][0].tiles[14][7].face.getPoints()[3].setZ(-50);
		 * regions[0][0].tiles[13][8].face.getPoints()[1].setZ(-50);
		 * regions[0][0].tiles[14][8].face.getPoints()[0].setZ(-50);
		 * regions[0][0].tiles[13][8].face.getPoints()[2].setZ(-50);
		 * regions[0][0].tiles[14][8].face.getPoints()[3].setZ(-50);
		 * regions[0][0].tiles[13][9].face.getPoints()[1].setZ(-50);
		 * regions[0][0].tiles[14][9].face.getPoints()[0].setZ(-50);
		 * 
		 * regions[0][0].tiles[15][7].face.getPoints()[2].setZ(50);
		 * regions[0][0].tiles[16][7].face.getPoints()[3].setZ(50);
		 * regions[0][0].tiles[15][8].face.getPoints()[1].setZ(50);
		 * regions[0][0].tiles[16][8].face.getPoints()[0].setZ(50);
		 * regions[0][0].tiles[15][8].face.getPoints()[2].setZ(50);
		 * regions[0][0].tiles[16][8].face.getPoints()[3].setZ(50);
		 * regions[0][0].tiles[15][9].face.getPoints()[1].setZ(50);
		 * regions[0][0].tiles[16][9].face.getPoints()[0].setZ(50);
		 */
	}

	public void generateRegions() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				regions[i][j] = new Region();
			}
		}
	}

	public void generateTiles() {
		for (int rx = 0; rx < 3; rx++) {
			for (int ry = 0; ry < 3; ry++) {
				for (int x = -9; x < 10; x++) {
					for (int y = -9; y < 10; y++) {
						regions[rx][ry].tiles[x + 10][y + 10] = new Tile((x * 48) + (4800 * rx), (y * 48)
										+ (4800 * ry), 1);
					}
				}
			}
		}
	}

	// HUGE overhead here.
	public Face[] getGroundFaces() {
		Face[] face = new Face[20 * 20];
		int counter = 0;
		float[] x = new float[4];
		float[] y = new float[4];
		float[] z = new float[4];
		for (int i = 0; i < 20; i++) {
			for (int j = 0; j < 20; j++) {
				x[0] = regions[0][0].tiles[i][j].face.getPoints()[0]
						.getX();
				y[0] = regions[0][0].tiles[i][j].face.getPoints()[0]
						.getY();
				z[0] = regions[0][0].tiles[i][j].face.getPoints()[0]
						.getZ();
				x[1] = regions[0][0].tiles[i][j].face.getPoints()[1]
						.getX();
				y[1] = regions[0][0].tiles[i][j].face.getPoints()[1]
						.getY();
				z[1] = regions[0][0].tiles[i][j].face.getPoints()[1]
						.getZ();
				x[2] = regions[0][0].tiles[i][j].face.getPoints()[2]
						.getX();
				y[2] = regions[0][0].tiles[i][j].face.getPoints()[2]
						.getY();
				z[2] = regions[0][0].tiles[i][j].face.getPoints()[2]
						.getZ();
				x[3] = regions[0][0].tiles[i][j].face.getPoints()[3]
						.getX();
				y[3] = regions[0][0].tiles[i][j].face.getPoints()[3]
						.getY();
				z[3] = regions[0][0].tiles[i][j].face.getPoints()[3]
						.getZ();
				face[counter] = new Face(x, y, z).setBackFaceVisibility(false);
				counter++;
			}
		}
		// System.out.println(counter + " Faces.");
		return face;
	}
}
