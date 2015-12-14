package world;

import graphics.Face;
import client.Client;

public class World {
	public final Client client;
	public Region[][] regions = new Region[3][3];

	public World(final Client client) {
		this.client = client;
		generateRegions();
		generateTiles();
		// Making the dimple things on the map
		regions[1][1].tiles[13][7].face.getPoints()[2].setZ(-50);
		regions[1][1].tiles[14][7].face.getPoints()[3].setZ(-50);
		regions[1][1].tiles[13][8].face.getPoints()[1].setZ(-50);
		regions[1][1].tiles[14][8].face.getPoints()[0].setZ(-50);
		regions[1][1].tiles[13][8].face.getPoints()[2].setZ(-50);
		regions[1][1].tiles[14][8].face.getPoints()[3].setZ(-50);
		regions[1][1].tiles[13][9].face.getPoints()[1].setZ(-50);
		regions[1][1].tiles[14][9].face.getPoints()[0].setZ(-50);

		regions[1][1].tiles[15][7].face.getPoints()[2].setZ(50);
		regions[1][1].tiles[16][7].face.getPoints()[3].setZ(50);
		regions[1][1].tiles[15][8].face.getPoints()[1].setZ(50);
		regions[1][1].tiles[16][8].face.getPoints()[0].setZ(50);
		regions[1][1].tiles[15][8].face.getPoints()[2].setZ(50);
		regions[1][1].tiles[16][8].face.getPoints()[3].setZ(50);
		regions[1][1].tiles[15][9].face.getPoints()[1].setZ(50);
		regions[1][1].tiles[16][9].face.getPoints()[0].setZ(50);
	}

	public void generateRegions() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				regions[i][j] = new Region(client);
			}
		}
	}

	public void generateTiles() {
		for (int rx = 0; rx < 3; rx++) {
			for (int ry = 0; ry < 3; ry++) {
				for (int x = 0; x < 100; x++) {
					for (int y = 0; y < 100; y++) {
						regions[rx][ry].tiles[x][y] = new Tile(client, (x * 48)
								+ (4800 * rx), (y * 48) + (4800 * ry), 1);
					}
				}
			}
		}
	}

	public Face[] getGroundFaces() {
		Face[] face = new Face[100000];
		int counter = 0;
		int[] x = new int[4];
		int[] y = new int[4];
		int[] z = new int[4];
		int widthFix = (int) ((4800 * 1) - (-500 + client.playerX) - (.5 * client.width));
		int heightFix = (int) ((4800 * 1) - (-500 + client.playerY) - (.5 * client.height));
		for (int i = 0; i < 100; i++) {
			for (int j = 0; j < 100; j++) {
				x[0] = regions[1][1].tiles[i][j].face.getPoints()[0].getX()
						- widthFix;
				y[0] = regions[1][1].tiles[i][j].face.getPoints()[0].getY()
						- heightFix;
				z[0] = regions[1][1].tiles[i][j].face.getPoints()[0].getZ();
				x[1] = regions[1][1].tiles[i][j].face.getPoints()[1].getX()
						- widthFix;
				y[1] = regions[1][1].tiles[i][j].face.getPoints()[1].getY()
						- heightFix;
				z[1] = regions[1][1].tiles[i][j].face.getPoints()[1].getZ();
				x[2] = regions[1][1].tiles[i][j].face.getPoints()[2].getX()
						- widthFix;
				y[2] = regions[1][1].tiles[i][j].face.getPoints()[2].getY()
						- heightFix;
				z[2] = regions[1][1].tiles[i][j].face.getPoints()[2].getZ();
				x[3] = regions[1][1].tiles[i][j].face.getPoints()[3].getX()
						- widthFix;
				y[3] = regions[1][1].tiles[i][j].face.getPoints()[3].getY()
						- heightFix;
				z[3] = regions[1][1].tiles[i][j].face.getPoints()[3].getZ();
				face[counter] = new Face(x, y, z).setBackFaceVisibility(false);
				counter++;
			}
		}
		return face;
	}
}
