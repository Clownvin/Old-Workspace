package world;

import java.io.Serializable;

import client.Client;

public class Region implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 853929531749852467L;
	public Tile[][] tiles = new Tile[100][100];

	public Region(Client c) {
		for (int i = 0; i < 100; i++) {
			for (int j = 0; j < 100; j++) {
				tiles[i][j] = new Tile(c, 0, 0, 0);
			}
		}
	}
}
