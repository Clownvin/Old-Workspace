package main;

import world.BasicMap;
import world.MapPacket;

public class ClickHandler {
	@SuppressWarnings("rawtypes")
	public BasicMap map;

	public ClickHandler(@SuppressWarnings("rawtypes") BasicMap m) {
		map = m;
	}

	public MapPacket getMapTileOfClickArea(int x, int y) {
		x -= 0;
		if (x < 0)
			x = 0;
		y -= 26;
		if (y < 0)
			y = 0;
		for (int i = 0; i < map.map.length; i++) {
			for (int j = 0; j < map.map[i].length; j++) {
				if (map.map[i][j] == null)
					continue;
				if ((x <= map.map[i][j].x + map.map[i][j].width && x >= map.map[i][j].x)
						&& (y <= map.map[i][j].y + map.map[i][j].height && y >= map.map[i][j].y)) {
					MapPacket mapPacket = new MapPacket(i, j, null);
					System.out.println("Clicked on tile of index " + mapPacket);
					return mapPacket;
				}
			}
		}
		return null;
	}

	public MapPacket getMapTileOfNPCInArea(int x1, int y1, int x2, int y2) {
		for (int i = 0; i < map.map.length; i++) {
			for (int j = 0; j < map.map[i].length; j++) {
				if (map.map[i][j] == null)
					continue;
				if ((map.map[i][j].x >= x2 && map.map[i][j].x <= x1)
						&& (map.map[i][j].y >= y2 && map.map[i][j].y <= y1)) {
					return new MapPacket(i, j, 0);
				}
			}
		}
		return null;
	}

	/*
	 * public MapPacket getMapTileOfNPCInClickArea(int x, int y){ MapPacket
	 * mapPacket = getMapTileOfClickArea(x,y); for(Object o :
	 * map.map[mapPacket.x][mapPacket.y].contains){
	 * if(o.getClass().equals(NPC.class)){ mapPacket.other = o; } }
	 * System.out.println("Clicked on tile of index "+mapPacket); return
	 * mapPacket; }
	 */
}
